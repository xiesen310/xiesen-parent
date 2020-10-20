package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.avro.AvroSerializerFactory;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.SaslConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author xiese
 * @Description 张维指标转指标模拟数据代码
 * @Email xiesen310@163.com
 * @Date 2020/8/5 17:53
 */
public class ZhangWeiJsonProducer {

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
        System.setProperty("java.security.krb5.conf", "D:\\tmp\\kerberos\\krb5.conf");
        System.setProperty("java.security.auth.login.config", "D:\\tmp\\kerberos\\kafka_server_jaas.conf");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");

        // sasl 认证
        /*props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");
        Configuration.setConfiguration(new SaslConfig("admin", "admin"));*/


        return new KafkaProducer<>(props);
    }


    public static String buildMsg() {
        JSONObject filebeatJson = new JSONObject();
        JSONObject beat = new JSONObject();
        JSONObject host = new JSONObject();
        JSONObject log = new JSONObject();
        JSONObject path = new JSONObject();
        log.put("file", path);
        path.put("path", "/var/log/access.log");
        JSONObject metadata = new JSONObject();
        metadata.put("beat", "filebeat");
        metadata.put("type", "doc");
        metadata.put("version", "6.8.1");
        JSONObject prospector = new JSONObject();
        prospector.put("type", "log");

        beat.put("hostname", "zorkdata-151");
        beat.put("name", "zorkdata-151");
        beat.put("version", "6.8.1");

        host.put("containerized", "false");
        host.put("name", "zorkdata-151");
        host.put("architecture", "x86_64");

        String message = "";
        filebeatJson.put("offset", "1593590026413");
        filebeatJson.put("log", log);
        filebeatJson.put("@metadata", metadata);
        filebeatJson.put("prospector", prospector);
        filebeatJson.put("logTypeName", "test_topic_log8");
        filebeatJson.put("source", "/var/log/nginx/access.log");
        filebeatJson.put("message", message);
        filebeatJson.put("collectorruleid", "1");
        filebeatJson.put("appprogramname", "test_appprogramname8");
        filebeatJson.put("@timestamp", DateUtil.getUTCTimeStr());

        filebeatJson.put("servicecode", "test_cdde8");
        filebeatJson.put("appsystem", "test_appsystem8");
        filebeatJson.put("beat", beat);
        filebeatJson.put("host", host);
        filebeatJson.put("servicename", "test_appprogramname8");
        filebeatJson.put("grokmessage", "192.168.1.151 - - [29/Jun/2020:16:09:23 +0800] \"GET /webserver/scene/getSceneList.do?menuItemId=1039&sceneGroupId=&templateFlag= HTTP/1.0\" 200 3167");
        filebeatJson.put("datemessage", "2020-07-06T08:22:00.666Z");
        filebeatJson.put("jsonmessage", "{\"testmessage\":\"test\"}");
        filebeatJson.put("geoipmessage", "151.101.230.217");
        filebeatJson.put("csvmessage", "aaaa,bbbbb,cccc");
        filebeatJson.put("kvmessage", "pin=12345~0&d=123&e=foo@bar.com&oq=bobo&ss=12345");
        filebeatJson.put("mutatemessage", "8e3dfc85999b4e02bae4adf4b92b909a");
        int length = filebeatJson.toJSONString().length();

        StringBuilder msg = new StringBuilder();
        String strs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        while (length <= 1024) {
            msg.append(strs);
            message = message + msg;
            length += strs.length();
        }
        filebeatJson.put("message", message);
        return filebeatJson.toJSONString();
    }


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
        String topic = "xiesen";
        String bootstrapServers = "zorkdata-91:9092";
        long records = 1000L;
        System.out.println(buildMsg());
        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());
        long index = 0;
        for (index = 0; index < records; index++) {
            String message = buildMsg();
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();

        Thread.sleep(1000L);
    }
}
