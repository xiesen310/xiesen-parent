package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;

/**
 * {"labels":{"__name__":"prometheus_tsdb_time_retentions_total","instance":"192.168.1.92:9090","job":"prometheus"},"name":"prometheus_tsdb_time_retentions_total","timestamp":"2022-10-18T01:41:26Z","value":"0"}*
 *
 * @author xiese
 * @Description 模拟 json 数据
 * @Email xiesen310@163.com
 * @Date 2020/6/28 10:08
 */
public class MockPrometheusJson {
    private static String topic = "xiesen-test-json";
    private static String brokerAddr = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";
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
        JSONObject labels = new JSONObject();
        String name = "prometheus_tsdb_time_retentions_total";
        labels.put("__name__", name);
        labels.put("instance", "192.168.1.92:9090");
        labels.put("job", "prometheus");

        jsonObject.put("labels", labels);
        jsonObject.put("name", name);
        jsonObject.put("timestamp", "2022-10-18T01:41:26Z");
        jsonObject.put("value", "0");
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
