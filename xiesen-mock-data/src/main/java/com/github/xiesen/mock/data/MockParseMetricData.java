package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @Description 模拟解析格式数据
 * @Email xiesen310@163.com
 * @Date 2020/12/14 13:21
 */
public class MockParseMetricData {

    /**
     * kafka producer
     *
     * @param bootstrapServers
     * @param serializerClassName
     * @param <T>
     * @return
     */
    private static <T> KafkaProducer<String, T> buildProducer(String bootstrapServers, String serializerClassName) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", "all");
        props.put("retries", 5);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", serializerClassName);
        props.put("batch.size", 16384);
        props.put("linger.ms", 0);
        props.put("buffer.memory", 33554432);

        // kerberos 认证
        /*System.setProperty("java.security.krb5.conf", "D:\\tmp\\kerberos\\krb5.conf");
        System.setProperty("java.security.auth.login.config", "D:\\tmp\\kerberos\\kafka_server_jaas.conf");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");*/

        // sasl 认证
        /*props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");
        Configuration.setConfiguration(new SaslConfig("admin", "admin"));*/


        return new KafkaProducer<>(props);
    }

    public static String buildMsg() {
        JSONObject bigJson = new JSONObject();
        JSONObject hostJson = new JSONObject();
        hostJson.put("name", "yf122");
        bigJson.put("host", hostJson);
        bigJson.put("topicname", "ods_all_metric");
        bigJson.put("clustername", "基础监控");
        bigJson.put("servicecode", "lmt模块");
        bigJson.put("ip", "192.168.70.122");

        JSONObject systemObject = new JSONObject();
        JSONObject coreObject = new JSONObject();
        coreObject.put("user_pct", 1.1);
        coreObject.put("irq", new JSONObject().put("pct", 0));
        coreObject.put("softirq", new JSONObject().put("pct", 0.0517));
        coreObject.put("system", new JSONObject().put("pct", 0.0176));
        coreObject.put("id", 0);
        coreObject.put("iowait", new JSONObject().put("pct", 0));
        coreObject.put("nice", new JSONObject().put("pct", 0));
        coreObject.put("steal", new JSONObject().put("pct", 0));
        coreObject.put("idle", new JSONObject().put("pct", 0.9065));
        coreObject.put("user", new JSONObject().put("pct", 0.0242));

        systemObject.put("core", coreObject);
        bigJson.put("system", systemObject);

        bigJson.put("appprogramname", "lmt模块");

        JSONObject eventObject = new JSONObject();
        eventObject.put("duration", 211618);
        eventObject.put("dataset", "system.core");
        eventObject.put("module", "system");

        bigJson.put("event", eventObject);
        bigJson.put("@version", "1");

        JSONObject agentObject = new JSONObject();
        agentObject.put("hostname", "yf122");
        agentObject.put("version", "7.4.0");
        agentObject.put("ephemeral_id", "b324b0f9-cac1-40b9-bc48-7bb3789cea2e");
        agentObject.put("id", "0f81f6b9-fa64-485f-8a3e-dfd3e87f5946");
        agentObject.put("type", "metricbeat");

        bigJson.put("agent", agentObject);
        JSONArray tagsArr = new JSONArray();
        tagsArr.add("beats_input_raw_event");
        bigJson.put("tags", tagsArr);

        bigJson.put("servicename", "lmt模块");
        bigJson.put("collecttime", DateUtil.getUTCTimeStr());
        bigJson.put("appsystem", "dev_test");
        bigJson.put("collectruleid", 1);


        JSONObject ecsObject = new JSONObject();
        ecsObject.put("version", "1.1.0");
        bigJson.put("ecs", ecsObject);

        JSONObject metricSetObject = new JSONObject();
        metricSetObject.put("period", 10000);
        metricSetObject.put("name", "core");
        bigJson.put("metricset", metricSetObject);
        bigJson.put("transtime", DateUtil.getUTCTimeStr());
        bigJson.put("@timestamp", DateUtil.getUTCTimeStr());

        JSONObject serviceObject = new JSONObject();
        serviceObject.put("type", "system");
        bigJson.put("service", serviceObject);
        bigJson.put("transip", "192.168.70.120");

        return bigJson.toJSONString();
    }

    /**
     * 发送数据
     *
     * @param producer
     * @param topic
     * @param message
     * @param <T>
     */
    private static <T> void send(KafkaProducer<String, T> producer, String topic, T message) {
        ProducerRecord<String, T> producerRecord = new ProducerRecord<>(topic, null, message);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (null != exception) {
                    System.out.println(String.format("消息发送失败：%s", metadata.toString()));
                    exception.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) throws InterruptedException {
        String topic = "ods_all_metric";
//        String bootstrapServers = "autotest-1:9092,autotest-2:9092,autotest-3:9092";
        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
        long records = 10000L;

        System.out.println(buildMsg());

        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());

        for (long index = 0; index < records; index++) {
            String message = buildMsg();
            System.out.println(message);
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);

    }
}
