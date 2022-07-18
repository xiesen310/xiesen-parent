package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.DateTimeUtils;
import com.github.xiesen.model.ZorkSpan;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @Description 模拟解析格式数据
 * @Email xiesen310@163.com
 * @Date 2020/12/14 13:21
 */
public class MockChinaBankCallChainData {
    public static final String PARENT_ID = "0000000000000000";

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
    public static String buildMsg() {
        String traceId = UUID.randomUUID().toString().replaceAll("-", "");
        JSONObject jsonObject = CallA(traceId);

        return jsonObject.toJSONString();
    }

    /**
     * A --> B --> C
     *
     * @return
     */
    private static JSONObject CallA(String traceId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", DateUtil.getUTCTimeStr());
        jsonObject.put("logTypeName", "test");
        jsonObject.put("offset", "0");

        JSONObject normalFields = new JSONObject();
        normalFields.put("traceid", traceId);
        normalFields.put("parentid", PARENT_ID);
        normalFields.put("spanid", UUID.randomUUID().toString().replaceAll("-", ""));
        normalFields.put("starttime", System.currentTimeMillis() + "");
        normalFields.put("endtime", System.currentTimeMillis() + "");
        normalFields.put("spanname", "SpringMvcSpan|0");
        normalFields.put("code", "200");
        normalFields.put("spantype", "0");
        normalFields.put("spanlayer", "HTTP");
        normalFields.put("remoteendpointname", "");
        normalFields.put("remoteappsystem", "noah");
        normalFields.put("remoteservicename", "");
        normalFields.put("remoteserviceinstance", "");
        normalFields.put("remoteip", "10.35.76.209");
        normalFields.put("addinformation", "");
        jsonObject.put("normalFields", normalFields);

        JSONObject dimensions = new JSONObject();
        dimensions.put("endpoint", "http://10.182.200.92.8090");
        dimensions.put("component", "cn.boccfc.tracer.plugin.springmvc.interceptor.RequestMappingMethodInterceptor");
        dimensions.put("hostname", "cl-hsfw-1-ap01");
        dimensions.put("ip", "10.35.76.209");
        dimensions.put("serviceinstance", "10.35.76.209");
        dimensions.put("calltype", "remote");
        dimensions.put("operationname", "icmRPCCall");
        dimensions.put("appsystem", "noah");
        dimensions.put("servicename", "icmRPCCall");
        dimensions.put("status", "0");
        jsonObject.put("dimensions", dimensions);

        JSONObject measures = new JSONObject();
        measures.put("duration", randomDuration());
        jsonObject.put("measures", measures);
        return jsonObject;
    }

    private static Double randomDuration() {
        Random random = new Random();
        double MAX = 100000.0;
        double MIN = 1000.0;
        double result = 0;
        for (int i = 0; i < 10; i++) {
            result = MIN + (random.nextDouble() * (MAX - MIN));
            result = (double) Math.round(result * 100) / 100;
        }
        return result;
    }

    private static JSONObject CallB(String traceId, String parentId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", DateUtil.getUTCTimeStr());
        jsonObject.put("logTypeName", "test");
        jsonObject.put("offset", "0");

        JSONObject normalFields = new JSONObject();
        normalFields.put("traceid", traceId);
        normalFields.put("parentid", PARENT_ID);
        normalFields.put("spanid", UUID.randomUUID().toString().replaceAll("-", ""));
        normalFields.put("starttime", System.currentTimeMillis() + "");
        normalFields.put("endtime", System.currentTimeMillis() + "");
        normalFields.put("spanname", "SpringMvcSpan|0");
        normalFields.put("code", "200");
        normalFields.put("spantype", "0");
        normalFields.put("spanlayer", "HTTP");
        normalFields.put("remoteendpointname", "");
        normalFields.put("remoteappsystem", "noah");
        normalFields.put("remoteservicename", "");
        normalFields.put("remoteserviceinstance", "");
        normalFields.put("remoteip", "10.35.76.208");
        normalFields.put("addinformation", "");
        jsonObject.put("normalFields", normalFields);

        JSONObject dimensions = new JSONObject();
        dimensions.put("endpoint", "http://10.182.200.92.8090");
        dimensions.put("component", "cn.boccfc.tracer.plugin.springmvc.interceptor.RequestMappingMethodInterceptor");
        dimensions.put("hostname", "cl-hsfw-1-ap01");
        dimensions.put("ip", "10.35.76.208");
        dimensions.put("serviceinstance", "10.35.76.208");
        dimensions.put("calltype", "remote");
        dimensions.put("operationname", "icmRPCCall");
        dimensions.put("appsystem", "noah");
        dimensions.put("servicename", "icmRPCCall");
        dimensions.put("status", "0");
        jsonObject.put("dimensions", dimensions);

        JSONObject measures = new JSONObject();
        measures.put("duration", randomDuration());
        jsonObject.put("measures", measures);
        return jsonObject;
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


    private static void sendData(String topic, String brokers, long records) throws InterruptedException {
        KafkaProducer<String, String> producer = buildProducer(brokers, "org.apache.kafka.common.serialization.StringSerializer");

        for (long index = 0; index < records; index++) {
            String traceId = UUID.randomUUID().toString().replaceAll("-", "");
            String msgA = CallA(traceId).toJSONString();
            ZorkSpan zorkSpan = JSONObject.parseObject(msgA, ZorkSpan.class);
            String spanidA = zorkSpan.getNormalFields().getSpanid();
            send(producer, topic, msgA);
            TimeUnit.SECONDS.sleep(1);

            String msgB = CallB(traceId, spanidA).toJSONString();
            ZorkSpan zorkSpanB = JSONObject.parseObject(msgB, ZorkSpan.class);
            String spanidB = zorkSpanB.getNormalFields().getSpanid();
            send(producer, topic, msgB);
            TimeUnit.SECONDS.sleep(1);

//            String msgC = CallB(traceId, spanidB).toJSONString();
//            send(producer, topic, msgC);
//            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();
    }

    public static void main(String[] args) throws InterruptedException {
        String topic = "china_bank_call_chain";
        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
        long records = 1000L;

        sendData(topic, bootstrapServers, records);
        Thread.sleep(1000L);

    }
}
