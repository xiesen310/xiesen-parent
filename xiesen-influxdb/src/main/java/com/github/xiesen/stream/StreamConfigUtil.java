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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamConfigUtil {
    /**
     * 将 streamx 配置文件对象{@Config config}转换成 map 结构
     *
     * @param config {@Config config}
     * @return map
     */
    public static Map<String, Object> config2Map(Config config) {
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
    private static String unescapeJavaString(String str) {
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

    /**
     * 将配置对象转换成配置文件字符串
     *
     * @param config config
     * @return String
     */
    public static String toConfigString(Config config) {
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
     * 校验配置文件
     *
     * @param conf 配置文件字符串
     * @return Boolean
     */
    public static Boolean check(String url, String conf) {
        HttpResponse response = null;
        Boolean result = false;
        try {
            response = HttpUtil.createPost(url)
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
            if (null != response) {
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

    public static final String CHECK_INTERFACE_URL = "http://192.168.70.5:9002/api/jobs/check";
    public static final String LOG2ES_CONFIG = "job.env{\n" +
            "    # 任务名称\n" +
            "    job.name = \"log2es_dwd_default_log\"\n" +
            "    # 任务并行度\n" +
            "    execution.parallelism = 1\n" +
            "}\n" +
            "\n" +
            "ddl {\n" +
            "    define = \"\"\n" +
            "}\n" +
            "\n" +
            "\n" +
            "source {\n" +
            "     ZorkDataKafkaStream {\n" +
            "        # afka消费数据类型,一般分为3种：json、logavro、metricavro,日志入库使用logavro\n" +
            "        data.type = \"logavro\"\n" +
            "        # kakfa消费的topic，可用‘,’隔开同时消费多个topic\n" +
            "        topics = \"dwd_default_log\"\n" +
            "        # kafka集群地址，形如：yf172:9092,yf171:9092,yf170:9092 多个节点使用‘,’隔开\n" +
            "        consumer.bootstrap.servers = \"kafka1:9092\"\n" +
            "        # 消费topics的groupId地址\n" +
            "        consumer.group.id = \"dwd_default_log\"\n" +
            "        # kafka消费模式-->> latest:最新开始消费 earliest:从头开始消费 none: 默认消费模式\n" +
            "        offset.reset=\"none\"\n" +
            "        # kakfa 安全认证类型,支持类型(kerberos,sasl,none),这里一般默认none即可,可在streamx客户端配置Kafka认证全局生效\n" +
            "        kafka.security.model=\"none\"\n" +
            "        # 结果表名称\n" +
            "        result_table_name = \"ZorkDataKafkaStreamSource\"\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "transform {\n" +
            "}\n" +
            "\n" +
            "sink {\n" +
            "    ZorkDataElasticsearch6 {\n" +
            "        # es 地址,使用list 数组接收参数,多个地址之间使用\",\"进行分割\n" +
            "        hosts = [\"elasticsearch:9200\"]\n" +
            "        # es 的版本 如：5、6、7 不需要小版本号\n" +
            "        es.version = \"6\"\n" +
            "        # es 集群名称\n" +
            "        es.clusterName = \"docker-cluster\"\n" +
            "        # 模块是否取set isSet = true 表示是国信; servicecode 和 appprogramname 是一致的 isSet = false 表示是国泰; appprogramname是模块\n" +
            "        isSet = \"false\"\n" +
            "        # es插件名称\n" +
            "        result_table_name = \"ZorkDataElasticsearch6\"\n" +
            "        # 入库index生成方式，目前分为两种方式:\n" +
            "        # 1. index前缀+\"_\"+logTypeName+\"_\"+日期，type为logTypeName ,这种方式是 es6 默认支持的方式，在这里设置的入库方式是 old\n" +
            "        # 2. index前缀+\"_\"+日期 ，type为doc，这种入库方式是国信特有的入库方式，属于定制化的操作，在这里设置的入库方式是 new\n" +
            "        storageWay=\"new\"\n" +
            "        # es 数据映射关系url 地址\n" +
            "        data.relation.url = \"http://192.168.70.92:9999/api/onlyone/smartdata/restful/v1/dataRelationShip/findByDataTypeAndStatus?status=1&dataType=1\"\n" +
            "        # rabbitmq 地址\n" +
            "        mq.address = \"paas-rabbitmq:5672\"\n" +
            "        # rabbitmq 用户名\n" +
            "        mq.username = \"admin\"\n" +
            "        # rabbitmq 密码\n" +
            "        mq.password = \"admin\"\n" +
            "        # rabbitmq 数据接入关系通知队列\n" +
            "        mq.queue.fanout = \"smartdata.datarelationship.e.q.f\"\n" +
            "    }\n" +
            "}";

    public static void main(String[] args) {
        Config config = ConfigFactory.parseString(LOG2ES_CONFIG);
        Map<String, Object> map = StreamConfigUtil.config2Map(config);
        Map<String, Object> jobEnv = (Map<String, Object>) map.get("job.env");
        jobEnv.put("job.name", jobEnv.get("job.name").toString() + "-update");
        Config newConfig = ConfigFactory.parseMap(map);
        String configString = StreamConfigUtil.toConfigString(newConfig);
        System.out.println(configString);
        Boolean check = check(CHECK_INTERFACE_URL, configString);
        if (check) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }
}
