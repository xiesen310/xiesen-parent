package com.github.xiesen.kafka241.ops;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author xiesen
 * @title: KafkaConsumerWithTimestamp
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/7/1 17:28
 */
public class KafkaConsumerWithTimestamp {
    public static final Logger log = LoggerFactory.getLogger(KafkaConsumerWithTimestamp.class);
    private final KafkaConsumer<String, String> kafkaConsumer;

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public KafkaConsumerWithTimestamp(String brokers, String topic, Long timestamp) {
        Properties props = new Properties();
        //集群地址
        props.put("bootstrap.servers", brokers);

        //设置我们独特的消费者的组id
        props.put("group.id", "kafka-data-check");
        //设置手动提交
        props.put("enable.auto.commit", "false");
        //这个可以设置大一点
        props.put("session.timeout.ms", "30000");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //我一般测试单条报错数据，1
        props.put("max.poll.records", 1500);
        kafkaConsumer = new KafkaConsumer<>(props);
        Map<TopicPartition, Long> map = new HashMap<>();
        final List<PartitionInfo> partitionInfos = kafkaConsumer.partitionsFor(topic);
        for (PartitionInfo partitionInfo : partitionInfos) {
            map.put(new TopicPartition(topic, partitionInfo.partition()), timestamp);
        }
        final Map<TopicPartition, OffsetAndTimestamp> timestampMap = kafkaConsumer.offsetsForTimes(map);

        List<TopicPartition> topicPartitionList = new ArrayList<>(timestampMap.keySet());
        kafkaConsumer.assign(topicPartitionList);
        timestampMap.forEach((topicPartition, offsetAndTimestamp) -> {
            if (offsetAndTimestamp != null) {
                kafkaConsumer.seek(topicPartition, offsetAndTimestamp.offset());
                int partition = topicPartition.partition();
                long timestamps = offsetAndTimestamp.timestamp();
                long offset = offsetAndTimestamp.offset();
                log.info("[{}] partition = {} timestamp={} offset = {}", KafkaConsumerWithTimestamp.class.getSimpleName(), partition, timestamps, offset);
            }
        });
    }

    public void close() {
        if (kafkaConsumer != null) {
            log.info("[{}] close topic = {}", KafkaConsumerWithTimestamp.class.getSimpleName(), JSON.toJSONString(kafkaConsumer.listTopics()));
            kafkaConsumer.close();
        }
    }
}