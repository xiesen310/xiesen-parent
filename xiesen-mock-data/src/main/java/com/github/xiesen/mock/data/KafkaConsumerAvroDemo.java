package com.github.xiesen.mock.data;

import com.github.xiesen.common.avro.AvroDeserializerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiese
 * @Description KafkaConsumerDemo
 * @Email xiesen310@163.com
 * @Date 2020/9/19 16:01
 */
public class KafkaConsumerAvroDemo {
    public static void main(String[] args) {
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", "kafka-1:19092,kafka-2:19092,kafka-3:19092");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.deserializer", StringDeserializer.class.getName());
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeSerializer");
        props.put("value.deserializer", ByteArrayDeserializer.class.getName());
        props.put("group.id", "group9");

        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("windowresult"));
        AtomicLong i = new AtomicLong();
        while (true) {
            //  从服务器开始拉取数据

            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
            records.forEach(record -> {
                i.getAndIncrement();
                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(),
                        record.partition(),
                        record.offset(), record.key(), AvroDeserializerFactory.getMetricDeserializer().deserializing(record.value()));

                System.out.println("消费了 " + i + " 条数据");
            });
        }


    }
}
