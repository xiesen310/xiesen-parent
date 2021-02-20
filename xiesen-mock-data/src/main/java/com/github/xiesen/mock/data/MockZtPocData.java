package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 模拟政通 poc 测试数据
 *
 * @author 谢森
 * @since 2021/2/9
 */
public class MockZtPocData {
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
     * {
     * "deleteList": ["47ae69534434bb317d9e4d52b5b87a74", "367da0d1ceea81e6734b0c7855ce2104",
     * "548dac9533b5aa6ce9931b90b17a4edc", "b04533dcdbdbe27d79ebd7fd7bacf0ae"],
     * "index": "be_performed",
     * "type": "be_performed",
     * "updateList": [{
     * "caseCode ": " (2009) 朝执字第03594号 ",
     * "caseCreateTime": 1239724800000,
     * "execCourtName": "北京市朝阳区人民法院",
     * "execMoney": "6300",
     * "fssFbdate": 1534176000000,
     * "md5": "3d9093939638a111e204c1b7979a8994",
     * "pName": "北京时代运输有限公司 ",
     * "upDated": 1584028800000,
     * "upstate": 2
     * }]
     * }
     *
     * @return
     */
    public static String buildMsg() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", "be_performed");
        jsonObject.put("type", "be_performed");

        jsonObject.put("caseCode", "(2009) 朝执字第03594号");
        jsonObject.put("caseCreateTime", System.currentTimeMillis());
        jsonObject.put("execCourtName", "北京市朝阳区人民法院");
        jsonObject.put("execMoney", "6300");
        jsonObject.put("fssFbdate", System.currentTimeMillis());
        jsonObject.put("md5", UUID.randomUUID().toString().replaceAll("-", ""));
        jsonObject.put("pName", "北京时代运输有限公司");
        jsonObject.put("upDated", System.currentTimeMillis());
        jsonObject.put("upstate", 2);
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
        String topic = "zt";
//        String bootstrapServers = "zorkdata-91:9092";
        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
//        String bootstrapServers = "zorkdata-92:9092";
        long records = 1000L;

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
