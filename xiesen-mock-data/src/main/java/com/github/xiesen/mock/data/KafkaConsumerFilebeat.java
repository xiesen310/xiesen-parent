package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.xiesen.mock.util.LogRecords;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiese
 * @Description KafkaConsumerDemo
 * @Email xiesen310@163.com
 * @Date 2020/9/19 16:01
 */
public class KafkaConsumerFilebeat {
    public static void main(String[] args) {
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "group6");

        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest ");
//        props.put("client.id", "zy_client_id");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("filebeat"));
        AtomicLong i = new AtomicLong();
        while (true) {
            //  从服务器开始拉取数据

            ConsumerRecords<String, String> records = consumer.poll(100);
            records.forEach(record -> {
//                i.getAndIncrement();
                System.out.println(record.timestamp());
                LogRecords logRecords = JSON.parseObject(record.value(), new TypeReference<LogRecords>() {
                });

//                logRecords.setKfkTs(record.timestamp());

                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(),
                        record.partition(),
                        record.offset(), record.key(), record.value());
                System.out.println("消费了 " + i + " 条数据");
            });
        }

    }
}
