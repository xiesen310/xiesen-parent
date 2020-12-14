package com.github.xiesen.kafka08.mock;

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

/**
 * @author 谢森
 * @Description 模拟日志合并数据
 * @Email xiesen310@163.com
 * @Date 2020/12/11 11:34
 */
public class MockLogMergeData {

    public static void main(String[] args) throws InterruptedException {
        String topic = "xiesen";
        String bootstrapServers = "kafka-1:9092,kafka-2:9092,kafka-3:9092";
        long records = 100L;

        KafkaProducer<String, byte[]> producer = buildProducer(bootstrapServers, ByteArraySerializer.class.getName());
        for (long index = 0; index < records; index++) {
            send(producer, topic, mockReqData());
            Thread.sleep(1000);
            send(producer, topic, mockRespData());
        }

        producer.close();
        Thread.sleep(1000L);


        for (int i = 0; i < 100; i++) {
            mockReqData();
            Thread.sleep(100);
            mockRespData();
        }

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


    /**
     * 模拟响应数据
     */
    private static byte[] mockRespData() {
        String logTypeName = "tomcat_biz_filebeat";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/data/logs/tomcat/goldsun/goldsun-biz.log";
        String offset = "3933705";

        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("path", "/goldsun/home/getBanners/2.0");
        dimensions.put("hostname", "DVJTY4-WEB149");
        dimensions.put("appprogramname", "DVJTY4-WEB149_8080");
        dimensions.put("servicecode", "SHOUYE");
        dimensions.put("clustername", "东莞");
        dimensions.put("appsystem", "JTY4");
        dimensions.put("servicename", "金太阳4首页服务tomcat");
        dimensions.put("spkg", "d8b747da-01bd-42a4-a1e8-2237c834e3cd");

        Map<String, Double> measures = new HashMap<>();
        measures.put("code", 0.0);
        measures.put("field2", 102.0);


        Map<String, String> normalFields = new HashMap<>();
        normalFields.put("msg", "SYSERR_OK");
        normalFields.put("session", "70376d4341a8bf178cc00146e4039ef522415a75769903fecc777403");
        normalFields.put("ip", "112.17.238.32");
        normalFields.put("logstash_deal_name", "logstash-1");
        normalFields.put("logchecktime", DateUtil.getUTCTimeStr());
        normalFields.put("sysver", "5.4.6");
        normalFields.put("message", "resp data");
        normalFields.put("pkg", "");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        normalFields.put("req_flag", "");
        normalFields.put("logcheckip", "10.33.209.8");
        normalFields.put("deviceVers", "12.3.1");
        normalFields.put("netaddr", "13586991978");
        normalFields.put("logstash_deal_ip", "10.33.196.126");
        normalFields.put("hwID", "A25D3D72E99141A78E1A2EF9AF58279B");
        normalFields.put("usercode", "1519007850385408");

        return AvroSerializerFactory.getLogAvroSerializer().serializingLog(logTypeName, timestamp,
                source, offset, dimensions, measures, normalFields);
    }


    /**
     * 模拟请求数据
     */
    private static byte[] mockReqData() {
        String logTypeName = "tomcat_biz_filebeat_req";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/data/logs/tomcat/goldsun/goldsun-biz.log";
        String offset = "3933705";

        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("path", "/goldsun/home/getBanners/2.0");
        dimensions.put("hostname", "DVJTY4-WEB149");
        dimensions.put("appprogramname", "DVJTY4-WEB149_8080");
        dimensions.put("servicecode", "SHOUYE");
        dimensions.put("clustername", "东莞");
        dimensions.put("appsystem", "JTY4");
        dimensions.put("servicename", "金太阳4首页服务tomcat");
        dimensions.put("spkg", "d8b747da-01bd-42a4-a1e8-2237c834e3cd");

        Map<String, Double> measures = new HashMap<>();
        measures.put("code", 1.0);
        measures.put("fields1", 101.0);


        Map<String, String> normalFields = new HashMap<>();
        normalFields.put("msg", "");
        normalFields.put("session", "");
        normalFields.put("ip", "112.17.238.32");
        normalFields.put("logstash_deal_name", "logstash-1");
        normalFields.put("logchecktime", DateUtil.getUTCTimeStr());
        normalFields.put("sysver", "5.4.6");
        normalFields.put("message", "req data");
        normalFields.put("pkg", "");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        normalFields.put("req_flag", "");
        normalFields.put("logcheckip", "10.33.209.8");
        normalFields.put("deviceVers", "12.3.1");
        normalFields.put("netaddr", "13586991978");
        normalFields.put("logstash_deal_ip", "10.33.196.126");
        normalFields.put("hwID", "A25D3D72E99141A78E1A2EF9AF58279B");
        normalFields.put("usercode", "");

        return AvroSerializerFactory.getLogAvroSerializer().serializingLog(logTypeName, timestamp,
                source, offset, dimensions, measures, normalFields);
    }
}
