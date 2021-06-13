package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.avro.AvroSerializerFactory;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * @author 谢森
 * @Description 模拟日志合并数据
 * @Email xiesen310@163.com
 * @Date 2020/12/11 11:34
 */
public class MockLogHadoopData2 {

    public static void main(String[] args) throws InterruptedException {
        String topic = "xiesen";
        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
        long records = 100000000L;

        KafkaProducer<String, byte[]> producer = buildProducer(bootstrapServers, ByteArraySerializer.class.getName());
        for (long index = 0; index < records; index++) {
            send(producer, topic, mockNetWorkLog());
        }

        Thread.sleep(2000);

    }

    /**
     * 构建 kafkaProducer
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


    private static Map<String, Double> getRandomMetrics() {
        Map<String, Double> dimensionsMap = new HashMap<>();
        Random random = new Random();
        int i = random.nextInt(200);
        dimensionsMap.put("latency", Double.valueOf(i));
        return dimensionsMap;
    }

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        Map<String, String> dimensionsMap = new HashMap<>();
        dimensionsMap.put("appsystem", "dev_test");
        dimensionsMap.put("hostname", "yf12" + random.nextInt(10));
        dimensionsMap.put("appprogramname", "linux模块");
        dimensionsMap.put("clustername", "基础监控");
        dimensionsMap.put("ip", "192.168.70.12" + random.nextInt(10));
        return dimensionsMap;

    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>();
        normalFields.put("countryCode", "BD");
        normalFields.put("message", "data update error ptah");
        return normalFields;
    }

    private static String getRandomOffset() {
        Random random = new Random();
        long l = random.nextInt(10000);
        return String.valueOf(l);
    }

    /**
     * 模拟响应数据
     */
    private static byte[] mockNetWorkLog() {

        JSONObject jsonObject = new JSONObject();
        String logTypeName = "default_analysis_template";
        String timestamp = DateUtil.getUTCTimeStr();

        Map<String, String> dimensions = getRandomDimensions();
        Map<String, String> normalFieldsMap = getRandomNormalFields();
        Map<String, Double> measures = getRandomMetrics();

        jsonObject.put("logTypeName", logTypeName);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("source", "/var/log/path.log");

        String offset = getRandomOffset();
        jsonObject.put("offset", offset);

        jsonObject.put("dimensions", dimensions);
        jsonObject.put("measures", measures);
        jsonObject.put("normalFields", normalFieldsMap);

        byte[] bytes = AvroSerializerFactory.getLogAvroSerializer().serializingLog(logTypeName, timestamp, "核新",
                offset, dimensions, measures, normalFieldsMap);

        return bytes;
    }


}
