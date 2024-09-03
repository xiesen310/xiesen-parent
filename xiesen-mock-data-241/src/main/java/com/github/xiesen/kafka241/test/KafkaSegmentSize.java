package com.github.xiesen.kafka241.test;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class KafkaSegmentSize {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.70.6:9092");
        props.put(AdminClientConfig.CLIENT_ID_CONFIG, "kafka-segment-size-client");

        try (AdminClient adminClient = AdminClient.create(props)) {
            // 获取集群中的所有broker
            Collection<Node> nodes = adminClient.describeCluster().nodes().get();
            Node controller = adminClient.describeCluster().controller().get();
            Collection<ConfigResource> configsToDescribe = new ArrayList<>();
            ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, controller.idString());
            configsToDescribe.add(configResource);
            Long logSegmentBytes = 0L;
            // 查询集群配置信息
            DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(configsToDescribe);
            for (Map.Entry<ConfigResource, Config> entry : describeConfigsResult.all().get().entrySet()) {
                ConfigResource resource = entry.getKey();
                Config config = entry.getValue();
                for (ConfigEntry configEntry : config.entries()) {
                    if ("log.segment.bytes".equals(configEntry.name())) {
                        System.out.println("Broker ID: " + resource.name() + " Log Segment Bytes: " + configEntry.value());
                        logSegmentBytes = Long.parseLong(configEntry.value());
                        break;
                    }
                }
            }

            // 对每个节点发起日志目录描述请求
            for (Node node : nodes) {
                DescribeLogDirsResult describeLogDirsResult = adminClient.describeLogDirs(Collections.singletonList(node.id()));
                Map<Integer, Map<String, LogDirDescription>> descriptions = describeLogDirsResult.allDescriptions().get();

                // 输出日志目录的信息
                if (!descriptions.isEmpty()) {
                    Map<String, LogDirDescription> logDirDescriptionMap = descriptions.values().iterator().next();
                    Long finalLogSegmentBytes = logSegmentBytes;
                    logDirDescriptionMap.forEach((logDirPath, logDirDescription) -> {
                        System.out.println("Broker ID: " + node.id());
                        AtomicReference<Long> currentNodeSize = new AtomicReference<>(0L);
                        System.out.println("  Log Directory: " + logDirPath);
                        AtomicReference<Long> segmentSize = new AtomicReference<>(0L);
                        logDirDescription.replicaInfos().forEach((topicPartition, partitionReplicaDescription) -> {
                            currentNodeSize.updateAndGet(v -> v + partitionReplicaDescription.size());
                            long size = partitionReplicaDescription.size();
                            if (size > 0) {
                                long topicSegmentSize = (long) Math.floor(Math.floorDiv(size, finalLogSegmentBytes)) + 1;
                                segmentSize.updateAndGet(v -> v + topicSegmentSize);
                            } else {
                                segmentSize.updateAndGet(v -> v + 1);
                            }
//                            System.out.println("    Topic: " + topicPartition.topic() + " Partition: " + topicPartition.partition() + " Size: " + size);
                        });

                        System.out.println("Broker ID: " + node.id() + " Total Size: " + currentNodeSize.get() + " bytes" + " " + currentNodeSize.get() / 1024 / 1024 / 1024 + " GB " + "Segment Size: " + segmentSize.get());
                    });

                } else {
                    System.out.println("No log directory information available for Broker ID: " + node.id());
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Error while fetching segment sizes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
