package com.github.xiesen.mock.test;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class KafkaClusterVersion {
    public static void main(String[] args) {
        Properties props = new Properties();
        // 替换为你的Kafka broker地址
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.70.6:9092");

        Map<Integer, Node> nodeMap = new HashMap<>();
        try (AdminClient adminClient = AdminClient.create(props)) {
            // 获取集群中的所有broker
            Collection<Node> nodes = adminClient.describeCluster().nodes().get();
            Node controller = adminClient.describeCluster().controller().get();
            System.out.println("Active Controller: " + controller);

            // 查询每个broker的配置
            Collection<ConfigResource> configsToDescribe = new ArrayList<>();
            for (Node node : nodes) {
                ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, String.valueOf(node.id()));
                configsToDescribe.add(configResource);
                nodeMap.put(node.id(), node);
            }

            // 查询集群配置信息
            DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(configsToDescribe);

            // 获取配置结果
            for (Map.Entry<ConfigResource, Config> entry : describeConfigsResult.all().get().entrySet()) {
                ConfigResource resource = entry.getKey();
                Config config = entry.getValue();
                for (ConfigEntry configEntry : config.entries()) {
                    if ("log.dirs".equals(configEntry.name())) {
                        System.out.println("Broker ID: " + resource.name() + " Log Directory: " + configEntry.value());
                    }

                    if ("log.retention.hours".equals(configEntry.name())) {
                        System.out.println("Broker ID: " + resource.name() + " Log Retention Hours: " + configEntry.value());
                    }

                    if ("message.max.bytes".equals(configEntry.name())) {
                        System.out.println("Broker ID: " + resource.name() + " Message Max Bytes: " + configEntry.value());
                    }
                    if ("log.segment.bytes".equals(configEntry.name())) {
                        System.out.println("Broker ID: " + resource.name() + " Log Segment Bytes: " + configEntry.value());
                    }

                    if ("inter.broker.protocol.version".equals(configEntry.name())) {
                        String name = resource.name();
                        if (nodeMap.containsKey(Integer.parseInt(name))) {
                            Node node = nodeMap.get(Integer.parseInt(name));
                            boolean isActiveController = node.id() == controller.id();
                            System.out.println("Broker ID: " + node.id() + " Inter-broker Protocol Version: " + configEntry.value() + " Host: " + node.host() + " Port: " + node.port() + " Active Controller: " + isActiveController);
                        }
                    }
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
