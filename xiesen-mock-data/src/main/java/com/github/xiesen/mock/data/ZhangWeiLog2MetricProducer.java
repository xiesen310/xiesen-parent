package com.github.xiesen.mock.data;

import com.github.xiesen.common.avro.AvroSerializerFactory;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.SaslConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author xiese
 * @Description 张维日志转指标模拟数据代码
 * @Email xiesen310@163.com
 * @Date 2020/8/5 17:41
 */
public class ZhangWeiLog2MetricProducer {
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


    private static byte[] buildLogMessage() {
        //固定字段
        String logTypeName = "streamx_log_test";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/opt/20191231.log";
        String offset = String.valueOf(6322587L);

        //维度列
        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("appprogramname", "tc50");
        dimensions.put("appsystem", "TXJY");
        dimensions.put("hostname", "localhost");
        dimensions.put("ip", "192.168.0.20");

        //度量列
        Map<String, Double> measures = new HashMap<>();

        //普通列
        Map<String, String> normalFields = new HashMap<>();
        normalFields.put("age", "26");
        normalFields.put("country_code", "CN");
        normalFields.put("id", "xxxxeeeeeee");
        normalFields.put("message", "功能请求 IP:182.140.129.3");
        normalFields.put("name", "张三");

        return AvroSerializerFactory.getLogAvorSerializer().serializingLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);
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
        String topic = "log2metric";
        String bootstrapServers = "zorkdata-91:9092";
        long records = 1000L;

        KafkaProducer<String, byte[]> producer = buildProducer(bootstrapServers, ByteArraySerializer.class.getName());
        long index = 0;
        for (index = 0; index < records; index++) {
            byte[] message = buildLogMessage();
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);
    }
}
