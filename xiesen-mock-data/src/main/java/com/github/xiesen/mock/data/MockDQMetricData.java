package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.mortbay.util.ajax.JSON;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @Description 模拟解析格式数据
 * @Email xiesen310@163.com
 * @Date 2020/12/14 13:21
 */
public class MockDQMetricData {
    private static String topic = "xiesen_metric_data_multi";
    private static String brokerAddr = "kafka-1:29092,kafka-2:29092,kafka-3:29092";
    private static ProducerRecord<String, String> producerRecord = null;
    private static KafkaProducer<String, String> producer = null;

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

    public static final List<String> METRIC_SET_NAMES = Arrays.asList(
//            "mock_disk_system_mb",
            "mock_cpu_system_mb",
            "mock_memory_system_mb");

    public static String randomMetricSetName() {
        return METRIC_SET_NAMES.get(new Random().nextInt(METRIC_SET_NAMES.size()));
    }

    public static Double randomUserPct() {
        final double v = new Random().nextDouble();
        final String str = String.format("%.2f", v);
        return Double.parseDouble(str);
    }


    /**
     * 发送数据
     *
     * @param producer
     * @param topic
     * @param message
     * @param <T>
     */
    private static <T> void send(KafkaProducer<String, T> producer, String topic, T message) throws ExecutionException, InterruptedException {
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

    public static long getStartDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    public static long getEndDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    public static void mockJson() throws ExecutionException, InterruptedException {
        final KafkaProducer<String, Object> producer = buildProducer(brokerAddr, StringSerializer.class.getName());
        long i = getStartDateTime(new Date());
        int num = 0;
        while (i < getEndDateTime(new Date())) {
            String metricSetName = randomMetricSetName();
            String timestamp = String.valueOf(i);
            Map<String, String> dimensions = new HashMap<>();
            dimensions.put("appsystem", "dev_test");
            dimensions.put("hostname", "zorkdata70-1.host.com");
            dimensions.put("ip", "192.168.70.1");
            dimensions.put("servicename", "数据质量模块");
            dimensions.put("clustername", "基础监控");
            dimensions.put("appprogramname", "数据质量模块");

            Map<String, Double> metrics = new HashMap<>();

            metrics.put("user_pct", randomUserPct());

            Map<String, Object> map = new HashMap<>();
            map.put("metricsetname", metricSetName);
            map.put("timestamp", timestamp);
            map.put("dimensions", dimensions);
            map.put("metrics", metrics);
            final String message = JSON.toString(map);
            System.out.println(message);

            send(producer, topic, message);
            num++;
            i += 10000;
        }
        if (null != producer) {
            producer.close(2000, TimeUnit.MILLISECONDS);
            Thread.sleep(2000);
        }
        System.out.println("一共 " + num + " 条数据");
    }

    public static void mockJsonWithIAndMetricSetName(long startTime, long endTime, String ipPrefix, String metricSetName) throws ExecutionException, InterruptedException {
        if (producer == null) {
            producer = buildProducer(brokerAddr, StringSerializer.class.getName());
        }
        long i = startTime;
        int num = 0;
        while (i < endTime) {
            //String metricSetName = randomMetricSetName();
            String timestamp = String.valueOf(i);
            Map<String, String> dimensions = new HashMap<>();
            dimensions.put("appsystem", "dev_test");
            dimensions.put("hostname", "zorkdata70-" + ipPrefix + ".host.com");
            dimensions.put("ip", "192.168.70." + ipPrefix);
            dimensions.put("servicename", "数据质量模块");
            dimensions.put("clustername", "基础监控");
            dimensions.put("appprogramname", "数据质量模块");

            Map<String, Double> metrics = new HashMap<>();

            metrics.put("user_pct", randomUserPct());

            Map<String, Object> map = new HashMap<>();
            map.put("metricsetname", metricSetName);
            map.put("timestamp", timestamp);
            map.put("dimensions", dimensions);
            map.put("metrics", metrics);
            final String message = JSON.toString(map);
             System.out.println(message);
            send(producer, topic, message);
            Thread.sleep(2000);
            num++;
            i += 10000;
        }
        System.out.println("[metricSetName = " + metricSetName + " ,ipPrefix = " + ipPrefix + "]一共 " + num + " 条数据");
    }


    public static void buildMessage() throws ExecutionException, InterruptedException {
        String metricSetName = "mock_core_system_mb";
        buildMessage(metricSetName);
    }

    public static void buildMessage(String metricSetName) throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            final long startDateTime = getStartDateTime(new Date());
            final long endDateTime = getEndDateTime(new Date());
            final String ipPrefix = String.valueOf(i);
            mockJsonWithIAndMetricSetName(startDateTime, endDateTime, ipPrefix, metricSetName);
            System.out.println("模拟 [metricSetName = " + metricSetName + " ,ipPrefix = " + ipPrefix + " , " + startDateTime + " -- " + endDateTime + "] 数据耗时: " + (System.currentTimeMillis() - start) + " ms");
        }

    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        for (String metricSetName : METRIC_SET_NAMES) {
            buildMessage(metricSetName);
        }
        System.out.println("模拟数据总耗时: " + (System.currentTimeMillis() - start) + " ms.");
    }
}
