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

    /**
     * 将 streamx 配置文件对象{@Config config}转换成 map 结构
     *
     * @param config {@Config config}
     * @return map
     */
    private static Map<String, Object> config2Map(Config config) {
        Map<String, Object> bigMap = new HashMap<>(5);
        Config jobEnv = config.getConfig("job.env");
        Map<String, Object> jobEnvMap = pluginConfObject2Map(jobEnv);
        bigMap.put("job.env", jobEnvMap);

        Config ddl = config.getConfig("ddl");
        Map<String, Object> ddlMap = pluginConfObject2Map(ddl);
        bigMap.put("ddl", ddlMap);

        List<Map<String, Object>> sourceList = new ArrayList<>();
        List<? extends Config> sourceEnv = config.getConfigList("source");
        for (Config conf : sourceEnv) {
            Map<String, Object> stringObjectMap = pluginConfObject2Map(conf);
            sourceList.add(stringObjectMap);
        }
        bigMap.put("source", sourceList);

        List<Map<String, Object>> sinkList = new ArrayList<>();
        List<? extends Config> sinkEnv = config.getConfigList("sink");
        for (Config conf : sinkEnv) {
            Map<String, Object> stringObjectMap = pluginConfObject2Map(conf);
            sinkList.add(stringObjectMap);
        }
        bigMap.put("sink", sinkList);

        List<Map<String, Object>> transformList = new ArrayList<>();
        List<? extends Config> transformEnv = config.getConfigList("transform");
        for (Config conf : transformEnv) {
            Map<String, Object> stringObjectMap = pluginConfObject2Map(conf);
            transformList.add(stringObjectMap);
        }
        bigMap.put("transform", transformList);

        return bigMap;
    }

    /**
     * 去除转译字符
     *
     * @param str 字符串
     * @return 去除转译后的字符串
     */
    public static String unescapeJavaString(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\\' && i + 1 < str.length()) {
                char nextChar = str.charAt(i + 1);
                switch (nextChar) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    default:
                        sb.append(nextChar);
                        break;
                }
                i++;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 插件配置对象转成 map
     *
     * @param config 插件配置对象
     * @return map
     */
    private static Map<String, Object> pluginConfObject2Map(Config config) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, ConfigValue> valueEntry : config.entrySet()) {
            String key = valueEntry.getKey();
            String valueType = config.getValue(key).valueType().name();
            if ("LIST".equalsIgnoreCase(valueType)) {
                List hostList = new ArrayList();
                ConfigList configList = config.getList(key);
                for (ConfigValue v : configList) {
                    hostList.add(v.render().replaceAll("^\"|\"$", ""));
                }
                map.put(key, hostList);
            } else {
                ConfigValue value = config.getValue(key);
                String render = value.render();
                String v = render.replaceAll("^\"|\"$", "");
                map.put(key, unescapeJavaString(v));
            }
        }
        return map;
    }

    public static void main(String[] args) {
//        parseTest();
        String filePath = "/Users/xiesen/Desktop/log2es.conf";
        try {
//            String content = readLocalConfFile(filePath);
            List<String> list = JdbcReadUtil.searchStreamConf();
            for (String content : list) {
                Config config = ConfigFactory.parseString(content);
//                System.out.println("原始 config: " + config);
                Map<String, Object> map = config2Map(config);
                Map<String, Object> jobEnv = (Map<String, Object>) map.get("job.env");
                jobEnv.put("job.name", jobEnv.get("job.name").toString() + "-update");
                Config newConfig = ConfigFactory.parseMap(map);

//                System.out.println("新 config: " + newConfig);
//                System.out.println("==========================");
                String configString = toConfigString(newConfig);
                check(configString);
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
     * 校验配置文件
     *
     * @param conf 配置文件字符串
     * @return Boolean
     */
    public static Boolean check(String conf) {
        HttpResponse response = null;
        Boolean result = false;
        try {
            response = HttpUtil.createPost(CHECK_INTERFACE_URL)
                    .header("Content-Type", "text/plain").body(conf).execute();
            String body = response.body();
            if (response.isOk()) {
                JSONObject jsonObject = JSON.parseObject(body);
                JSONObject dataJsonObject = JSON.parseObject(jsonObject.get("data").toString());
                Boolean success = dataJsonObject.getBoolean("success");
                result = success;
                String message = dataJsonObject.getString("message");
//                System.out.println(message);
            }
        } catch (Exception e) {
            if (null != response && !response.body().contains("register UDF exception")) {
                Console.log("校验失败: {}, 配置文件: {}", response.body(), conf);
            }
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return result;
    }

    /**
     * 将配置对象转换成配置文件字符串
     *
     * @param config config
     * @return String
     */
    private static String toConfigString(Config config) {
        StringBuilder builder = new StringBuilder();
        Config jobEnv = config.getConfig("job.env");
        builder.append("job.env {").append("\r\n");
        for (Map.Entry<String, ConfigValue> valueEntry : jobEnv.entrySet()) {
            String key = valueEntry.getKey();
            String v = jobEnv.getValue(key).render();
            builder.append(key).append(" = ").append(v).append("\r\n");
        }
        builder.append("}").append("\r\n");

        builder.append("source {").append("\r\n");
        List<? extends Config> source = config.getConfigList("source");
        for (Config conf : source) {
            builder.append(printPluginConf(conf));
        }
        builder.append("}").append("\r\n");

        Config ddlEnv = config.getConfig("ddl");
        builder.append("ddl {").append("\r\n");
        for (Map.Entry<String, ConfigValue> valueEntry : ddlEnv.entrySet()) {
            String key = valueEntry.getKey();
            String v = ddlEnv.getValue(key).render();
            builder.append(key).append(" = ").append(v).append("\r\n");
        }
        builder.append("}").append("\r\n");

        builder.append("transform {").append("\r\n");
        List<? extends Config> transform = config.getConfigList("transform");
        for (Config conf : transform) {
            builder.append(printPluginConf(conf));
        }
        builder.append("}").append("\r\n");

        builder.append("sink {").append("\r\n");
        List<? extends Config> sink = config.getConfigList("sink");
        for (Config conf : sink) {
            builder.append(printPluginConf(conf));
        }
        builder.append("}").append("\r\n");

//        System.out.println(builder.toString());
        return builder.toString();
    }

    /**
     * 拼接插件信息
     *
     * @param config 配置对象
     * @return String
     */
    private static String printPluginConf(Config config) {
        StringBuilder builder = new StringBuilder();
        String pluginName = config.getString("plugin_name");
        builder.append(pluginName).append("  { \r\n");
        for (Map.Entry<String, ConfigValue> valueEntry : config.entrySet()) {
            String key = valueEntry.getKey();
            String valueType = config.getValue(key).valueType().name();

            if ("LIST".equalsIgnoreCase(valueType)) {
                List<String> hostList = new ArrayList();
                ConfigList configList = config.getList(key);
                for (ConfigValue v : configList) {
                    hostList.add(v.render());
                }
                StringBuilder builderList = new StringBuilder();
                builderList.append("[");
                for (String o : hostList) {
                    builderList.append(o).append(",");
                }
                String substring = builderList.toString().substring(0, builderList.toString().length() - 1) + "]";
                builder.append(key).append(" = ").append(substring).append("\r\n");
            } else {
                ConfigValue value = config.getValue(key);
                String render = value.render();
                String v = render;
                builder.append(key).append(" = ").append(v).append("\r\n");
            }
        }
        builder.append("}").append("\r\n");
        return builder.toString();
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
