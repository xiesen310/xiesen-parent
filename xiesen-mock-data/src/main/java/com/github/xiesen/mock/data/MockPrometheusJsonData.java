package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @Description 模拟 prometheus 格式数据
 * @Email xiesen310@163.com
 * @Date 2020/12/14 13:21
 */
public class MockPrometheusJsonData {
    public static final List<String> ips = new ArrayList<String>();

    static {
        ips.add("10.180.11.33");
        ips.add("148.70.65.234");
        ips.add("10.180.230.87");
        ips.add("10.180.227.79");
        ips.add("10.181.13.158");
        ips.add("10.180.5.62");
        ips.add("10.180.1.21");
        ips.add("10.180.1.4");
        ips.add("10.180.1.14");
    }

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

    /**
     * @return
     */
    public static String buildMsg(String ip) {
        JSONObject jsonObject = new JSONObject();
        String name = "flink_jobmanager_Status_JVM_Memory_Direct_Count";
        JSONObject labels = new JSONObject();
        labels.put("__name__", name);
        labels.put("app", "streamx");
        labels.put("applicationType", "ZorkData StreamX");
        labels.put("exported_job", "siddhi_test");
        labels.put("host", "cdh_2");
        labels.put("instance", ip + ":9091");
        labels.put("job", "flink-pushgateway");
        String timestamp = DateUtil.getParseTimeStr();
        String value = "" + new Random().nextInt(20) + 5;

        jsonObject.put("labels", labels);
        jsonObject.put("name", name);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("value", value);
        return jsonObject.toJSONString();
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
        String topic = "mock-prometheus-data";
        String bootstrapServers = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";
        long records = 1000000L;
        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());

        for (long index = 0; index < records; index++) {
            ips.forEach(ip -> {
                String message = buildMsg(ip);
                System.out.println(message);
                send(producer, topic, message);
            });
            TimeUnit.SECONDS.sleep(5);
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);

    }
}
