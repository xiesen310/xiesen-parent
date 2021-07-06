package com.github.xiesen.kafka08.util;

import com.github.xiesen.common.avro.AvroSerializerFactory;
import com.github.xiesen.common.utils.PropertiesUtil;
import com.github.xiesen.common.utils.StringUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * @author xiese
 * @Description customer producer 08
 * @Email xiesen310@163.com
 * @Date 2020/7/23 20:21
 */
public class CustomerProducer08 {
    private static final Logger logger = LoggerFactory.getLogger(CustomerProducer08.class);
    static String servers = "kafka-1:9092,kafka-2:9092,kafka-3:9092";
    static int batchSize = 1;
    static CustomerProducer08 testProducer;
    static String topics;
    public static long logSize;

    private static KafkaProducer<String, byte[]> producer;
    private static KafkaProducer<String, String> noAvroProducer;

    public static synchronized CustomerProducer08 getInstance(String propertiesName) {
        if (testProducer == null) {
            testProducer = new CustomerProducer08(propertiesName);
        }
        return testProducer;
    }

    public CustomerProducer08(String propertiesName) {
        try {
            initConfig(propertiesName);
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization" +
                    ".StringSerializer");
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization" +
                    ".ByteArraySerializer");

            /**
             * 这个参数控制着相同分区内数据发送的批次个数大小，也就是当数据达到 这个size 时，进行数据发送,
             * 但是并不是数据达不到 size 的值，就不会发送数据，默认是 1048576，即 16k
             */
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);

            producer = new KafkaProducer<String, byte[]>(props);

            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization" +
                    ".StringSerializer");
            noAvroProducer = new KafkaProducer<String, String>(props);

        } catch (Exception ex) {
            logger.error("初始化Kafka失败,系统自动退出! ", ex);
            System.exit(1);
        }
    }

    public void initConfig(String propertiesName) throws Exception {
        Properties properties = PropertiesUtil.getProperties(propertiesName);
        topics = properties.getProperty("log.topic");
        servers = properties.getProperty("kafka.servers", "zorkdata-151:9092").trim();
        batchSize = StringUtil.getInt(properties.getProperty("kafka.batch.size", "5000").trim(), 1);
        logSize = StringUtil.getLong(properties.getProperty("log.size", "5000").trim(), 1);
    }

    /**
     * 发送日志数据
     *
     * @param logTypeName  日志集
     * @param timestamp    时间戳
     * @param source       日志来源
     * @param offset       offset
     * @param dimensions   维度
     * @param measures     度量值
     * @param normalFields 普通列
     */
    public void sendLog(String logTypeName, String timestamp, String source, String offset,
                        Map<String, String> dimensions, Map<String, Double> measures,
                        Map<String, String> normalFields) {
        try {
            byte[] bytes = AvroSerializerFactory.getLogAvroSerializer().serializingLog(logTypeName, timestamp, source,
                    offset, dimensions, measures, normalFields);
            producer.send(new ProducerRecord<String, byte[]>(topics, null, bytes));
        } catch (Exception e) {
            logger.error("sendLog-插入Kafka失败", e);
        }
    }

    /**
     * 发送指标数据
     *
     * @param metricSetName 指标集
     * @param timestamp     时间戳
     * @param dimensions    维度
     * @param metrics       指标
     */
    public void sendMetric(String metricSetName, String timestamp, Map<String, String> dimensions, Map<String,
            Double> metrics) {
        try {
            byte[] bytes = AvroSerializerFactory.getMetricAvroSerializer().serializingMetric(metricSetName, timestamp
                    , dimensions, metrics);
            producer.send(new ProducerRecord<String, byte[]>(topics, null, bytes));
        } catch (Exception e) {
            logger.error("sendMetric-插入Kafka失败", e);
        }
    }


    public void sendJsonLog(String logJson) {
        try {
            noAvroProducer.send(new ProducerRecord<String, String>(topics, null, logJson));
        } catch (Exception e) {
            logger.error("send json Log-插入Kafka失败", e);
        }
    }
}
