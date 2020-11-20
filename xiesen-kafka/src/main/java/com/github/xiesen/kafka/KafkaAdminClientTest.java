package com.github.xiesen.kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author 谢森
 * @Description KafkaAdminClientTest
 * @Email xiesen310@163.com
 * @Date 2020/11/19 15:54
 */
public class KafkaAdminClientTest {
    public static void main(String[] args) throws Exception {
        AdminClient client = getKafkaAdminClient();

//        listTopicsWithOptions(client);

//        listTopics(client);
//        listTopicsWithList(client);

//        descConfigTopics(client, listTopics(client));

//        createTopic(Arrays.asList("xiesen_test_002"), client);

//        deleteTopic(Arrays.asList("xiesen_test_002"), client);
//        createTopic(Arrays.asList("xiesen_test_002"), client);
        descConfigTopics(client, Arrays.asList("xiesen_test_002"));
        updateTopicConfig(Arrays.asList("xiesen_test_002"), client);
        descConfigTopics(client, Arrays.asList("xiesen_test_002"));
        client.close();
    }

    /**
     * 更新 topic 配置信息
     * delete.retention.ms 删除策略
     *
     * @param topicNames
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void updateTopicConfig(List<String> topicNames, AdminClient client) throws ExecutionException,
            InterruptedException {
        List<ConfigResource> configResources = new ArrayList<>();

        topicNames.forEach(topicName -> configResources.add(new ConfigResource(ConfigResource.Type.TOPIC, topicName)));

        // 建立修改的配置项,配置项以ConfigEntry形式存在
        Config config = new Config(Collections.singletonList(new ConfigEntry("delete.retention.ms", "3600000")));


        // 参数构造
        Map<ConfigResource, Config> configMap = new HashMap<>();
        configResources.forEach(configResource -> configMap.put(configResource, config));

        AlterConfigsResult result = client.alterConfigs(configMap);
        result.all().get();


    }


    /**
     * 创建 topic
     *
     * @param topicNames
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void createTopic(List<String> topicNames, AdminClient client) throws ExecutionException,
            InterruptedException {
        List<NewTopic> topicList = new ArrayList<>();
        topicNames.forEach(topicName -> {
            topicList.add(new NewTopic(topicName, 1, Short.parseShort("1")));
        });

        CreateTopicsResult result = client.createTopics(topicList);
        result.all().get();
        result.values().forEach((name, future) -> System.out.println("create topicName [ " + name + " ] success"));
    }


    /**
     * 删除 topic
     *
     * @param topicNames
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void deleteTopic(List<String> topicNames, AdminClient client) throws ExecutionException,
            InterruptedException {
        DeleteTopicsResult result = client.deleteTopics(topicNames);
        result.all().get();
        result.values().forEach((name, future) -> System.out.println("delete topicName: [" + name + " ] success"));
    }

    /**
     * topic 描述信息
     *
     * @param client
     * @param topicNames
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void descConfigTopics(AdminClient client, List<String> topicNames) throws ExecutionException,
            InterruptedException {
        List<ConfigResource> configResources = new ArrayList<>(64);
        topicNames.forEach(topicName -> configResources.add(
                // 指定要获取的源
                new ConfigResource(ConfigResource.Type.TOPIC, topicName)));

        DescribeConfigsResult result = client.describeConfigs(configResources);
        Map<ConfigResource, Config> resourceConfigMap = result.all().get();

        Map<String, Object> map = new HashMap<>(64);

        resourceConfigMap.forEach((configResource, config) -> {
            if (configResource.type().name().equals("TOPIC")) {
                String topicName = configResource.name();
                Collection<ConfigEntry> entries = config.entries();
                JSONObject jsonObject = new JSONObject();
                entries.forEach(configEntry -> {
                    String name = configEntry.name();
                    String value = configEntry.value();
                    jsonObject.put(name, value);
                });
                map.put(topicName, jsonObject);
            }
        });

        map.forEach((k, v) -> {
            System.out.println(k + " : " + v.toString());
        });
    }

    /**
     * 获取 topic 列表
     *
     * @param client
     * @throws InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    private static Set<String> listTopics(AdminClient client) throws InterruptedException,
            java.util.concurrent.ExecutionException {
        ListTopicsResult listTopicsResult = client.listTopics();
        Set<String> topicNames = listTopicsResult.names().get();

        topicNames.forEach(System.out::println);
        return topicNames;
    }


    private static void listTopicsWithList(AdminClient client) throws InterruptedException,
            java.util.concurrent.ExecutionException {
        ListTopicsResult listTopicsResult = client.listTopics();
        Collection<TopicListing> topicList = listTopicsResult.listings().get();
        topicList.forEach(System.out::println);
    }

    /**
     * 获取 kafka topic 列表(包含 kafka 内部的 topic)
     * 如: __consumer_offsets，internal=true
     *
     * @param client
     * @throws InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    private static void listTopicsWithOptions(AdminClient client) throws InterruptedException,
            java.util.concurrent.ExecutionException {
        ListTopicsOptions options = new ListTopicsOptions();
        // 列出内部的Topic
        options.listInternal(true);
        ListTopicsResult listTopicsResult = client.listTopics(options);
        // 获取所有topic的名字，返回的是一个Set集合
        Set<String> topicNames = listTopicsResult.names().get();

        topicNames.forEach(System.out::println);
    }

    /**
     * 获取 kafka admin client
     *
     * @return AdminClient
     */
    private static AdminClient getKafkaAdminClient() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "kafka-1:19092,kafka-2:19092,kafka-3:19092");
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);
        return KafkaAdminClient.create(properties);
    }


}
