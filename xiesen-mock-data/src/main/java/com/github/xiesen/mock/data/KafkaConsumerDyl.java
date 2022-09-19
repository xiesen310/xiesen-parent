package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiese
 * @Description KafkaConsumerDemo
 * @Email xiesen310@163.com
 * @Date 2020/9/19 16:01
 */
public class KafkaConsumerDyl {
    public static void main(String[] args) {
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", "192.168.70.76:9092");
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
        consumer.subscribe(Collections.singletonList("skywalking-zork-span-test"));
        AtomicLong i = new AtomicLong();
        while (true) {
            //  从服务器开始拉取数据

            ConsumerRecords<String, String> records = consumer.poll(100);
            records.forEach(record -> {
                i.getAndIncrement();
//                System.out.println("数据写入到 kafka 的时间: " + record.timestamp());
//                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(),
//                        record.partition(),
//                        record.offset(), record.key(), record.value());
                String value = record.value();
                JSONObject jsonObject = JSONObject.parseObject(value);
                Map<String, String> dimensions = (Map<String, String>) jsonObject.get("dimensions");
                String endpoint = dimensions.get("endpoint");
                System.out.println("endpoint = " + endpoint);
                System.out.println("消费了 " + i + " 条数据");
            });
        }


    }
}
