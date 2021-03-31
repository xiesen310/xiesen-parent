package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author xiese
 * @Description 模拟 json 数据
 * @Email xiesen310@163.com
 * @Date 2020/6/28 10:08
 */
public class MockTestJson {
    private static String topic = "xiesen_test1";
    private static String brokerAddr = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
    private static ProducerRecord<String, String> producerRecord = null;
    private static KafkaProducer<String, String> producer = null;

    public static void init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerAddr);
        props.put("acks", "1");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", StringSerializer.class.getName());
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        producer = new KafkaProducer<String, String>(props);
    }

    /**
     * 模拟消息
     *
     * @return
     */
    public static String buildMsg() {
        JSONObject jsonObject = new JSONObject();
        JSONObject dimensions = new JSONObject();
        JSONObject normalFields = new JSONObject();
        normalFields.put("message", "lisi111");
        dimensions.put("appsystem", "minicomputer");
        dimensions.put("hostname", "HMC01");
        dimensions.put("ip", "10.200.68.103");

        jsonObject.put("logTypeName", "test_yunxin_log");
        jsonObject.put("timestamp", DateUtil.getUTCTimeStr());
        jsonObject.put("dimensions", dimensions);
        jsonObject.put("normalFields", "test_yunxin_log");
        return jsonObject.toString();
    }

    /**
     * 发送数据
     *
     * @param topic
     */
    public static void send(String topic) {
        init();
        String req = buildMsg();
        System.out.println(req);
        producerRecord = new ProducerRecord<String, String>(
                topic,
                null,
                req
        );
        producer.send(producerRecord);
    }

    /**
     * 主函数
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i <= 1000; i++) {
            send(topic);
            Thread.sleep(2000);
        }
    }
}
