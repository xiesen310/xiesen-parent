package com.github.xiesen.mock.data;

import com.github.xiesen.common.avro.AvroSerializerFactory;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author xiese
 * @Description 张畅指标转指标模拟数据代码
 * @Email xiesen310@163.com
 * @Date 2020/8/5 17:53
 */
public class ZhangChangMetric2MetricProducer {

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

        return new KafkaProducer<>(props);
    }


    private static byte[] buildAvroMessage() {
        //固定字段
        String metricSetName = "zhangchang_log2metric";
        String timestamp = String.valueOf(System.currentTimeMillis());

        //维度列
        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("appprogramname", "tomcat");
        dimensions.put("appsystem", "OMDB");
        dimensions.put("hostname", "kafka-34");
        dimensions.put("ip", "192.168.30.34");

        //度量列
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("countNum", 1.0);

        return AvroSerializerFactory.getMetricAvroSerializer().serializingMetric(metricSetName, timestamp, dimensions, metrics);
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
        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
        long records = 10000L;

        KafkaProducer<String, byte[]> producer = buildProducer(bootstrapServers, ByteArraySerializer.class.getName());
        long index = 0;
        for (index = 0; index < records; index++) {
            byte[] message = buildAvroMessage();
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();

        Thread.sleep(1000L);
    }
}
