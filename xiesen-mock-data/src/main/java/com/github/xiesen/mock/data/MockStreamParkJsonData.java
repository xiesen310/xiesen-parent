package com.github.xiesen.mock.data;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author 谢森
 * @Description 模拟日志合并数据
 * @Email xiesen310@163.com
 * @Date 2020/12/11 11:34
 */
public class MockStreamParkJsonData {

    public static void main(String[] args) throws InterruptedException {
        String topic = "user_behavior";
        String bootstrapServers = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";
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
        jsonObject.put("user_id", String.valueOf(RandomUtil.randomInt(10000, 10010)));
        jsonObject.put("item_id", String.valueOf(RandomUtil.randomInt(1000, 1005)));
        jsonObject.put("category_id", RandomUtil.randomBoolean() ? "1464116" : "1575622");
        jsonObject.put("behavior", RandomUtil.randomBoolean() ? "pv" : "learning flink");
        String formattedDateTime = DateUtil.format(DateUtil.parse("2021-02-01 01:00:00"), "yyyy-MM-dd'T'HH:mm:ss'Z'");
        jsonObject.put("ts", formattedDateTime);
        return jsonObject.toJSONString();
    }

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
                }
            }
        });
    }


}
