package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
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

    /**
     * 模拟新建消息
     * @return
     */
    public static String buildUpdateMsg() {
        Map<String,Object> map = new HashMap<>();
        map.put("alarmContent", "[CST May 25 10:49:15]");
        map.put("alarmCount", "1");
        map.put("lastTime", DateUtil.getUTCTimeStr());
        map.put("eventId", "nAeVq3kB7nfzsGWqruMT");
//        map.put("eventId", UUID.randomUUID().toString().replaceAll("-", ""));
        map.put("alarmTime", DateUtil.getUTCTimeStr());
        map.put("alarmId", UUID.randomUUID().toString().replaceAll("-",""));
        map.put("alarmLevel", "5");
        map.put("alarmTitle", "noahtest-215");
        map.put("dataType", 2);
        return JSON.toJSONString(map);
    }

    public static String buildNewMsg() {
        Map<String,Object> map = new HashMap<>();
        map.put("alarmContent", "[CST May 25 10:49:15]");


        map.put("lastTime", DateUtil.getUTCTimeStr());

        map.put("alarmCount", "8");
        map.put("eventId", UUID.randomUUID().toString().replaceAll("-", ""));

        map.put("hostname", "noahtest-215");
        map.put("alarmObject", "122_ff89f1d295fdf1ec6ed5ffae0f33c7e5");
        map.put("eventStatus", "1");
        map.put("alarmId", "9307365bf52d4345a3f42e2be5b0d894");
        map.put("appSystem", "tdx");
        map.put("eventTime", DateUtil.getUTCTimeStr());
        map.put("alarmLevel", "5");
        map.put("alarmTitle", "noahtest-215");
        map.put("beginTime", DateUtil.getUTCTimeStr());
        map.put("alarmObjectValue", "122_dev_test_noahtest-215");
        map.put("ruleId", "122");
        map.put("dataType", 1);
        return JSON.toJSONString(map);
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
//        String bootstrapServers = "node120:9092,node121:9092,node122:9092";
        long records = 10000L;

        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());

        for (long index = 0; index < records; index++) {
            String message = buildNewMsg();
            System.out.println(message);
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);

    }
}
