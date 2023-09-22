package com.github.xiesen.mock.data;

import com.github.xiesen.common.avro.AvroDeserializerFactory;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

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
//        props.put("bootstrap.servers", "cs56:9092,cs55:9092,cs54:9092");
//        props.put("bootstrap.servers", "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092");
        props.put("bootstrap.servers", "hadoop102:9092");
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
//        props.put("auto.offset.reset", "earliest");

        System.setProperty("java.security.krb5.conf", "D:\\tmp\\kerberos\\krb5.conf");
        System.setProperty("java.security.auth.login.config", "D:\\tmp\\kerberos\\kafka_server_jaas.conf");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("metric-avro"));
        AtomicLong i = new AtomicLong();
        while (true) {
            //  从服务器开始拉取数据

            ConsumerRecords<String, byte[]> records = consumer.poll(100);

            records.forEach(record -> {
                GenericRecord value =
                        AvroDeserializerFactory.getMetricDeserializer().deserializing(record.value());
                i.getAndIncrement();

                /*if (value.get("metricsetname").equals("zork_error_data")) {
                    System.out.println(value);
                }*/

                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(),
                        record.partition(),
                        record.offset(), record.key(), AvroDeserializerFactory.getMetricDeserializer().deserializing
                        (record.value()));


                System.out.println("消费了 " + i + " 条数据");
            });
        }


    }
}
