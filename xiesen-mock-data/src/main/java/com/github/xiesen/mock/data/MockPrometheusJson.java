package com.github.xiesen.mock.data;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

/**
 * {"labels":{"__name__":"prometheus_tsdb_time_retentions_total","instance":"192.168.1.92:9090","job":"prometheus"},"name":"prometheus_tsdb_time_retentions_total","timestamp":"2022-10-18T01:41:26Z","value":"0"}*
 *
 * @author xiese
 * @Description 模拟 json 数据
 * @Email xiesen310@163.com
 * @Date 2020/6/28 10:08
 */
public class MockPrometheusJson {
    private static String topic = "test-prometheus";
    private static String brokerAddr = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";
    private static ProducerRecord<String, String> producerRecord = null;
    private static KafkaProducer<String, String> producer = null;

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

    private static List<String> ips = Arrays.asList("10.228.24.186", "192.168.80.52", "10.180.129.83", "10.180.47.72", "10.180.41.142",
            "10.180.203.109", "10.180.9.19", "10.180.104.18", "10.180.129.250", "10.180.57.71");

    private static List<String> names = Arrays.asList("prometheus_tsdb_time_retentions_total", "com_gmas_statistic_total_gauge_of_interface", "container_network_receive_bytes_total_irate5m");

    /**
     * 模拟消息
     *
     * @return
     */
    public static String buildMsg() {
        JSONObject jsonObject = new JSONObject();
        JSONObject labels = new JSONObject();
        String name = RandomUtil.randomEle(names);
        labels.put("__name__", name);
        labels.put("instance", RandomUtil.randomEle(ips) + ":9090");
        labels.put("job", "prometheus");

        jsonObject.put("labels", labels);
        jsonObject.put("name", name);
        jsonObject.put("timestamp", prometheusFormatTimestamp());
        jsonObject.put("value", String.valueOf(RandomUtil.randomInt(0, 5)));
        return jsonObject.toString();
    }

    public static String prometheusFormatTimestamp() {
        return DateFormatUtils.formatUTC(DateUtil.date(), "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    /**
     * 发送数据
     *
     * @param topic
     */
    public static void send(String topic) {
        init();
        String req = buildMsg();
        System.out.println(req);
        producerRecord = new ProducerRecord<String, String>(
                topic,
                null,
                req
        );
        producer.send(producerRecord);
    }

    /**
     * 主函数
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i <= 1000; i++) {
            send(topic);
            Thread.sleep(2000);
        }
    }
}
