package com.github.xiesen.stream;

import com.typesafe.config.waterdrop.Config;
import com.typesafe.config.waterdrop.ConfigFactory;
import com.typesafe.config.waterdrop.ConfigOrigin;
import com.typesafe.config.waterdrop.ConfigValue;

import java.util.*;

public class StreamConfigExample2 {


    public static void main(String[] args) {
        Map<String, Object> bigMap = new HashMap<>();
        Map<String, Object> jobEnv = new HashMap<>();
        jobEnv.put("job.name", "log2es_dwd_default_log");
        jobEnv.put("execution.parallelism", 1);
        bigMap.put("job.env", jobEnv);

        Map<String, Object> ddl = new HashMap<>();
        ddl.put("define", "");
        bigMap.put("ddl", ddl);

        List<Map<String, Object>> sourceList = new ArrayList<>();
        Map<String, Object> zorkDataKafkaStream = new HashMap<>();
        zorkDataKafkaStream.put("data.type", "logavro");
        zorkDataKafkaStream.put("topics", "dwd_default_log");
        zorkDataKafkaStream.put("consumer.bootstrap.servers", "kafka1:9092");
        sourceList.add(zorkDataKafkaStream);
        bigMap.put("source", sourceList);

        List<Map<String, Object>> transformList = new ArrayList<>();
        bigMap.put("transform", transformList);

        List<Map<String, Object>> sinkList = new ArrayList<>();
        Map<String, Object> zorkDataElasticsearch6 = new HashMap<>();
        zorkDataElasticsearch6.put("hosts", Arrays.asList("elasticsearch:9200"));
        zorkDataElasticsearch6.put("es.version", "6");
        zorkDataElasticsearch6.put("es.clusterName", "docker-cluster");
        sinkList.add(zorkDataElasticsearch6);
        bigMap.put("sink", sinkList);

        Config config = ConfigFactory.parseMap(bigMap);
        Config jobEnvConfig = config.getConfig("job.env");
        ConfigValue value = jobEnvConfig.getValue("job.name");
        ConfigOrigin origin1 = value.origin().withComments(Arrays.asList("任务名称"));
        ConfigValue configValue = value.withOrigin(origin1);
        Config config1 = jobEnvConfig.withValue("job.name", configValue);
        Config config2 = config.withValue("job.env", config1.root());

        System.out.println(config2);
    }
}
