package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.*;
import java.util.*;

/**
 * @author 谢森
 * @Description ReadMetricFile2Kafka
 * @Email xiesen310@163.com
 * @Date 2021/1/20 9:49
 */
public class ReadSpanJsonFile2Kafka {

    private static String brokerAddr = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
    private static String topic = "zork-span4";
    private static ProducerRecord<String, String> producerRecord = null;
    private static KafkaProducer<String, String> producer = null;


    public static void main(String[] args) {
        String filePath = "D:\\tmp\\trace.log";
        init();
        try {
            File dir = new File(filePath);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                    int lineCount = insertToKafka(bufferedReader);
                    System.out.println(file.getName() + " 数据总条数: " + lineCount);
                    bufferedReader.close();
                }
            } else {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dir), "UTF-8"));
                int lineCount = insertToKafka(bufferedReader);
                System.out.println(dir.getName() + " 数据总条数: " + lineCount);
                bufferedReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerAddr);
        props.put("acks", "1");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", StringSerializer.class.getName());
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        producer = new KafkaProducer<String, String>(props);
    }


    public static String buildMsg() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appsystem", "test");
        jsonObject.put("message", "this is message, " + UUID.randomUUID());
        return jsonObject.toString();
    }


    /**
     * 发送数据
     *
     * @param topic
     */
    public static void send(String topic, String msg) {
        producerRecord = new ProducerRecord<String, String>(
                topic,
                null,
                msg
        );
        producer.send(producerRecord);
    }


    private static int insertToKafka(BufferedReader bufferedReader) throws IOException, InterruptedException {
        String strLine;
        int lineCount = 0;
        while (null != (strLine = bufferedReader.readLine())) {
            if (lineCount % 1000 == 0) {
                System.out.println("第[" + lineCount + "]行数据:" + strLine);
            }

            JSONObject jsonObject = JSONObject.parseObject(strLine);
            if (null != jsonObject) {
                // System.out.println(strLine);
                JSONObject dimensions = JSONObject.parseObject(jsonObject.getString("dimensions"));
                String servicename = dimensions.getString("servicename");

                JSONObject normalFields = JSONObject.parseObject(jsonObject.getString("normalFields"));
                String spanlayer = normalFields.getString("spanlayer");

                String remoteservicename = normalFields.getString("remoteservicename");

                /*if (null == servicename || servicename.length() == 0) {
                    System.out.println("servicename is null: " + strLine);
                } else if (null == spanlayer || spanlayer.length() == 0) {
                    System.out.println("spanlayer is null: " + strLine);
                }
                else if (servicename.equalsIgnoreCase(remoteservicename)) {
                    System.out.println("remoteservicename == servicename: " + strLine);
                }

                else {
                    send(topic, strLine);
                }*/

                send(topic, strLine);
            } else {
                System.out.println("json is null");
            }
            lineCount++;
        }
        return lineCount;
    }

    /**
     * 按行读取文件
     *
     * @param strFile 文件名称
     */
    public static void readFileByLine(String strFile) {
        try {
            File file = new File(strFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine = null;
            int lineCount = 0;
            while (null != (strLine = bufferedReader.readLine())) {
                JSONObject jsonObject = JSONObject.parseObject(strLine);
                JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.getString("system"));
                boolean flag = jsonObject1.containsKey("filesystem");
                if (flag) {
                    System.out.println("第[" + lineCount + "]行数据:" + strLine);
                }
//                log.info("第[" + lineCount + "]行数据:[" + strLine + "]");
                lineCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
