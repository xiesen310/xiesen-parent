package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.avro.AvroSerializerFactory;
import com.github.xiesen.common.constant.LogConstants;
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
import java.util.concurrent.TimeUnit;

/**
 * @author xiese
 * @Description 张维日志转指标模拟数据代码
 * @Email xiesen310@163.com
 * @Date 2020/8/5 17:41
 */
public class XiesenLog2MetricProducer {
    private static <T> KafkaProducer<String, T> buildProducer(String bootstrapServers, String serializerClassName) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        //props.put("acks", "1");
        props.put("acks", "all");
        props.put("retries", 5);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", serializerClassName);
        props.put("batch.size", 16384);
        props.put("linger.ms", 0);
        props.put("buffer.memory", 33554432);

        return new KafkaProducer<>(props);
    }

    private static String getRandomOffset() {
        Random random = new Random();
        long l = random.nextInt(10000);
        return String.valueOf(l);
    }

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>();

        dimensions.put(LogConstants.STR_HOSTNAME, "zorkdata1");
//        dimensions.put(LogConstants.STR_HOSTNAME, "zorkdata" + i);

        dimensions.put(LogConstants.STR_IP, "192.168.1.1");
//        dimensions.put(LogConstants.STR_IP, "192.168.1." + i);
        dimensions.put(LogConstants.STR_APP_PROGRAM_NAME, "tc50");
        dimensions.put(LogConstants.STR_APP_SYSTEM, "tdx");

        return dimensions;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>();
        normalFields.put(LogConstants.STR_MESSAGE, "data update success");
        return normalFields;
    }

    public static byte[] buildMsg() {
        JSONObject jsonObject = new JSONObject();
        String logTypeName = "stream_log_avro";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/var/log/" + DateUtil.getDate() + ".log";
//        String offset = getRandomOffset();
        String offset = String.valueOf(10000L);

        Map<String, String> dimensions = getRandomDimensions();
        Map<String, Double> measures = new HashMap<>();
        Map<String, String> normalFields = getRandomNormalFields();
        jsonObject.put(LogConstants.STR_LOG_TYPENAME, logTypeName);
        jsonObject.put(LogConstants.STR_TIMESTAMP, timestamp);
        jsonObject.put(LogConstants.STR_SOURCE, source);
        jsonObject.put(LogConstants.STR_OFFSET, offset);
        jsonObject.put(LogConstants.STR_DIMENSIONS, dimensions);
        jsonObject.put(LogConstants.STR_MEASURES, measures);
        jsonObject.put(LogConstants.STR_NORMAL_FIELDS, normalFields);
        return AvroSerializerFactory.getLogAvroSerializer().serializingLog(logTypeName, timestamp, source, offset,
                dimensions, measures, normalFields);
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


    public static void main(String[] args) throws Exception {
        String topic = "xiesen";
        String bootstrapServers = "kafka1:9092,kafka2:9092,kafka3:9092";
        long records = 100000000L;

        KafkaProducer<String, byte[]> producer = buildProducer(bootstrapServers, ByteArraySerializer.class.getName());
        long index = 0;
        for (index = 0; index < records; index++) {
            byte[] message = buildMsg();

            send(producer, topic, message);
            if (index % 60 == 0) {
                TimeUnit.SECONDS.sleep(new Random(100).nextInt());
            }
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);
    }
}
