package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.KafkaTools;
import com.github.xiesen.mock.util.ProducerPool;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockLogJson {
    /**
     * 获取维度信息*
     *
     * @return Map<String, String>
     */
    private static Map<String, String> getRandomDimensions() {
        Map<String, String> dimensions = new HashMap<>(7);
        dimensions.put("clustername", "集群");
        dimensions.put("hostname", "zork-rd-dev-7092");
        dimensions.put("appprogramname", "模块");
        dimensions.put("appsystem", "poctest");
        dimensions.put("ip", "192.168.70.92");
        dimensions.put("servicecode", "模块");
        dimensions.put("servicename", "模块");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        Random random = new Random();
        int i = random.nextInt(90) + 5;
        measures.put("lags", (double) i);
        measures.put("memory_used", 0.9);
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("message", "183.95.248.189 - - [23/Jul/2020:08:26:32 +0800] \"GET " +
                "/gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ" +
                "%3D%3D HTTP/1.1\" 200 872 ");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        return normalFields;
    }


    public static String mockJsonLogData() {
        Map<String, Object> bigMap = new HashMap<>();
        String logTypeName = "default_analysis_template";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/var/log/test/access.log";
        String offset = String.valueOf(new Random().nextInt(100000000));

        Map<String, String> dimensions = getRandomDimensions();
        Map<String, Double> measures = getRandomMeasures();
        Map<String, String> normalFields = getRandomNormalFields();

        bigMap.put("logTypeName", logTypeName);
        bigMap.put("timestamp", timestamp);
        bigMap.put("source", source);
        bigMap.put("offset", offset);
        bigMap.put("dimensions", dimensions);
        bigMap.put("measures", measures);
        bigMap.put("normalFields", normalFields);
        return JSON.toJSONString(bigMap);
    }

    public static void main(String[] args) throws Exception {

        String topic = "input-test-2";
        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
//        String bootstrapServers = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";
        long records = 1000L;

        KafkaProducer<String, String> producer = KafkaTools.buildProducer(bootstrapServers, StringSerializer.class.getName());
        for (long index = 0; index < records; index++) {
            System.out.println(mockJsonLogData());
            KafkaTools.send(producer, topic, mockJsonLogData());
            Thread.sleep(2000);
        }

        Thread.sleep(2000);
    }
}
