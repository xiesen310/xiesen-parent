package com.github.xiesen.kafka241;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author 谢森
 * @since 2021/4/14
 */
public class KafkaProducerDemo {
    private static final String topic = "xiesen241";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "zorkdata-95:9092");
        props.put("client.id", "DemoProducer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);
        int messageNo = 1;
        for (int i = 0; i < 10; i++) {
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            producer.send(new ProducerRecord<>(topic, messageNo, messageStr), new DemoCallBack(startTime, messageNo,
                    messageStr)).get();
            ++messageNo;
        }

    }
}
