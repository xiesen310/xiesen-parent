package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;

/**
 * @author xiese
 * @Description 模拟 json 数据
 * @Email xiesen310@163.com
 * @Date 2020/6/28 10:08
 */
public class MockGuoSenProdJson {
    private static String topic = "guosen_json";
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
        JSONObject properties = new JSONObject();
        JSONObject lib = new JSONObject();
        JSONObject extractor = new JSONObject();
//        jsonObject.put("login_id", UUID.randomUUID().toString().replaceAll("-", ""));
        jsonObject.put("time", System.currentTimeMillis());
        jsonObject.put("anonymous_id", UUID.randomUUID().toString().replaceAll("-", ""));
        jsonObject.put("event", "$AppEnd");
        jsonObject.put("_track_id", 254411L);
//        jsonObject.put("_flush_time", System.currentTimeMillis());

        properties.put("platform_name", "iOS");
        properties.put("$device_id", UUID.randomUUID().toString());
        properties.put("$os_version", "14.1");
        properties.put("product_name", "金太阳");
        properties.put("$os", "iOS");
        properties.put("$screen_height", 667);
        properties.put("$is_first_day", false);
        properties.put("$app_name", "金太阳T");
        properties.put("$model", "iPhone10,1");
        properties.put("$screen_width", 375);
        properties.put("$app_id", "cn.com.guosen.m.iGuosenSmart");
        properties.put("$app_version", "5.6.5");
        properties.put("$lib", "iOS");
        properties.put("$wifi", true);
        properties.put("$network_type", "WIFI");
        properties.put("$timezone_offset", -480);
        properties.put("$lib_version", "2.1.12");
        properties.put("$ip", "61.141.193.166");
        properties.put("$event_duration", 160.824);
        properties.put("$track_signup_original_id", UUID.randomUUID().toString().replaceAll("-", ""));
        properties.put("$is_login_id", true);
        properties.put("$city", "深圳");
        properties.put("$province", "广东");
        properties.put("$country", "中国");


        jsonObject.put("properties", properties);
        jsonObject.put("distinct_id", UUID.randomUUID().toString().replaceAll("-", ""));
        jsonObject.put("type", "track");

        lib.put("$lib_version", "2.1.12");
        lib.put("$lib", "iOS");
        lib.put("$app_version", "5.6.5");
        lib.put("$lib_method", "autoTrack");

        jsonObject.put("lib", lib);
//        jsonObject.put("device_id", UUID.randomUUID().toString().replaceAll("-", ""));
        jsonObject.put("map_id", UUID.randomUUID().toString().replaceAll("-", ""));
        jsonObject.put("user_id", System.currentTimeMillis());
        jsonObject.put("recv_time", System.currentTimeMillis());

        extractor.put("f", "(dev=10300,ino=1613799705)");
        extractor.put("o", 648);
        extractor.put("n", "access_log.2020113011");
        extractor.put("s", 11110);
        extractor.put("c", 11110);
        extractor.put("e", "data05.sa");

        jsonObject.put("extractor", extractor);
        jsonObject.put("project_id", 1);
        jsonObject.put("project", "default");
        jsonObject.put("ver", 2);
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
