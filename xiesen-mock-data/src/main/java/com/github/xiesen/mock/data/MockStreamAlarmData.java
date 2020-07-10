package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟 stream 告警数据
 * @Email xiesen310@163.com
 * @Date 2020/7/10 14:39
 */
public class MockStreamAlarmData {
    public static String buildAlarmJson() {
        Random random = new Random();
        int i = random.nextInt(10);
        JSONObject alarmJson = new JSONObject();
        alarmJson.put("alarmTypeName", "alarm_metric");
        alarmJson.put("expressionId", i);
        alarmJson.put("metricSetName", "cpu_system_metricbeat");
        alarmJson.put("severity", i * 3);
        alarmJson.put("status", "PROBLEM");
        alarmJson.put("timestamp", DateUtil.getUTCTimeStr());
        String extFields = "{\n" +
                "        \"uuid\":\"2a094fd38e894de485ae09820bf5a08c\",\n" +
                "        \"sourSystem\":\"1\",\n" +
                "        \"actionID\":\"0\",\n" +
                "        \"mergeTag\":\"1\",\n" +
                "        \"connectId\":\"2a094fd38e894de485ae09820bf5a08c\",\n" +
                "        \"eventNum\":\"2\",\n" +
                "        \"alarmSuppress\":\"alarmSuppress\",\n" +
                "        \"alarmWay\":\"2,2,2\",\n" +
                "        \"successFlag\":\"1\",\n" +
                "        \"expressionId\":\"2\",\n" +
                "        \"alarmtime\":\"2020-07-08T20:02:00.000+08:00\",\n" +
                "        \"calenderId\":\"1\",\n" +
                "        \"reciTime\":\"1594209785705\",\n" +
                "        \"alarmDetailType\":\"1\",\n" +
                "        \"revUsers\":\"[]\"\n" +
                "    }";
        String searchSentence = "SELECT mean(\"cores\") AS value  FROM cpu_system_metricbeat WHERE ( \"hostname\" =~ /\\.*/ ) AND ( \"ip\" =~ /\\.*/ ) AND ( \"appsystem\" = 'dev_test') AND time >= 1594209600000ms AND time < 1594209720000ms GROUP BY time(1m),\"hostname\",\"ip\",\"appsystem\" fill(null)";
        JSONObject extFieldsJson = JSONObject.parseObject(extFields);
        extFieldsJson.put("searchSentence", searchSentence);
        alarmJson.put("extFields", extFieldsJson);
        Map<String, Object> sourceMap = new HashMap<>(4);
        sourceMap.put("hostname", "zorkdata" + i);
        sourceMap.put("ip", "192.168.1." + i);
        sourceMap.put("sourSystem", i);
        sourceMap.put("appsystem", "tdx");
        alarmJson.put("sources", sourceMap);
        String title = "192.168.1." + i + " 指标告警";
        alarmJson.put("title", title);
        if (alarmJson.toJSONString().getBytes().length < 1024) {
            int content = 1011 - alarmJson.toJSONString().getBytes().length;
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < content; j++) {
                str.append("a");
            }
            alarmJson.put("content", str.toString());
        }
        return alarmJson.toJSONString();
    }

    public static void main(String[] args) throws InterruptedException {
        long size = 10000000L * 1;
        for (int i = 0; i < size; i++) {
            String json = buildAlarmJson();
            System.out.println(json);
            CustomerProducer producer = ProducerPool.getInstance("config.properties").getProducer();
            producer.sendJsonLog(json);
        }
        Thread.sleep(1000);
    }
}
