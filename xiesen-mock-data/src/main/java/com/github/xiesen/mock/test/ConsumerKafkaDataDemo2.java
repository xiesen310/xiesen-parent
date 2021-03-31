package com.github.xiesen.mock.test;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 谢森
 * @since 2021/3/17
 */
public class ConsumerKafkaDataDemo2 {
    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", "kafka-1:19092,kafka-2:19092,kafka-3:19092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "group5");

        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest ");
//        props.put("client.id", "zy_client_id");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("xiesen"));
        AtomicLong i = new AtomicLong();
        while (true) {
            //  从服务器开始拉取数据
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000000000));
            records.forEach(record -> {
                i.getAndIncrement();
                System.out.println(record.timestamp());
                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(),
                        record.partition(),
                        record.offset(), record.key(), record.value());
                System.out.println("消费了 " + i + " 条数据");
            });
        }


    }
}
