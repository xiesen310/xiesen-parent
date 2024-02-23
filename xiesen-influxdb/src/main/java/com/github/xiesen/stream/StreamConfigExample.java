package com.github.xiesen.stream;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.typesafe.config.waterdrop.Config;
import com.typesafe.config.waterdrop.ConfigFactory;
import com.typesafe.config.waterdrop.ConfigList;
import com.typesafe.config.waterdrop.ConfigValue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamConfigExample {
    public static final String CHECK_INTERFACE_URL = "http://192.168.70.5:9002/api/jobs/check";

    public static void main(String[] args) {
//        parseTest();
        String filePath = "/Users/xiesen/Desktop/log2es.conf";
        try {
//            String content = readLocalConfFile(filePath);
            List<String> list = JdbcReadUtil.searchStreamConf();
            for (String content : list) {
                Config config = ConfigFactory.parseString(content);
//                System.out.println("原始 config: " + config);
                Map<String, Object> map = StreamConfigUtil.config2Map(config);
                Map<String, Object> jobEnv = (Map<String, Object>) map.get("job.env");
                jobEnv.put("job.name", jobEnv.get("job.name").toString() + "-update");
                Config newConfig = ConfigFactory.parseMap(map);

//                System.out.println("新 config: " + newConfig);
//                System.out.println("==========================");
                String configString = StreamConfigUtil.toConfigString(newConfig);
                StreamConfigUtil.check(configString, CHECK_INTERFACE_URL);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取本地 streamx 配置文件
     *
     * @param filePath 配置文件路径
     * @return String
     * @throws IOException
     */
    private static String readLocalConfFile(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String content = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("原始配置文件:");
        System.out.println(content);
        return content;
    }


    /**
     * 测试解析
     */
    @Deprecated
    private static void parseTest() {
        String filePath = "/Users/xiesen/Desktop/log2es.conf";
        try {
            String content = readLocalConfFile(filePath);

            Config config = ConfigFactory.parseString(content);
//            Map<String, Object> map = config2Map(config);

            System.out.println("配置文件解析如下:");
            // job env 解析
            Config jobEnv = config.getConfig("job.env");
            String jobName = jobEnv.getString("job.name");
            System.out.println("jobName = " + jobName);

            jobEnv.atKey("job.name");
            ConfigValue value1 = jobEnv.getValue("job.name");

//            ConfigOrigin origin = SimpleConfigOrigin()
//            ConfigValue configValue = new SimpleConfigOrigin()
            jobEnv = jobEnv.withValue("job.name2", value1);
            System.out.println(jobEnv.getString("job.name2"));

            System.out.println(value1);
            System.out.println(jobEnv);


            // source 解析
            List<? extends Config> sourceEnv = config.getConfigList("source");
            for (Config conf : sourceEnv) {
                String pluginName = conf.getString("plugin_name");
                System.out.println("source 插件名称：" + pluginName);
                if ("ZorkDataKafkaStream".equalsIgnoreCase(pluginName)) {
                    String kafkaBroker = conf.getString("consumer.bootstrap.servers");
                    String topics = conf.getString("topics");
                    Console.log("kafka source 插件: kafkaBroker = {}; topics = {};", kafkaBroker, topics);
                }

            }

            // sink 解析
            List<? extends Config> sinkEnv = config.getConfigList("sink");
            for (Config conf : sinkEnv) {
                String pluginName = conf.getString("plugin_name");
                System.out.println("sink 插件名称：" + pluginName);
                if ("ZorkDataElasticsearch6".equalsIgnoreCase(pluginName)) {
                    String esClusterName = conf.getString("es.clusterName");
                    ConfigList hosts = conf.getList("hosts");
                    List hostList = new ArrayList();
                    for (ConfigValue value : hosts) {
                        hostList.add(value.render());
                    }
                    String mqAddress = conf.getString("mq.address");

                    Console.log("es sink 插件: esClusterName = {}; hosts = {}; mqAddress = {};", esClusterName, hostList.toString(), mqAddress);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
