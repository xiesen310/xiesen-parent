package com.github.xiesen.jmeter.mock;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.jmeter.util.Producer;
import org.apache.jmeter.samplers.SampleResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author xiese
 * @Description 模拟 streamx 告警数据
 * @Email xiesen310@163.com
 * @Date 2020/8/18 10:57
 */
public class StreamAlarmData {
    private static String buildAlarmJson() {
        Random random = new Random();
        int i = random.nextInt(10);
        JSONObject alarmJson = new JSONObject();
        alarmJson.put("alarmTypeName", "alarm_metric");
        alarmJson.put("expressionId", i);
        alarmJson.put("metricSetName", "cpu_system_metricbeat");
        alarmJson.put("severity", i * 3);
        alarmJson.put("status", "PROBLEM");
        alarmJson.put("timestamp", DateUtil.getUTCTimeStr());
        String searchSentence = "SELECT mean(\"cores\") AS value  FROM cpu_system_metricbeat WHERE ( \"hostname\" =~ " +
                "/\\.*/ ) AND ( \"ip\" =~ /\\.*/ ) AND ( \"appsystem\" = 'dev_test') AND time >= 1594209600000ms AND " +
                "time < 1594209720000ms GROUP BY time(1m),\"hostname\",\"ip\",\"appsystem\" fill(null)";
        JSONObject extFieldsJson = new JSONObject();
        extFieldsJson.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
        extFieldsJson.put("sourSystem", String.valueOf(new Random().nextInt(10000)));
        extFieldsJson.put("actionID", String.valueOf(new Random().nextInt(10000)));
        extFieldsJson.put("mergeTag", String.valueOf(new Random().nextInt(10000)));
        extFieldsJson.put("connectId", UUID.randomUUID().toString().replaceAll("-", ""));
        extFieldsJson.put("eventNum", String.valueOf(new Random().nextInt(10000)));
        extFieldsJson.put("alarmSuppress", "alarmSuppress");
        extFieldsJson.put("alarmWay", "2,2,2");
        extFieldsJson.put("successFlag", "1");
        extFieldsJson.put("expressionId", new Random().nextInt(100000));

        extFieldsJson.put("alarmtime", DateUtil.getUTCTimeStr());
        extFieldsJson.put("calenderId", "1");
        extFieldsJson.put("reciTime", System.currentTimeMillis());
        extFieldsJson.put("alarmDetailType", "1");
        extFieldsJson.put("revUsers", "[]");

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

    /**
     * 模拟 alarm 数据
     *
     * @param results
     * @param producer
     * @param topicName
     */
    public static void buildAlarmData(SampleResult results, Producer producer, String topicName) {
        producer.sendJson(topicName, buildAlarmJson());
        results.setResponseCode("0");
        results.setResponseData(buildAlarmJson(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);
    }

    public static void main(String[] args) {
        System.out.println(buildAlarmJson());
    }

}
