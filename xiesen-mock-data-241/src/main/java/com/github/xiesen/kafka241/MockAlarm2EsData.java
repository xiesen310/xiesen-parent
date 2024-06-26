package com.github.xiesen.kafka241;

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
 * @author xiese
 * @Description 模拟 stream 告警数据
 * @Email xiesen310@163.com
 * @Date 2020/7/10 14:39
 */
public class MockAlarm2EsData {
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

    public static String buildAlarmJson() {
        Random random = new Random();
        int i = random.nextInt(10);
        JSONObject alarmJson = new JSONObject();
        alarmJson.put("alarmTypeName", "alarm_metric");
        alarmJson.put("expressionId", i);
        alarmJson.put("metricSetName", "cpu_system_metricbeat");
        alarmJson.put("severity", i * 3);
        alarmJson.put("status", "PROBLEM");
        alarmJson.put("timestamp", DateUtil.getUTCTimeStr());
        String extFields = "{\n" +
                "        \"uuid\":\"2a094fd38e894de485ae09820bf5a08c\",\n" +
                "        \"sourSystem\":\"1\",\n" +
                "        \"actionID\":\"0\",\n" +
                "        \"mergeTag\":\"1\",\n" +
                "        \"connectId\":\"2a094fd38e894de485ae09820bf5a08c\",\n" +
                "        \"eventNum\":\"2\",\n" +
                "        \"alarmSuppress\":\"alarmSuppress\",\n" +
                "        \"alarmWay\":\"2,2,2\",\n" +
                "        \"successFlag\":\"1\",\n" +
                "        \"expressionId\":\"2\",\n" +
                "        \"alarmtime\":\"2020-07-08T20:02:00.000+08:00\",\n" +
                "        \"calenderId\":\"1\",\n" +
                "        \"reciTime\":\"1594209785705\",\n" +
                "        \"alarmDetailType\":\"1\",\n" +
                "        \"revUsers\":\"[]\"\n" +
                "    }";
        String searchSentence = "SELECT mean(\"cores\") AS value  FROM cpu_system_metricbeat WHERE ( \"hostname\" =~ " +
                "/\\.*/ ) AND ( \"ip\" =~ /\\.*/ ) AND ( \"appsystem\" = 'dev_test') AND time >= 1594209600000ms AND " +
                "time < 1594209720000ms GROUP BY time(1m),\"hostname\",\"ip\",\"appsystem\" fill(null)";
        JSONObject extFieldsJson = JSONObject.parseObject(extFields);
        extFieldsJson.put("searchSentence", searchSentence);
        alarmJson.put("extFields", extFieldsJson);
        Map<String, Object> sourceMap = new HashMap<>(4);
        sourceMap.put("hostname", "zorkdata" + i);
        sourceMap.put("ip", "192.168.1." + i);
        sourceMap.put("sourSystem", i);
        sourceMap.put("appsystem", "tdx");
        alarmJson.put("sources", sourceMap);
        String title = "192.168.1." + i + " 指标告警";
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
        String topic = "alarm2es";
//        String bootstrapServers = "zorkdata-91:9092";
//        String bootstrapServers = "zorkdata-95:9092";
//        String bootstrapServers = "zorkdata-92:9092";
        String bootstrapServers = "kafka-1:19092,kafka-3:19092,kafka-2:19092";
        long records = 1000L;

        System.out.println(buildAlarmJson());

        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());

        for (long index = 0; index < records; index++) {
            String message = buildAlarmJson();
            System.out.println(message);
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);

    }
}
