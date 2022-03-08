package com.github.xiesen.mock.test;

import com.github.xiesen.common.avro.AvroDeserializer;
import com.github.xiesen.common.avro.AvroDeserializerFactory;
import com.github.xiesen.mock.util.Db;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author 谢森
 * @since 2021/3/17
 */
public class ConsumerKafkaMetricData {
    static BatchCommon<String> stringBatchCommon = null;

    private static void init() {
        Consumer<List<String>> consumer = list -> {
            int size = list.size();
            if (size > 0) {
                Db db = new Db();
                try {
                    for (String name : list) {
                        db.psBatchBizData(name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        stringBatchCommon = new BatchCommon<>(1000, 3000, consumer);

    }


    public static void main(String[] args) throws InterruptedException {
        init();
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", "kafka-1:19092,kafka-2:19092,kafka-3:19092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.BytesDeserializer");
        props.put("group.id", "group7");

        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest ");
//        props.put("client.id", "zy_client_id");

        KafkaConsumer<String, String> consumer = new KafkaConsumer(props, new StringDeserializer(), new MetricDataDeserializer());

        consumer.subscribe(Collections.singletonList("xieseselen_metric_data_test1"));
        AtomicLong i = new AtomicLong();
        while (true) {
            //  从服务器开始拉取数据
            ConsumerRecords<String, String> records = consumer.poll(1000);
            AvroDeserializer metricDeserializer = AvroDeserializerFactory.getMetricDeserializer();
            records.forEach(record -> {
                System.out.println(record.value());
                stringBatchCommon.add(record.value());
                i.getAndIncrement();
                /*System.out.println(record.timestamp());
                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(),
                        record.partition(),
                        record.offset(), record.key(), record.value());*/
                System.out.println("消费了 " + i + " 条数据");
            });
        }

    }
}
