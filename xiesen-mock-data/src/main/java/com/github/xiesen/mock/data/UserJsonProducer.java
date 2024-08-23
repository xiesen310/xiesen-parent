package com.github.xiesen.mock.data;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author xiese
 * @Description 张维指标转指标模拟数据代码
 * @Email xiesen310@163.com
 * @Date 2020/8/5 17:53
 */
public class UserJsonProducer {

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
//        System.setProperty("java.security.krb5.conf", "D:\\tmp\\kerberos\\krb5.conf");
//        System.setProperty("java.security.auth.login.config", "D:\\tmp\\kerberos\\kafka_server_jaas.conf");
//        props.put("security.protocol", "SASL_PLAINTEXT");
//        props.put("sasl.kerberos.service.name", "kafka");
//        props.put("sasl.mechanism", "GSSAPI");

        // sasl 认证
        /*props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");
        Configuration.setConfiguration(new SaslConfig("admin", "admin"));*/


        return new KafkaProducer<>(props);
    }


    public static String buildMsg() {
        JSONObject jsonObject = new JSONObject();
        if (RandomUtil.randomBoolean()) {
            jsonObject.put("user_id", "543462");
            jsonObject.put("item_id", "1715");
            jsonObject.put("category_id", "1464116");
            jsonObject.put("behavior", "pv");
            jsonObject.put("ts", createFormattedLocalDateTime());
        } else {
            jsonObject.put("user_id", "662867");
            jsonObject.put("item_id", "2244074");
            jsonObject.put("category_id", "1575622");
            jsonObject.put("behavior", "learning flink");
            jsonObject.put("ts", createFormattedLocalDateTime());
        }
        return jsonObject.toJSONString();
    }

    public static String createFormattedLocalDateTime() {
        // 创建 LocalDateTime 对象
        LocalDateTime dateTime = LocalDateTime.now();

        // 创建 DateTimeFormatter 对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        // 格式化 LocalDateTime 并返回
        return dateTime.format(formatter);
    }

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
        String topic = "xiesen";
        String bootstrapServers = "192.168.70.6:9092,192.168.70.7:9092,192.168.70.8:9092";
        long records = 1000L;
        System.out.println(buildMsg());
        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());
        long index = 0;
        for (index = 0; index < records; index++) {
            String message = buildMsg();
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();

        Thread.sleep(1000L);
    }
}
