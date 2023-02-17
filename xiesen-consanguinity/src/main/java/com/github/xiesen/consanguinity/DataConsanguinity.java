package com.github.xiesen.consanguinity;

import com.github.xiesen.consanguinity.model.FileBeatModel;
import com.github.xiesen.consanguinity.model.FilebeatInputType;
import com.github.xiesen.consanguinity.model.KafkaModel;
import com.github.xiesen.consanguinity.model.LogstashModel;
import com.github.xiesen.consanguinity.utils.Neo4jHelper;
import com.github.xiesen.consanguinity.utils.YamlHelper;
import org.neo4j.driver.Session;

import java.util.*;

import static org.neo4j.driver.Values.parameters;

/**
 * @author xiesen
 */
public class DataConsanguinity {
    public static final String NEO4J_URI = "bolt://192.168.90.24:27687";
    public static final String NEO4J_USERNAME = "neo4j";
    public static final String NEO4J_PASSWORD = "zorkdata.com";

    public static void main(String[] args) {
        final Neo4jHelper neo4jHelper = new Neo4jHelper(NEO4J_URI, NEO4J_USERNAME, NEO4J_PASSWORD);
        final Session session = neo4jHelper.getNeo4jSession();
        final FileBeatModel beatModel = YamlHelper.parseFilebeatConf();

        final List<FilebeatInputType> filebeatTypeInstances = beatModel.getFilebeatTypeInstances();
        List<String> beatNodeNames = new ArrayList<>(filebeatTypeInstances.size());
        Map<String, String> kafkaMap = new HashMap<>(filebeatTypeInstances.size());

        // 创建 filebeat 节点
        for (FilebeatInputType filebeatInputType : filebeatTypeInstances) {
            final Map<String, Object> fields = filebeatInputType.getFields();
            final String s = fieldsToNeo4jAttribute(fields);
            String nodeName = "node_" + filebeatInputType.getIp().replaceAll("\\.", "_");
            beatNodeNames.add(nodeName);
            kafkaMap.put(filebeatInputType.getTopicName(), filebeatInputType.getBroker());
            String filebeatSql = "CREATE (" + nodeName + ":filebeat {" + s + ",nodeName:'" + nodeName + "'})";
            session.run(filebeatSql);
        }

        final LogstashModel logstash = beatModel.getLogstash();
        final List<String> hosts = logstash.getHosts();
        final boolean enabled = logstash.isEnabled();
        final boolean loadBalance = logstash.isLoadBalance();
        List<String> logstashNodeNames = new ArrayList<>(hosts.size());

        // 创建 logstash 节点
        for (String host : hosts) {
            String nodeName = "node_" + host.replaceAll("\\.", "_").replaceAll(":", "_");
            logstashNodeNames.add(nodeName);
            String logstashSql = "CREATE (" + nodeName + ":logstash {enabled:$enabled,loadBalance:$loadBalance,host:$host,nodeName:$nodeName})";
            session.run(logstashSql, parameters("enabled", enabled, "loadBalance", loadBalance, "host", host, "nodeName", nodeName));
        }

        // 创建 kafka 节点
        kafkaMap.forEach((topic, broker) -> {
            String kafkaSql = "CREATE (kafka_" + topic + ":kafka {topic:$topic,broker:$broker})";
            session.run(kafkaSql, parameters("topic", topic, "broker", broker));
        });


        // 创建关系
        for (String logstashNodeName : logstashNodeNames) {
            // 创建 logstash 与 kafka 之间的关系
            kafkaMap.forEach((topic, broker) -> {
                String kafkaRelationship = "MATCH (" + logstashNodeName + ":logstash),(" + topic + ":kafka) where " + logstashNodeName + ".nodeName = '" + logstashNodeName + "' AND " + topic + ".topic = '" + topic + "' CREATE (" + logstashNodeName + ")-[:原始数据上报kafka]->(" + topic + ") RETURN " + logstashNodeName + "," + topic;
                session.run(kafkaRelationship);
            });

            // 创建filebeat 与 kafka 之间的关系
            for (String beatNodeName : beatNodeNames) {
                String sql = "MATCH(" + beatNodeName + ":filebeat),(" + logstashNodeName + ":logstash) where " + beatNodeName + ".nodeName = '" + beatNodeName + "' AND " + logstashNodeName + ".nodeName = '" + logstashNodeName + "' CREATE (" + beatNodeName + ")-[:filebeat数据采集]->(" + logstashNodeName + ") RETURN " + beatNodeName + "," + logstashNodeName;
                session.run(sql);
            }
        }

        neo4jHelper.closeAll();
    }

    public static String fieldsToNeo4jAttribute(Map<String, Object> fields) {
        StringBuilder builder = new StringBuilder();
        fields.forEach((k, v) -> builder.append(k).append(": '").append(v.toString()).append("'").append(","));
        final String s = builder.toString();
        return s.substring(0, s.length() - 1);
    }
}
