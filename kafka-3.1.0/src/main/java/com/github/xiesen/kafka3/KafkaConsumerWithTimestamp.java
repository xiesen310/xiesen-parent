package com.github.xiesen.kafka3;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author xiesen
 * @title: KafkaConsumerDemo
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/5/7 9:53
 */
@Slf4j
public class KafkaConsumerWithTimestamp {
    public static final String topic = "xs";
    public static final String brokers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";

    private final KafkaConsumer<String, String> kafkaConsumer;

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


    private void close() {
        if (kafkaConsumer != null) {
            log.info("[{}] close topic = {}", KafkaConsumerWithTimestamp.class.getSimpleName(), JSON.toJSONString(kafkaConsumer.listTopics()));
            kafkaConsumer.close();
        }
    }

    public static AdminClient getKafkaAdminClient(String brokers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return KafkaAdminClient.create(props);
    }

    public static Set<String> listKafkaTopics(String brokers) throws ExecutionException, InterruptedException {
        AdminClient kafkaAdminClient = getKafkaAdminClient(brokers);
        ListTopicsResult listTopicsResult = kafkaAdminClient.listTopics();
        Set<String> set = listTopicsResult.names().get();
        set.remove("__consumers_offsets");
        return set;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /// hasStreamData();
        Set<String> set = KafkaConsumerWithTimestamp.listKafkaTopics(brokers);
        for (String topic : set) {
            String s = hasStreamData(topic);
            if (null != s) {
                deleteTopic(s);
            }
        }


        System.out.println("删除topic后的 topic 列表");
        for (String kafkaTopic : KafkaConsumerWithTimestamp.listKafkaTopics(brokers)) {
            System.out.println(kafkaTopic);
        }


    }

    /**
     * 删除 topic
     *
     * @param topics topics
     */
    private static void deleteTopic(String... topics) {
        AdminClient adminClient = KafkaConsumerWithTimestamp.getKafkaAdminClient(brokers);
        ArrayList<String> list = new ArrayList<>();
        Arrays.stream(topics).forEach((s -> list.add(s)));
        adminClient.deleteTopics(list);
        adminClient.close();
    }

    /**
     * 删除 topics
     *
     * @param topics topics
     */
    private static void deleteTopic(Set<String> topics) {
        ArrayList<String> list = new ArrayList<>();
        topics.stream().forEach(topic -> list.add(topic));
        AdminClient adminClient = KafkaConsumerWithTimestamp.getKafkaAdminClient(brokers);
        adminClient.deleteTopics(list);
        adminClient.close();
    }

    /**
     * 删除 topic
     *
     * @param topic topic
     */
    private static void deleteTopic(String topic) {
        long minutes = 5;
        long ts = System.currentTimeMillis() - 1000 * 60 * minutes;
        KafkaConsumerWithTimestamp kafkaTimeStampConsumer = new KafkaConsumerWithTimestamp(brokers, topic, ts);
        ConsumerRecords<String, String> records = kafkaTimeStampConsumer.kafkaConsumer.poll(Duration.ofSeconds(5));
        if (records.count() > 0) {
            log.info("kafka topic {} 最近 {} 分钟有数据", topic, minutes);
        } else {
            log.info("kafka topic {} 最近 {} 分钟无数据", topic, minutes);
            AdminClient adminClient = KafkaConsumerWithTimestamp.getKafkaAdminClient(brokers);
            adminClient.deleteTopics(Arrays.asList(topic));
            adminClient.close();
        }
        kafkaTimeStampConsumer.close();
    }

    /**
     * 判断是否有实时数据,如果没有实时数据,返回无数据 topic 集合
     *
     * @return {@link Set<String> set }
     * @throws ExecutionException   {@link ExecutionException}
     * @throws InterruptedException {@link InterruptedException}
     */
    private static Set<String> hasRealTimeData() throws ExecutionException, InterruptedException {
        Set<String> noDataTopics = new HashSet<>();
        Set<String> set = KafkaConsumerWithTimestamp.listKafkaTopics(brokers);
        for (String topic : set) {
            String result = hasStreamData(topic);
            if (null != result) {
                noDataTopics.add(result);
            }
        }
        return noDataTopics;
    }

    /**
     * 判断 topic 中是否有实时数据,如果没有实时数据; 返回 topic 反之,返回 null;
     *
     * @param topic topic
     * @return String
     */
    private static String hasStreamData(String topic) {
        String result = null;
        long minutes = 5;
        long ts = System.currentTimeMillis() - 1000 * 60 * minutes;
        KafkaConsumerWithTimestamp kafkaTimeStampConsumer = new KafkaConsumerWithTimestamp(brokers, topic, ts);
        ConsumerRecords<String, String> records = kafkaTimeStampConsumer.kafkaConsumer.poll(Duration.ofSeconds(5));
        if (records.count() > 0) {
            log.info("kafka topic {} 最近 {} 分钟有数据", topic, minutes);
        } else {
            log.info("kafka topic {} 最近 {} 分钟无数据", topic, minutes);
            result = topic;
        }
        kafkaTimeStampConsumer.kafkaConsumer.commitAsync();
        if (null != kafkaTimeStampConsumer) {
            kafkaTimeStampConsumer.close();
        }

        return result;
    }

}
