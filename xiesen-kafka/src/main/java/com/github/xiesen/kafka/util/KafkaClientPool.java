package com.github.xiesen.kafka.util;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.kafka.pojo.ClusterDO;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 谢森
 * @since 2021/4/28
 */
public class KafkaClientPool {
    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaClientPool.class);

    /**
     * AdminClient
     */
    private static Map<Long, AdminClient> AdminClientMap = new ConcurrentHashMap<>();

    private static Map<Long, KafkaProducer<String, String>> KAFKA_PRODUCER_MAP = new ConcurrentHashMap<>();

    private static ReentrantLock lock = new ReentrantLock();


    public static ClusterDO getTmpClusterDO() {
        ClusterDO clusterDO = new ClusterDO();
        clusterDO.setId(1L);
        clusterDO.setClusterName("kafka11");
        clusterDO.setZookeeper("kafka-1:2181/kafka110,kafka-2:2181/kafka110,kafka-3:2181/kafka110");
        clusterDO.setBootstrapServers("kafka-1:19092,kafka-2:19092,kafka-3:19092");
        clusterDO.setSecurityProperties("");
        clusterDO.setJmxProperties("");
        clusterDO.setStatus(1);
        clusterDO.setGmtCreate(new Date());
        clusterDO.setGmtModify(new Date());
        return clusterDO;
    }

    public static AdminClient getAdminClient(Long clusterId) {
        AdminClient adminClient = AdminClientMap.get(clusterId);
        if (adminClient != null) {
            return adminClient;
        }
//        ClusterDO clusterDO = PhysicalClusterMetadataManager.getClusterFromCache(clusterId);
        ClusterDO clusterDO = getTmpClusterDO();

        if (clusterDO == null) {
            return null;
        }
        Properties properties = createProperties(clusterDO, false);
        lock.lock();
        try {
            adminClient = AdminClientMap.get(clusterId);
            if (adminClient != null) {
                return adminClient;
            }
            AdminClientMap.put(clusterId, AdminClient.create(properties));
        } catch (Exception e) {
            LOGGER.error("create kafka admin client failed, clusterId:{}.", clusterId, e);
        } finally {
            lock.unlock();
        }
        return AdminClientMap.get(clusterId);
    }

    public static void closeAdminClient(ClusterDO cluster) {
        if (AdminClientMap.containsKey(cluster.getId())) {
            AdminClientMap.get(cluster.getId()).close();
        }
    }

    public static Properties createProperties(ClusterDO clusterDO, Boolean serialize) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterDO.getBootstrapServers());
        if (serialize) {
            properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization" +
                    ".StringSerializer");
            properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common" +
                    ".serialization.StringSerializer");
        } else {
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common" +
                    ".serialization.StringDeserializer");
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common" +
                    ".serialization.StringDeserializer");
        }
        if (ValidateUtils.isBlank(clusterDO.getSecurityProperties())) {
            return properties;
        }
        Properties securityProperties = JSONObject.parseObject(clusterDO.getSecurityProperties(), Properties.class);
        properties.putAll(securityProperties);
        return properties;
    }

}
