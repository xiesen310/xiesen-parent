package com.github.xiesen.mock.ops;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * @author xiesen
 * @title: KafkaConsumerWithTimestamp
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/7/1 17:28
 */
public class KafkaConsumerWithTimestamp2 {
    public static final String topic = "test-alarm";
    public static final String brokers = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";

    public static final String groupId = "KafkaConsumerWithTimestamp2-my-consumer-group";

    /**
     * 将指定的时间（2023-12-11 09:00:00） 转换成时间戳
     *
     * @param datetime 2023-12-11 09:00:00
     * @return long
     */
    public static long getTimestamp(String datetime) {
        // 构造指定时间的日期对象
        return DateUtil.parse(datetime).getTime();
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // 设置起始时间
        long startTime = getTimestamp("2023-12-11 09:00:00");

        // 获取分区信息
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        Map<TopicPartition, Long> map = new HashMap<>();
        for (PartitionInfo partitionInfo : partitionInfos) {
            map.put(new TopicPartition(topic, partitionInfo.partition()), startTime);
        }
        final Map<TopicPartition, OffsetAndTimestamp> timestampMap = consumer.offsetsForTimes(map);

        List<TopicPartition> topicPartitionList = new ArrayList<>(timestampMap.keySet());
        // 订阅数据
        consumer.assign(topicPartitionList);
        timestampMap.forEach((topicPartition, offsetAndTimestamp) -> {
            if (offsetAndTimestamp != null) {
                consumer.seek(topicPartition, offsetAndTimestamp.offset());
                int partition = topicPartition.partition();
                long timestamps = offsetAndTimestamp.timestamp();
                long offset = offsetAndTimestamp.offset();
                Console.log("[{}] partition = {} timestamp={} offset = {}", KafkaConsumerWithTimestamp.class.getSimpleName(), partition, timestamps, offset);
            }
        });

        // 消费数据
        long endTime = getTimestamp("2023-12-11 09:10:00");
        long tmp = 0L;
        while (tmp < endTime) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                tmp = record.timestamp();
                // 处理消费的记录
                System.out.println("Received message: " + record.value());
            }
        }
    }
}