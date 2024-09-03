package com.github.xiesen.mock.test;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class KafkaOnlinePartitionsStats {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.70.6:9092");

        try (AdminClient adminClient = AdminClient.create(props)) {
            // 获取集群中的所有broker
            Collection<Node> nodes = adminClient.describeCluster().nodes().get();

            // 获取集群中的所有topic
            ListTopicsResult listTopicsResult = adminClient.listTopics(new ListTopicsOptions().listInternal(true));
            Set<String> topics = listTopicsResult.names().get();

            // 对每个topic发起描述请求
            Map<String, TopicDescription> topicDescriptions = adminClient.describeTopics(topics).all().get();

            // 计算每个节点上的在线分区数量
            int[] onlinePartitionCounts = new int[nodes.size()];
            Map<Integer, Integer> brokerIdToTopicCount = new HashMap<>();
            int i = 0;
            for (Node node : nodes) {
                System.out.println("Broker ID: " + node.id());
                int onlineCount = 0;

                Set<String> topicWithPartitions = new HashSet<>();
                // 遍历所有topic的描述信息
                for (TopicDescription topicDesc : topicDescriptions.values()) {
                    for (TopicPartitionInfo partitionInfo : topicDesc.partitions()) {
                        Set<Integer> ids = new HashSet<>();
                        for (Node replica : partitionInfo.replicas()) {
                            ids.add(replica.id());
                        }
                        ids.add(partitionInfo.leader().id());
                        // 检查分区的leader是否是当前节点
                        if (ids.contains(node.id())) {
                            onlineCount++;
                            topicWithPartitions.add(topicDesc.name());
                        }
                    }
                }
                brokerIdToTopicCount.put(node.id(), topicWithPartitions.size());
                System.out.println("  Number of Online Partitions: " + onlineCount);
                System.out.println("  Topics with Online Partitions: " + topicWithPartitions.size());
                onlinePartitionCounts[i++] = onlineCount;
            }

            // 输出每个节点上的在线分区数量
            for (int j = 0; j < nodes.size(); j++) {
                Node node = nodes.toArray(new Node[0])[j];
                System.out.println("Broker ID: " + node.id() + " has " + onlinePartitionCounts[j] + " online partitions.");
            }
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Error while fetching online partitions: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
