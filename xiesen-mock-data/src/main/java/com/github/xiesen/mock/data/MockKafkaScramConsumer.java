package com.github.xiesen.mock.data;

import com.github.xiesen.mock.util.ScramSaslConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.security.auth.login.Configuration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiese
 * @Description KafkaConsumerDemo
 * @Email xiesen310@163.com
 * @Date 2020/9/19 16:01
 */
@SuppressWarnings("all")
public class MockKafkaScramConsumer {
    public static final String BROKER_ADDRESS = "192.168.70.6:39092,192.168.70.7:39092,192.168.70.8:39092";
    public static final String TOPIC = "xiesen";
    public static final String GROUP_ID = "xiesen";

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = getStringKafkaConsumer(BROKER_ADDRESS, GROUP_ID);

        consumer.subscribe(Collections.singletonList(TOPIC));
        AtomicLong i = new AtomicLong();
        while (true) {
            //  从服务器开始拉取数据
            ConsumerRecords<String, String> records = consumer.poll(100);
            records.forEach(record -> {
                i.getAndIncrement();
                System.out.println("数据写入到 kafka 的时间: " + record.timestamp());
                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(),
                        record.partition(),
                        record.offset(), record.key(), record.value());
                System.out.println("消费了 " + i + " 条数据");
            });
        }


    }

    /**
     * 获取 kakfa consumer 客户端
     *
     * @param broker  kafka broker 地址
     * @param groupId kafka consumer group id
     * @return {@link KafkaConsumer}
     */
    private static KafkaConsumer<String, String> getStringKafkaConsumer(String broker, String groupId) {
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", broker);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", groupId);
        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest ");

        // sasl 认证
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        Configuration.setConfiguration(new ScramSaslConfig("xiesen", "xiesen"));

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        return consumer;
    }
}
