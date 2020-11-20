package com.github.xiesen.kafka08.mock;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author 谢森
 * @Description 模拟告警发送数据
 * @Email xiesen310@163.com
 * @Date 2020/11/9 10:50
 */
public class MockAlarmSendMsg {
    private static Producer producer;

    public static Producer createProducer(String zkConnect, String brokerList) {
        if (producer == null) {
            Properties properties = new Properties();
            properties.put("bootstrap.servers", brokerList);
            properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("batch.size", "1");
            producer = new KafkaProducer<>(properties);
        }
        return producer;
    }


    public static void main(String args[]) throws InterruptedException {
        Producer producer = createProducer("kafka-1:2181/kafka082,kafka-2:2181/kafka082,kafka-3:2181/kafka082",
                "kafka-1:9092,kafka-2:9092,kafka-3:9092");

        HashMap<String, Object> map = new HashMap<>();
        String topicName = "xiesen_alarm";
        String alarmTypeName = "alarm_metric";
        String timestamp = getUTCTimeStr();
        String expressionId = "7981";
        String strutName = "cpu_system_metricbeat";
        int severity = 5;
        String status = "PROBLEM";
        String title = "192.168.70.10 指标告警";
        String content = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        Map<String, Object> sources = new HashMap<>();
        sources.put("appsystem", "dev_test");
        sources.put("sourSystem", "1");
        sources.put("ip", "192.168.70.10");
        Map<String, Object> extFields = new HashMap<>();
        extFields.put("searchSentence", "SELECT mean(\"cores\") AS value  FROM cpu_system_metricbeat WHERE ( " +
                "\"hostname\" =~ /\\.*/ ) AND ( \"ip\" =~ /\\.*/ ) AND ( \"appsystem\" = 'dev_test') AND time >= " +
                "1594209600000ms AND time < 1594209720000ms GROUP BY time(1m),\"hostname\",\"ip\",\"appsystem\" fill" +
                "(null)");
        extFields.put("alarmWay", "1,1,1");
        extFields.put("successFlag", "1");
        extFields.put("expressionId", "7981");
        extFields.put("sourSystem", "1");
        extFields.put("alarmtime", getUTCTimeStr());
        extFields.put("revUsers", "[{\"email\":\"xiesen@zork.com.cn\",\"phone\":\"18503816310\"," +
                "\"telphone\":\"18503816310\",\"truename\":\"谢森\",\"userid\":5,\"wechatid\":\"XieSen\"}]");
        extFields.put("alarmDetailType", "1");
        map.put("alarmTypeName", alarmTypeName);
        map.put("timestamp", timestamp);
        map.put("severity", severity);
        map.put("expressionId", expressionId);
        map.put("metricSetName", strutName);
        map.put("status", status);
        map.put("title", title);
        map.put("content", content);
        map.put("extFields", extFields);
        int num = 1;
        while (num <= 100) {
            sources.put("hostname", "zorkdata001");
            map.put("sources", sources);
            String s = JSON.toJSONString(map);
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, s);
            System.out.println(s);
            producer.send(producerRecord);
            num++;
            Thread.sleep(1000);
        }
        producer.close();

    }

    public static String getUTCTimeStr() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");
        return format.format(new Date()).toString();
    }
}
