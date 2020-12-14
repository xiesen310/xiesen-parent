package com.github.xiesen.jmeter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.jmeter.mock.*;
import com.github.xiesen.jmeter.util.Producer;
import com.github.xiesen.jmeter.util.ProducerPool;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * @author xiese
 * @Description 流处理模拟数据
 * @Email xiesen310@163.com
 * @Date 2020/8/14 12:35
 */
public class StreamMockData extends AbstractJavaSamplerClient {
    private static final Logger logger = LoggerFactory.getLogger(StreamMockData.class);

    private static final String KAFKA_BROKER = "kafkaBroker";
    private static final String KAFKA_TOPIC_NAME = "topicName";
    private static final String DATA_TYPE = "dataType";
    private static final String JSON_DATA = "jsonData";
    private static final String STR_JSON = "json";
    private static final String STR_LOG_AVRO = "logavro";
    private static final String STR_METRIC_AVRO = "metricavro";
    private static final String STR_XIESEN_LOG_AVRO = "xiesenlogavro";
    private static final String STR_ALARM = "alarm";
    private static final String STR_ALARM_PUSH = "alarmPush";
    private static final String STR_HADOOP = "hadoop";
    private static final String STR_TEST_HADOOP = "test_hadoop";
    private static final String STR_STREAM_JSON = "stream_json";
    private static final String STR_OPEN_FALCON = "open_falcon";

    /**
     * kafka地址
     */
    private String kafkaAddress;

    /**
     * topic的名称
     */
    private String topicName;

    /**
     * 数据格式：json、logavro、metricavro
     */
    private String dataType;

    /**
     * 数据样例
     */
    private String jsonData;

    /**
     * 这个方法用来控制显示在GUI页面的属性，由用户来进行设置。
     * 此方法不用调用，是一个与生命周期相关的方法，类加载则运行。
     *
     * @return
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument(KAFKA_BROKER, "kafka1:9092,kafka2:9092,kafka3:9092");
        arguments.addArgument(KAFKA_TOPIC_NAME, "test");
        arguments.addArgument(DATA_TYPE, STR_TEST_HADOOP);
        arguments.addArgument(JSON_DATA, "");
        return arguments;
    }

    /**
     * 初始化方法，初始化性能测试时的每个线程
     * 实际运行时每个线程仅执行一次，在测试方法运行前执行，类似于LoadRunner中的init方法
     *
     * @param jsc
     */
    @Override
    public void setupTest(JavaSamplerContext jsc) {
        kafkaAddress = jsc.getParameter(KAFKA_BROKER);
        topicName = jsc.getParameter(KAFKA_TOPIC_NAME);
        dataType = jsc.getParameter(DATA_TYPE);
        jsonData = jsc.getParameter(JSON_DATA);
    }


