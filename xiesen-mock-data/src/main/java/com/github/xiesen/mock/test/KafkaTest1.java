package com.github.xiesen.mock.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class KafkaTest1 {
    public static void main(String[] args) {
        KafkaTest1 kafkaTest1 = new KafkaTest1();
        List<TopicPartition> partitions = kafkaTest1.getPartitionsByGroupId("xiesen");
        partitions.forEach(topicPartition -> {
            System.out.println(topicPartition.topic() + ":" + topicPartition.partition());
        });
    }
    /**
     * 根据 groupId 获取分区信息
     *
     * @param groupId 消费者组 ID
     * @return 包含 KafkaTopicPartition 对象的列表，每个对象代表一个分区的信息
     */
    private List<TopicPartition> getPartitionsByGroupId(String groupId) {
        List<TopicPartition> partitions = new ArrayList<>();
//        String bootstrapServers = config.getString("consumer.bootstrap.servers");
        String bootstrapServers = "192.168.70.6:9092,192.168.70.7:9092,192.168.70.8:9092";

        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // 订阅一个虚拟主题，以便触发分区分配
            consumer.subscribe(Collections.singletonList("__consumer_offsets"));

            // 等待分区分配完成
            consumer.poll(100);

            // 获取分配给消费者组的分区信息
            List<TopicPartition> assignedPartitions = new ArrayList<>(consumer.assignment());
            consumer.endOffsets(assignedPartitions);
            for (TopicPartition topicPartition : assignedPartitions) {
                partitions.add(new TopicPartition(topicPartition.topic(), topicPartition.partition()));
            }
        } catch (Exception e) {
            log.error("Error while fetching partitions by groupId: {}", e.getMessage(), e);
        }

        return partitions;
    }
}
