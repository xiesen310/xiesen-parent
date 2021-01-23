package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @Description 模拟解析格式数据
 * @Email xiesen310@163.com
 * @Date 2020/12/14 13:21
 */
public class MockMomoAlarmData {

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
        Random random = new Random();
        JSONObject alarmJson = new JSONObject();
        alarmJson.put("alarmTypeName", "alarm_metric");
        alarmJson.put("expressionId", 1);
        alarmJson.put("metricSetName", "cpu_system_metricbeat");
        alarmJson.put("severity", 1);
        alarmJson.put("status", "PROBLEM");
        alarmJson.put("timestamp", "2021-01-12T17:01:47.572+08:00");
//        alarmJson.put("timestamp", DateUtil.getUTCTimeStr());

        String searchSentence = "SELECT mean(\"cores\") AS value  FROM cpu_system_metricbeat WHERE ( \"hostname\" =~ " +
                "/\\.*/ ) AND ( \"ip\" =~ /\\.*/ ) AND ( \"appsystem\" = 'dev_test') AND time >= 1594209600000ms AND " +
                "time < 1594209720000ms GROUP BY time(1m),\"hostname\",\"ip\",\"appsystem\" fill(null)";
        JSONObject extFieldsJson = new JSONObject();
        extFieldsJson.put("uuid", "2a094fd38e894de485ae09820bf5a08c");
        extFieldsJson.put("sourSystem", "1");
        extFieldsJson.put("actionID", "0");
        extFieldsJson.put("mergeTag", "1");
        extFieldsJson.put("connectId", "2a094fd38e894de485ae09820bf5a08c");
        extFieldsJson.put("eventNum", "2");
        extFieldsJson.put("alarmSuppress", "alarmSuppress");
        extFieldsJson.put("alarmWay", "2,2,2");
        extFieldsJson.put("successFlag", "1");
        extFieldsJson.put("expressionId", "2");
        extFieldsJson.put("alarmtime", DateUtil.getUTCTimeStr());
        extFieldsJson.put("calenderId", "1");
        extFieldsJson.put("reciTime", "1594209785705");
        extFieldsJson.put("alarmDetailType", "1");
        extFieldsJson.put("revUsers", "[]");
        extFieldsJson.put("searchSentence", searchSentence);
        alarmJson.put("extFields", extFieldsJson);
        Map<String, Object> sourceMap = new HashMap<>(4);
        sourceMap.put("hostname", "zorkdata1");
        sourceMap.put("ip", "192.168.1.1");
        sourceMap.put("sourSystem", 1);
        sourceMap.put("appsystem", "tdx");
        alarmJson.put("sources", sourceMap);
        String title = "192.168.1.1 指标告警";
        alarmJson.put("title", title);
        if (alarmJson.toJSONString().getBytes().length < 1024) {
            int content = 1011 - alarmJson.toJSONString().getBytes().length;
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < content; j++) {
                str.append("a");
            }
            alarmJson.put("content", str.toString());
        }
        return alarmJson.toJSONString();
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
        String topic = "dwd_alarm_real";
//        String bootstrapServers = "yf172:9092,yf171:9092,yf170:9092";
        String bootstrapServers = "autotest-3:9092,autotest-2:9092,autotest-1:9092";
//        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
        long records = 10L;

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
