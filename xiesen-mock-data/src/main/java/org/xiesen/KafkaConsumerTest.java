package org.xiesen;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.util.Properties;

public class KafkaConsumerTest {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.70.6:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "aaaa");
        KafkaConsumer<Object, Object> kafkaConsumer = new KafkaConsumer<>(props);
        System.out.println(kafkaConsumer);
    }
}
