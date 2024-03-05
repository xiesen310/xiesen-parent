package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author 谢森
 * @Description 模拟解析格式数据
 * @Email xiesen310@163.com
 * @Date 2020/12/14 13:21
 */
public class MockShaojiaoMetricAvro {

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

    public static String ipPrefix() {
        boolean b = new Random().nextBoolean();
        if (b) {
            return "120";
        } else {
            return "121";
        }
    }

    //    public static final List<String> METRIC_SET_NAMES = Arrays.asList("core_system_mb", "cpu_system_mb", "memory_system_mb");
    public static final List<String> METRIC_SET_NAMES = Arrays.asList("core_system_mb");

    public static String randomMetricSetName() {
        return METRIC_SET_NAMES.get(new Random().nextInt(METRIC_SET_NAMES.size()));
    }

    public static String buildMsg() {
        JSONObject bigJson = new JSONObject();
//        String hostname = "yf12011111111";
        String prefix = ipPrefix();
        String hostname = "yf" + prefix;

        JSONObject hostJson = new JSONObject();
        hostJson.put("name", hostname);
        bigJson.put("host", hostJson);

        bigJson.put("topicname", "ods_default_log");
        bigJson.put("clustername", "基础监控");
        bigJson.put("message", "xiesen test data");
        bigJson.put("ip", "192.168.70." + prefix);
        JSONObject inputJson = new JSONObject();
        inputJson.put("type", "log");
        bigJson.put("input", inputJson);

        bigJson.put("servicecode", "lmt模块");
        bigJson.put("appprogramname", "lmt模块");
        bigJson.put("@version", "1");

        JSONObject agentJson = new JSONObject();
        agentJson.put("hostname", hostname);
        agentJson.put("ephemeral_id", "e955e2aa-4627-400e-a51c-2abbb7367e41");
        agentJson.put("id", "59ecdfa2-5be4-4021-9653-c1124aecb7d7");
        agentJson.put("version", "7.4.0");
        agentJson.put("type", "filebeat");

        bigJson.put("agent", agentJson);

        JSONArray tagsArray = new JSONArray();
        tagsArray.add("beats_input_codec_plain_applied");

        bigJson.put("tags", tagsArray);
        bigJson.put("servicename", "lmt模块");

        JSONObject logJson = new JSONObject();
        JSONObject fileJson = new JSONObject();
        fileJson.put("path", "/var/log/monit.log");
        logJson.put("file", fileJson);
        logJson.put("offset", 938284);

        bigJson.put("log", logJson);
        bigJson.put("appsystem", "dev_test");
        bigJson.put("collectruleid", 5);


        JSONObject ecsJson = new JSONObject();
        ecsJson.put("version", "1.1.0");
        bigJson.put("ecs", ecsJson);

        bigJson.put("collecttime", DateUtil.getUTCTimeStr());
        bigJson.put("transtime", DateUtil.getUTCTimeStr());
        bigJson.put("@timestamp", DateUtil.getUTCTimeStr());
        bigJson.put("transip", "192.168.70.85");

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

        int size = 1000000;

        CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen-parent\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();
        for (int i = 0; i < size; i++) {
            String metricSetName = randomMetricSetName();
            String timestamp = String.valueOf(System.currentTimeMillis());
            java.util.Map<String, String> dimensions = new HashMap<>();
//            String prefix = ipPrefix();
            String prefix = "120";
            dimensions.put("appsystem", "dev_test");
            dimensions.put("hostname", "autotest-" + prefix);
            dimensions.put("ip", "192.168.70." + prefix);
//            dimensions.put("servicename", "lmt模块");
//            dimensions.put("clustername", "基础监控");
            dimensions.put("appprogramname", "lmt模块");
            dimensions.put("name", UUID.randomUUID().toString().replaceAll("-", ""));

            Map<String, Double> metrics = new HashMap<>();

//            double v = new Random().nextDouble();
            double v = 0.8;

            metrics.put("user_pct", v);


            Map<String, Object> map = new HashMap<>();
            map.put("metricsetname", metricSetName);
            map.put("timestamp", timestamp);
            map.put("dimensions", dimensions);
            map.put("metrics", metrics);
            System.out.println(JSON.toJSONString(map));

            producer.sendMetric(metricSetName, timestamp, dimensions, metrics);
            Thread.sleep(2L);
        }

        Thread.sleep(1000L);
    }
}
