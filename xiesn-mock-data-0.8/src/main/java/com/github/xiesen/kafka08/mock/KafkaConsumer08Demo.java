package com.github.xiesen.kafka08.mock;


import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 谢森
 * @Description KafkaConsumer08Demo
 * @Email xiesen310@163.com
 * @Date 2020/10/24 11:56
 */
public class KafkaConsumer08Demo {
    private final ConsumerConnector consumer;

    public KafkaConsumer08Demo() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "kafka-1:2181/kafka082,kafka-2:2181/kafka082,kafka-3:2181/kafka082");
        props.put("group.id", "jd-group001");
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "largest");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ConsumerConfig config = new ConsumerConfig(props);
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
    }

    public void consume() {

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put("result", new Integer(1));
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(
                new VerifiableProperties());
        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
        KafkaStream<String, String> stream = consumerMap.get("result").get(0);
        ConsumerIterator<String, String> it = stream.iterator();
        while (it.hasNext()) {
            MessageAndMetadata<String, String> next = it.next();
            String key = next.key();
            System.out.println(key);
            System.out.println(it.next().message());
        }
    }

    public static void main(String[] args) {

        new KafkaConsumer08Demo().consume();

    }
}

