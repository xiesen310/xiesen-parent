package com.github.xiesen.jmeter.mock;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.constant.LogConstants;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.jmeter.util.Producer;
import org.apache.jmeter.samplers.SampleResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author xiese
 * @Description 流处理日志数据
 * @Email xiesen310@163.com
 * @Date 2020/8/14 12:52
 */
@SuppressWarnings("all")
public class GmasLogData {

    private static String getRandomOffset() {
        Random random = new Random();
        long l = random.nextInt(10000);
        return String.valueOf(l);
    }

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>();

        dimensions.put(LogConstants.STR_HOSTNAME, "zorkdata" + i);

        dimensions.put(LogConstants.STR_IP, "192.168.1." + i);
        dimensions.put(LogConstants.STR_APP_PROGRAM_NAME, "tc50");
        dimensions.put(LogConstants.STR_APP_SYSTEM, "tdx");

        return dimensions;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>();
        normalFields.put(LogConstants.STR_MESSAGE, "data update success");
        return normalFields;
    }

    public static void buildMsg(SampleResult results, Producer producer, String topicName) {
        JSONObject jsonObject = new JSONObject();
        String logTypeName = "stream_log_avro";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/var/log/" + UUID.randomUUID().toString().replace("-", "") + ".log";
        String offset = getRandomOffset();
        Map<String, String> dimensions = getRandomDimensions();
        Map<String, Double> measures = new HashMap<>();
        Map<String, String> normalFields = getRandomNormalFields();
        jsonObject.put(LogConstants.STR_LOG_TYPENAME, logTypeName);
        jsonObject.put(LogConstants.STR_TIMESTAMP, timestamp);
        jsonObject.put(LogConstants.STR_SOURCE, source);
        jsonObject.put(LogConstants.STR_OFFSET, offset);
        jsonObject.put(LogConstants.STR_DIMENSIONS, dimensions);
        jsonObject.put(LogConstants.STR_MEASURES, measures);
        jsonObject.put(LogConstants.STR_NORMAL_FIELDS, normalFields);

        producer.sendLogAvro(topicName, logTypeName, timestamp, source, offset, dimensions, measures, normalFields);
        System.out.println(jsonObject.toJSONString());
        results.setResponseCode("0");
        results.setResponseData(jsonObject.toJSONString(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);

    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        String logTypeName = "stream_log_avro";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/var/log/" + UUID.randomUUID().toString().replace("-", "") + ".log";
        String offset = getRandomOffset();
        Map<String, String> dimensions = getRandomDimensions();
        Map<String, Double> measures = new HashMap<>();
        Map<String, String> normalFields = getRandomNormalFields();
        jsonObject.put(LogConstants.STR_LOG_TYPENAME, logTypeName);
        jsonObject.put(LogConstants.STR_TIMESTAMP, timestamp);
        jsonObject.put(LogConstants.STR_SOURCE, source);
        jsonObject.put(LogConstants.STR_OFFSET, offset);
        jsonObject.put(LogConstants.STR_DIMENSIONS, dimensions);
        jsonObject.put(LogConstants.STR_MEASURES, measures);
        jsonObject.put(LogConstants.STR_NORMAL_FIELDS, normalFields);
        System.out.println(jsonObject.toJSONString());
    }
}
