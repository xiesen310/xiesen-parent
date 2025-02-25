package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;

/**
 * @author 谢森
 * @Description 模拟日志合并数据
 * @Email xiesen310@163.com
 * @Date 2020/12/11 11:34
 */
public class MockFlinkSqlJsonData {

    public static void main(String[] args) throws InterruptedException {
        String topic = "logs-topic";
//        String topic = "test";
        String bootstrapServers = "192.168.70.6:9092,192.168.70.7:9092,192.168.70.8:9092";
        long records = 1000L;

        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());
        for (long index = 0; index < records; index++) {
            send(producer, topic, mockJson());
            Thread.sleep(2000);
        }

        Thread.sleep(2000);

    }

    private static String mockJson() {
        JSONObject jsonObject = new JSONObject();
        String utcTimeStr = DateUtil.getUTCTime();
        jsonObject.put("log_time", utcTimeStr);
        jsonObject.put("level", "INFO");
        jsonObject.put("message", utcTimeStr + " INFO org.apache.hadoop.yarn.server.nodemanager.recovery.NMLeveldbStateStoreService: Full compaction cycle completed in 1 msec");
        JSONObject metadata = new JSONObject();
        metadata.put("service_name", "app");
        metadata.put("host_ip", "192.168.1.1");
        jsonObject.put("metadata", metadata);
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
    private static <T> void send(KafkaProducer<String, T> producer, String topic, T message) {
        ProducerRecord<String, T> producerRecord = new ProducerRecord<>(topic, null, message);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (null != exception) {
                    System.out.println(String.format("消息发送失败：%s", metadata.toString()));
                    exception.printStackTrace();
                } else {
                    System.out.println(String.format("消息发送成功：%s", message.toString()));
                }
            }
        });
    }


}
