package com.github.xiesen.kafka3;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiesen
 * @title: KafkaConsumerDemo
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/5/7 9:53
 */
public class KafkaConsumerDemo {
    public static void main(String[] args) {
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", "kafka-1:29092,kafka-2:29092,kafka-3:29092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "xiesen");

        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest ");
//        props.put("client.id", "zy_client_id");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

//        consumer.subscribe(Collections.singletonList("xiesen_test"));
        consumer.subscribe(Collections.singletonList("ods-jzjy-prometheus"));
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
}
