package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * @author 谢森
 * @Description ReadMetricFile2Kafka
 * @Email xiesen310@163.com
 * @Date 2021/1/20 9:49
 */
public class ReadLogFile2Kafka {
    public static void main(String[] args) {
        String topic = "ods_ht_log";
//        String filePath = "D:\\tmp\\原始topic.txt";
//        String filePath = "/Users/xiesen/Desktop/esb.log";
        String filePath = "/Users/xiesen/Downloads/normal.log";

        String bootstrapServers = "192.168.70.6:9092,192.168.70.7:9092,192.168.70.8:9092";
        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());


        try {

            File file = new File(filePath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine = null;
            int lineCount = 0;

            while (null != (strLine = bufferedReader.readLine())) {
                if (lineCount % 1000 == 0) {
                    System.out.println("第[" + lineCount + "]行数据:" + strLine);
                }
                if (null != strLine) {

                    send(producer, topic, strLine);
                    lineCount++;
                }
            }
            System.out.println("数据总条数: " + lineCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        producer.flush();
        producer.close();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        props.put("max.request.size", 5242880 * 2);

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
