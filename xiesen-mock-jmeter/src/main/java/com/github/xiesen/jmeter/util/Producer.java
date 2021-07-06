package com.github.xiesen.jmeter.util;

import com.github.xiesen.common.avro.AvroSerializerFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/8/14 12:44
 */
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private String bootstrapServer;
    private int kafkaBathSize;

    private KafkaProducer<String, byte[]> producerByte;
    private KafkaProducer<String, String> producerString;

    public void initConfig(String kafkaAddress) {
        kafkaBathSize = 100000;
        bootstrapServer = kafkaAddress;
    }

    public Producer(String kafkaAddress) {
        try {
            initConfig(kafkaAddress);
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            // props.put("client.id", "webAPI4LogGather"); 不自定义client.id,使用自增
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaBathSize);
            // 启用压缩
            props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
            producerByte = new KafkaProducer<>(props);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            producerString = new KafkaProducer<>(props);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    /**
     * 发送logAvro数据
     *
     * @param topic        指标名称
     * @param logTypeName  日志结构名称
     * @param timestamp    时间
     * @param source       日志路径
     * @param offset       偏移量
     * @param dimensions   维度
     * @param metrics      指标
     * @param normalFields 普通列
     */
    public void sendLogAvro(String topic, String logTypeName, String timestamp, String source, String offset,
                            Map<String, String> dimensions, Map<String, Double> metrics,
                            Map<String, String> normalFields) {
        try {
            byte[] bytes = AvroSerializerFactory.getLogAvroSerializer().serializingLog(logTypeName, timestamp, source,
                    offset, dimensions, metrics, normalFields);
            producerByte.send(new ProducerRecord<>(topic, null, bytes));
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * 想kafka发送metricAvro数据
     *
     * @param metricTopic   topic名称
     * @param metricSetName 指标集名称
     * @param timestamp     时间戳
     * @param dimensions    维度
     * @param metrics       指标
     */
    public void sendMetricAvro(String metricTopic, String metricSetName, String timestamp,
                               Map<String, String> dimensions,
                               Map<String, Double> metrics) {
        try {
            byte[] bytes = AvroSerializerFactory.getMetricAvroSerializer().serializingMetric(metricSetName, timestamp,
                    dimensions, metrics);
            producerByte.send(new ProducerRecord<>(metricTopic, null, bytes)).get();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * 向kafka发送json数据
     *
     * @param topic topic名称
     * @param json  json数据
     */
    public void sendJson(String topic, String json) {
        try {
            producerString.send(new ProducerRecord<>(topic, null, json));
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }


    public void close() {
        producerString.close();
        producerByte.close();
    }


    public void closeByte() {
        producerByte.close();
    }

    public void closeString() {
        producerString.close();
    }

}
