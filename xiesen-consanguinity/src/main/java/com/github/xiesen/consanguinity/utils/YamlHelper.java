package com.github.xiesen.consanguinity.utils;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.consanguinity.model.FileBeatModel;
import com.github.xiesen.consanguinity.model.FilebeatInputType;
import com.github.xiesen.consanguinity.model.KafkaModel;
import com.github.xiesen.consanguinity.model.LogstashModel;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xiesen
 */
public class YamlHelper {
    public static void main(String[] args)  {
        final FileBeatModel beatModel = parseFilebeatConf();
//        final BeatModel beatModel = parseMetricConf();
//        final BeatModel beatModel = parseExecbeatConf();

        System.out.println(JSON.toJSONString(beatModel));
    }

    @SuppressWarnings("all")
    public static Map<String, Object> readYaml(String name) {
        Yaml yaml = new Yaml();
        InputStream resourceAsStream = null;
        Map<String, Object> map = null;
        try {
            resourceAsStream = ClassLoader.getSystemResourceAsStream(name);
            map = yaml.loadAs(resourceAsStream, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != resourceAsStream) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    @SuppressWarnings("all")
    public static FileBeatModel parseFilebeatConf() {
        final Map<String, Object> map = readYaml("filebeat.yml");
        final Map<String, Object> logstashMap = (Map<String, Object>) map.get("output.logstash");
        final Boolean enabled = (Boolean) logstashMap.get("enabled");
        final List<String> hosts = (List<String>) logstashMap.get("hosts");
        final Boolean loadbalance = (Boolean) logstashMap.get("loadbalance");
        final String name = map.get("name").toString();
        final List<Map<String, Object>> filebeatList = (List<Map<String, Object>>) map.get("filebeat.inputs");
        List<String> topicList = new ArrayList<>(filebeatList.size());
        List<String> ipList = new ArrayList<>(filebeatList.size());
        List<FilebeatInputType> filebeatTypeInstanceList = new ArrayList<>(filebeatList.size());
        for (Map<String, Object> o : filebeatList) {
            final Map<String, Object> fields = (Map<String, Object>) o.get("fields");
            final String topicName = fields.get("topicname").toString();
            final String ip = fields.get("ip").toString();
            final List<String> paths = (List<String>) o.get("paths");
            final String encoding = o.getOrDefault("encoding", "utf-8").toString();
            final String type = o.getOrDefault("type", "log").toString();
            final String broker = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
            final FilebeatInputType filebeatTypeInstance = FilebeatInputType.builder().fields(fields).encoding(encoding).type(type)
                    .paths(paths).ip(ip).topicName(topicName).broker(broker).build();
            filebeatTypeInstanceList.add(filebeatTypeInstance);
            topicList.add(topicName);
            ipList.add(ip);
        }


        final LogstashModel logstash1 = LogstashModel.builder().hosts(hosts).enabled(enabled).loadBalance(loadbalance).build();
        final FileBeatModel beatModel = FileBeatModel.builder().name(name).type("filebeat").logstash(logstash1)
                .filebeatTypeInstances(filebeatTypeInstanceList).build();
        return beatModel;
    }

    @SuppressWarnings("all")
    public static FileBeatModel parseMetricbeatConf() {
        final Map<String, Object> map = readYaml("metricbeat.yml");
        final Map<String, Object> logstashMap = (Map<String, Object>) map.get("output.logstash");
        final Boolean enabled = (Boolean) logstashMap.get("enabled");
        final List<String> hosts = (List<String>) logstashMap.get("hosts");
        final Boolean loadbalance = (Boolean) logstashMap.get("loadbalance");

        final List<Map<String, Object>> metricList = (List<Map<String, Object>>) map.get("metricbeat.modules");
        List<String> topicList = new ArrayList<>(metricList.size());
        List<String> ipList = new ArrayList<>(metricList.size());
        for (Map<String, Object> o : metricList) {
            final Map<String, Object> fields = (Map<String, Object>) o.get("fields");
            final String topicName = fields.get("topicname").toString();
            final String ip = fields.get("ip").toString();
            topicList.add(topicName);
            ipList.add(ip);
        }

        final KafkaModel kafkaModel = KafkaModel.builder().broker("kafka-1:19092,kafka-2:19092,kafka-3:19092").topic("").build();
        final LogstashModel logstash1 = LogstashModel.builder().hosts(hosts).enabled(enabled).loadBalance(loadbalance).build();
        final FileBeatModel beatModel = FileBeatModel.builder().type("filebeat").logstash(logstash1).build();
        return beatModel;
    }

    @SuppressWarnings("all")
    public static FileBeatModel parseExecbeatConf() {
        final Map<String, Object> map = readYaml("execbeat.yml");
        final Map<String, Object> logstashMap = (Map<String, Object>) map.get("output.logstash");
        final Boolean enabled = (Boolean) logstashMap.get("enabled");
        final List<String> hosts = (List<String>) logstashMap.get("hosts");
        final Boolean loadbalance = (Boolean) logstashMap.get("loadbalance");

        final Map<String, Object> metricList = (Map<String, Object>) map.get("fields");

        final String topicName = metricList.get("topicname").toString();
        final String ip = metricList.get("ip").toString();
        List<String> topicList = Arrays.asList(topicName);
        List<String> ipList = Arrays.asList(ip);

        final KafkaModel kafkaModel = KafkaModel.builder().broker("kafka-1:19092,kafka-2:19092,kafka-3:19092").topic("").build();
        final LogstashModel logstash1 = LogstashModel.builder().hosts(hosts).enabled(enabled).loadBalance(loadbalance).build();
        final FileBeatModel beatModel = FileBeatModel.builder().type("filebeat").logstash(logstash1).build();
        return beatModel;
    }

}
