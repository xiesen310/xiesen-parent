package com.github.xiesen.mock.data;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

/**
 * @author 谢森
 * @Description 模拟Influxdb 聚合数据
 * @Email xiesen310@163.com
 * @Date 2020/12/11 11:34
 */
public class MockInfluxdbAggJsonData {
    private static final List<String> APP_SYSTEM_SET_LIST = Arrays.asList("jhydpt", "JHSystem", "jzjy", "zhlc", "tdx", "jty");
    private static final List<String> LOG_SET_LIST = Arrays.asList("audit_system", "audit_auditd",
            "default_analysis_template", "windows_winlog", "tkernel", "Lv2TradeHoliday");


    public static String randomAppSystemName(List<String> appList) {
        if (null == appList || appList.size() == 0) {
            return APP_SYSTEM_SET_LIST.get(new Random().nextInt(APP_SYSTEM_SET_LIST.size()));
        } else {
            return appList.get(new Random().nextInt(appList.size()));
        }
    }

    public static String randomLogSetName(List<String> nameList) {
        if (null == nameList || nameList.size() == 0) {
            return LOG_SET_LIST.get(new Random().nextInt(LOG_SET_LIST.size()));
        } else {
            return nameList.get(new Random().nextInt(nameList.size()));
        }
    }

    public static String randomIp() {
        int randomNum = RandomUtil.randomInt(2, 255);
        return "10.240.172." + randomNum;
    }

    public static void main(String[] args) throws InterruptedException {
//        String topic = "ace_xs_source";
        String topic = "test";
//        String bootstrapServers = "192.168.70.219:9092";
//        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
        String bootstrapServers = "192.168.70.6:9092,192.168.70.7:9092,192.168.70.8:9092";
        long records = 1000L;

        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());
        for (long index = 0; index < records; index++) {
            send(producer, topic, mockKey(), mockValue());
            Thread.sleep(20);
        }

        Thread.sleep(2000);

    }

    private static String mockKey() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MEASUREMENT", randomLogSetName(null));
        jsonObject.put("SYSTEM", randomAppSystemName(null));
        jsonObject.put("IP", randomIp());
        return jsonObject.toJSONString();
    }

    private static String mockValue() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CNT", RandomUtil.randomInt(100, 200));
        return jsonObject.toJSONString();
    }

    /**
     * 构建 kafkaProducer
     *
     * @param bootstrapServers
     * @param serializerClassName
     * @param <T>
     * @return
     */
    private static <T> KafkaProducer<String, T> buildProducer(String bootstrapServers, String serializerClassName) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", "all");
        props.put("retries", 5);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", serializerClassName);
        props.put("batch.size", 16384);
        props.put("linger.ms", 0);
        props.put("buffer.memory", 33554432);

        // kerberos 认证
        /*System.setProperty("java.security.krb5.conf", "D:\\tmp\\kerberos\\krb5.conf");
        System.setProperty("java.security.auth.login.config", "D:\\tmp\\kerberos\\kafka_server_jaas.conf");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");*/

        // sasl 认证
        /*props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");
        Configuration.setConfiguration(new SaslConfig("admin", "admin"));*/

        return new KafkaProducer<>(props);
    }

    /**
     * 发送数据
     *
     * @param producer
     * @param topic
     * @param message
     * @param <T>
     */
    private static <T> void send(KafkaProducer<String, T> producer, String topic, String key, T message) {
        ProducerRecord<String, T> producerRecord = new ProducerRecord<>(topic, key, message);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (null != exception) {
                    System.out.println(String.format("消息发送失败：%s", metadata.toString()));
                    exception.printStackTrace();
                }
            }
        });
    }


}
