package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.common.avro.AvroSerializerFactory;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiese
 * @Description 谢森指标转指标模拟数据代码
 * @Email xiesen310@163.com
 * @Date 2020/8/5 17:53
 */
public class xiesenMetricAvro {

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


    private static byte[] buildAvroMessage() {
        Map<String, Object> bigMap = new HashMap<>();
        //固定字段
        String metricSetName = "streamx_metric_cpu2";
        long ts = System.currentTimeMillis();
        String timestamp = String.valueOf(ts);


        //维度列

        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("appprogramname", "tomcat");
        dimensions.put("appsystem", "streamx");
        dimensions.put("hostname", "flink02");
        dimensions.put("ip", "192.168.0.2");

        //度量列
        Map<String, Double> metrics = new HashMap<>();

        metrics.put("cpu_usage_rate", randomCpuUsageRate());
        metrics.put("cpu_usage_rate2", randomCpuUsageRate());
        metrics.put("cpu_usage_rate3", randomCpuUsageRate());
        bigMap.put("metricsetname", metricSetName);
        bigMap.put("timestamp", timestamp);
        bigMap.put("dimensions", dimensions);
        bigMap.put("metrics", metrics);
        writeUsingFileWriter(JSON.toJSONString(bigMap));
        return AvroSerializerFactory.getMetricAvroSerializer().serializingMetric(metricSetName, timestamp, dimensions
                , metrics);
    }

    private static byte[] buildAvroMessage(String metricSetName, String timestamp) {
        Map<String, Object> bigMap = new HashMap<>();
        //固定字段
        /// String metricSetName = "streamx_metric_cpu2";
//        long ts = System.currentTimeMillis();
//        String timestamp = String.valueOf(ts);


        //维度列

        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("appprogramname", "tomcat");
        dimensions.put("appsystem", "streamx");
        dimensions.put("hostname", "flink02");
        dimensions.put("ip", "192.168.0.2");

        //度量列
        Map<String, Double> metrics = new HashMap<>();

        metrics.put("cpu_usage_rate", randomCpuUsageRate());
        bigMap.put("metricsetname", metricSetName);
        bigMap.put("timestamp", timestamp);
        bigMap.put("dimensions", dimensions);
        bigMap.put("metrics", metrics);
        writeUsingFileWriter(JSON.toJSONString(bigMap));
        return AvroSerializerFactory.getMetricAvroSerializer().serializingMetric(metricSetName, timestamp, dimensions
                , metrics);
    }

    private static Double[] CPU_USAGE_RATE_ARR = {0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

    private static Double randomCpuUsageRate() {
        Random random = new Random();
        return CPU_USAGE_RATE_ARR[random.nextInt(CPU_USAGE_RATE_ARR.length)];
    }

    private static void writeUsingFileWriter(String data) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("D:\\tmp\\metrics\\streamx_metric_cpu2.json", true);
            writer.write(data + "\n\r");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

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
        long start = System.currentTimeMillis();
        String topic = "xiesen_metric_data_multi";
        String bootstrapServers = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";
        long records = 10000000L;

        KafkaProducer<String, byte[]> producer = buildProducer(bootstrapServers, ByteArraySerializer.class.getName());
        long index = 0;
        for (index = 0; index < records; index++) {
            if (index % 1000 == 0) {
                System.out.println("写入了 " + index + " 条数据");
            }
            String metricSetNamePrefix = "streamx_metric_cpu";
            long ts = System.currentTimeMillis();
            byte[] message = buildAvroMessage();
            send(producer, topic, message);
            TimeUnit.MILLISECONDS.sleep(2);
        }

        producer.flush();
        producer.close();

        long end = System.currentTimeMillis();
        int second = (int) ((end - start) / 1000);
        printTime(records, second);
        Thread.sleep(1000L);
    }

    private static void printTime(long records, int second) {
        int hour, minute;
        hour = second / 3600;
        minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        System.out.println("写入到 kafka " + records + " 条数据,耗时: " + hour + " 小时 " + minute + " 分钟 " + second + " 秒");
    }
}
