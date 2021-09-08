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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author 谢森
 * @Description ReadMetricFile2Kafka
 * @Email xiesen310@163.com
 * @Date 2021/1/20 9:49
 */
public class ReadJsonFile2KafkaMetric {
    public static void main(String[] args) {
        String topic = "xiesen";
        String filePath = "D:\\tmp\\error.txt";
        CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen-parent\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();

//        producer.sendMetric(metricSetName, timestamp, dimensions, metrics);


        try {

            File file = new File(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String strLine = null;
            int lineCount = 0;

            while (null != (strLine = bufferedReader.readLine())) {
//                if (lineCount % 1000 == 0) {
                System.out.println("第[" + lineCount + "]行数据:" + strLine);
//                }

                JSONObject jsonObject = JSONObject.parseObject(strLine);
                String metricsetname = jsonObject.getString("metricsetname");
                String timestamp = jsonObject.getString("timestamp");
                String dimensionStr = jsonObject.getString("dimensions");
                JSONObject jsonObject1 = JSONObject.parseObject(dimensionStr);
                Map<String, String> dimensions = jsonObject1.toJavaObject(Map.class);
               /* if (dimensions.containsKey("errorInfo")) {
                    String errorInfo = dimensions.get("errorInfo");
                    System.out.println(errorInfo);
                    String s = errorInfo.replaceAll("\\.", " ").replaceAll("\\n", "").replaceAll("-", "");
                    System.out.println(s);
                    dimensions.put("errorInfo", s);
//                    dimensions.remove("errorInfo");
                }*/

                String metricStr = jsonObject.getString("metrics");
                JSONObject jsonObject2 = JSONObject.parseObject(metricStr);
                Set<String> keySet = jsonObject2.keySet();
                Map<String, Double> metrics = new HashMap<>();
                for (String key : keySet) {
                    double value = jsonObject2.getDoubleValue(key);
                    metrics.put(key, value);
                }


                System.out.println("metricSetName = " + metricsetname);
                System.out.println("timestamp = " + timestamp);
                System.out.println("dimensions = " + dimensions);
                System.out.println("metrics = " + metrics);


                producer.sendMetric(metricsetname, timestamp, dimensions, metrics);
                Thread.sleep(1000);
                lineCount++;
                System.out.printf(lineCount + "");
            }
            System.out.println("数据总条数: " + lineCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        producer.flush();
//        producer.close();

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
