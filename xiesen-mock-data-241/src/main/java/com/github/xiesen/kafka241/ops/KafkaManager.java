package com.github.xiesen.kafka241.ops;

import kafka.admin.AdminUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.security.JaasUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiesen
 * @title: KafkaManager
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/7/1 17:29
 */
public class KafkaManager {
    public static final Logger log = LoggerFactory.getLogger(KafkaManager.class);
    private final static String brokers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
    private final static String topic = "xs";
    private final static int minutes = 10;
    private final static String zkConnect = "kafka-1:2181/kafka110,kafka-2:2181/kafka110,kafka-3:2181/kafka110";

    public static void main(String[] args) {
        long ts = System.currentTimeMillis() - 1000 * 60 * minutes;

        KafkaConsumerWithTimestamp kafkaTimeStampConsumer = new KafkaConsumerWithTimestamp(brokers, topic, ts);
        ConsumerRecords<String, String> records = kafkaTimeStampConsumer.getKafkaConsumer().poll(100);
        if (records.count() > 0) {
            log.info("kafka topic {} 最近 {} 分钟有数据", brokers, minutes);
        } else {
//            deleteKafkaTopic(zkConnect, topic);
            log.info("kafka topic {} 最近 {} 分钟无数据", brokers, minutes);
        }
        kafkaTimeStampConsumer.getKafkaConsumer().commitAsync();
        kafkaTimeStampConsumer.close();
    }

    public static void deleteKafkaTopic(String ZkStr, String topic) {
//        ZkUtils zkUtils = ZkUtils.
//                apply(ZkStr, 30000, 30000, JaasUtils.isZkSecurityEnabled());

//        AdminUtils.deleteTopic(zkUtils, topic);
//        zkUtils.close();
    }
}
