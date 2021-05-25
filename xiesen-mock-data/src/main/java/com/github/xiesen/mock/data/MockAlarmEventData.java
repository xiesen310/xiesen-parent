package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @Description 模拟解析格式数据
 * @Email xiesen310@163.com
 * @Date 2020/12/14 13:21
 */
public class MockAlarmEventData {

    /**
     * kafka producer
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

    public static String buildMsg() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("alarmContent", "[CST May 25 10:49:15] error    : 'noahtest-215' mem usage of 81.4% matches " +
                "resource limit [mem usage > 80.0%]");


        jsonObject.put("lastTime", DateUtil.getUTCTimeStr());
//        jsonObject.put("eventId", "96e35d27835f4b9e91183b3b17656f72");
        jsonObject.put("eventId", UUID.randomUUID().toString().replaceAll("-", ""));
        jsonObject.put("recvUser", "recvUser");

        jsonObject.put("alarmTime", DateUtil.getUTCTimeStr());
        jsonObject.put("ip", "192.168.70.215");
        jsonObject.put("operator", "operator");
        jsonObject.put("alarmCount", 1);
        jsonObject.put("hostname", "noahtest-215");
        jsonObject.put("alarmObject", "122_ff89f1d295fdf1ec6ed5ffae0f33c7e5");
        jsonObject.put("eventStatus", 1);
        jsonObject.put("alarmId", "9307365bf52d4345a3f42e2be5b0d894");
        jsonObject.put("appSystem", "tdx");
        jsonObject.put("eventTime", DateUtil.getUTCTimeStr());
        jsonObject.put("alarmLevel", 5);
        jsonObject.put("alarmTitle", "noahtest-215");
        jsonObject.put("beginTime", DateUtil.getUTCTimeStr());
        jsonObject.put("alarmObjectValue", "122_dev_test_noahtest-215");
        jsonObject.put("ruleId", "122");
        return jsonObject.toJSONString();
    }

    /**
     * 发送数据
     *
     * @param producer
     * @param topic
     * @param message
     * @param <T>
     */
    private static <T> void send(KafkaProducer<String, T> producer, String topic, T message) {
        ProducerRecord<String, T> producerRecord = new ProducerRecord<>(topic, null, message);
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


    public static void main(String[] args) throws InterruptedException {
//        String topic = "hzy_alarm_real";
        String topic = "xiesen_test";
        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
        long records = 10000L;

        System.out.println(buildMsg());

        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());

        for (long index = 0; index < records; index++) {
            String message = buildMsg();
            System.out.println(message);
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);

    }
}