    /**
     * 性能测试时的线程运行体
     * 测试执行的循环体，根据线程数和循环次数的不同可执行多次，类似于Loadrunner中的Action方法
     *
     * @param javaSamplerContext
     * @return
     */
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult results = new SampleResult();
        Producer producer = ProducerPool.getInstance(kafkaAddress).getProducer();
        JSONObject jsonObject = null;
        if (jsonData != null) {
            jsonObject = JSON.parseObject(jsonData);
            System.out.println(jsonObject);
        }
        switch (dataType) {
            case STR_JSON:
                mockJsonData(results, producer, jsonObject);
                break;
            case STR_LOG_AVRO:
                mockLogAvroData(results, producer, jsonObject);
                break;
            case STR_METRIC_AVRO:
                mockMetricAvroData(results, producer, jsonObject);
                break;
            case STR_XIESEN_LOG_AVRO:
                StreamLogData.buildMsg(results, producer, topicName);
                break;
            case STR_ALARM:
                StreamAlarmData.buildAlarmData(results, producer, topicName);
                break;
            case STR_HADOOP:
                StreamHadoopData.buildHadoopData(results, producer, topicName);
                break;
            case STR_TEST_HADOOP:
                StreamHadoopDataTest.buildHadoopData(results, producer, topicName, jsonObject);
                break;
            case STR_ALARM_PUSH:
                mockAlarmPushJsonData(results, producer);
                break;
            case STR_STREAM_JSON:
                StreamJsonData.buildStreamJson(results, producer, topicName);
                break;
            case STR_OPEN_FALCON:
                Alarm2OpenFalconData.buildAlarm2OpenFalcon(results, producer, topicName);
                break;
            default:
                logger.error("不支持{}类型的数据", dataType);
        }
        return results;
    }

    /**
     * 模拟指标 avro 数据
     *
     * @param results
     * @param producer
     * @param jsonObject
     */
    private void mockMetricAvroData(SampleResult results, Producer producer, JSONObject jsonObject) {
        Map<String, String> dimensions;
        String metricSetName = jsonObject.get("metricSetName").toString();
        Map<String, String> metric = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("metric")), Map.class);
        Iterator<Map.Entry<String, String>> iterator = metric.entrySet().iterator();
        Map<String, Double> metricMap = new HashMap<>(3);
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            metricMap.put(next.getKey(), Double.parseDouble(String.valueOf(next.getValue())));
        }
        dimensions = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("dimensions")), Map.class);
        producer.sendMetricAvro(topicName, metricSetName, DateUtil.getUTCTimeStr(), dimensions, metricMap);
        results.setResponseCode("0");
        results.setResponseData(jsonObject.toJSONString(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);
    }

    /**
     * 模拟日志 avro 数据
     *
     * @param results
     * @param producer
     * @param jsonObject
     */
    private void mockLogAvroData(SampleResult results, Producer producer, JSONObject jsonObject) {
        String logTypeName = jsonObject.get("logTypeName").toString();
        String source = jsonObject.get("source").toString();
        String offset = jsonObject.get("offset").toString();
        Map<String, Double> metrics = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("measures")), Map.class);
        Map<String, String> dimensions = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("dimensions")),
                Map.class);
        Map<String, String> normalFields = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("normalFields")),
                Map.class);
        producer.sendLogAvro(topicName, logTypeName, DateUtil.getUTCTimeStr(), source, offset, dimensions, metrics,
                normalFields);
        results.setResponseCode("0");
        results.setResponseData(jsonObject.toJSONString(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);
    }

    /**
     * 模拟 json 格式数据
     *
     * @param results
     * @param producer
     * @param jsonObject
     */
    private void mockJsonData(SampleResult results, Producer producer, JSONObject jsonObject) {
        jsonObject.put("@timestamp", Instant.now().toString());
        jsonObject.put("offset", System.currentTimeMillis());
        producer.sendJson(topicName, jsonObject.toJSONString());
        results.setResponseCode("0");
        results.setResponseData(jsonObject.toJSONString(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);
    }

    /**
     * 模拟告警推送 json 格式数据
     *
     * @param results
     * @param producer
     */
    private void mockAlarmPushJsonData(SampleResult results, Producer producer) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", UUID.randomUUID());
        jsonObject.put("alarmChannelId", new Random().nextLong());
        jsonObject.put("appSystem", "tdx");
        jsonObject.put("time", DateUtil.getUTCTimeStr());
        jsonObject.put("title", "time");
        jsonObject.put("content", "content");
        jsonObject.put("level", 1);
        jsonObject.put("type", 1);
        jsonObject.put("pushResult", 1);
        jsonObject.put("sendMode", 1);
        jsonObject.put("recipient", "recipient");
        jsonObject.put("contactWay", "contactWay");

        producer.sendJson(topicName, jsonObject.toJSONString());
        results.setResponseCode("0");
        results.setResponseData(jsonObject.toJSONString(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);
    }

    /**
     * 测试结束方法，结束测试中的每个线程
     * 实际运行时，每个线程仅执行一次，在测试方法运行结束后执行，类似于Loadrunner中的End方法
     *
     * @param args
     */
    @Override
    public void teardownTest(JavaSamplerContext args) {

    }
}
