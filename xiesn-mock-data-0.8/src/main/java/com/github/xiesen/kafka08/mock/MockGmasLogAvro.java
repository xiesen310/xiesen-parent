package com.github.xiesen.kafka08.mock;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.kafka08.util.CustomerProducer08;
import com.github.xiesen.kafka08.util.ProducerPool;
import org.mortbay.util.ajax.JSON;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author xiese
 * @Description 模拟 gmas 日志数据
 * @Email xiesen310@163.com
 * @Date 2020/7/23 20:29
 */
@SuppressWarnings("all")
public class MockGmasLogAvro {
    public static void printMsg(String logTypeName, String timestamp, String source, String offset,
                                Map<String, String> dimensions, Map<String, Double> measures,
                                Map<String, String> normalFields) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logTypeName", logTypeName);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("source", source);
        jsonObject.put("offset", offset);
        jsonObject.put("dimensions", dimensions);
        jsonObject.put("measures", measures);
        jsonObject.put("normalFields", normalFields);
    }

    private static Map<String, String> getDimensions() {
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("appprogramname", "ydlog");
        dimensions.put("hostname", "gmt_log");
        dimensions.put("appsystem", "JHSystem");
        return dimensions;
    }

    private static Double randomDouble() {
        double a = Math.random();
        DecimalFormat df = new DecimalFormat("0.000");
        String str = df.format(a);
        Double result = Double.valueOf(str);
        return result;
    }

    private static String[] RESPONSE_CODES = {
            "200", "200", "200", "200", "304", "304", "304", "500", "400", "502"
    };

    private static String[] LOG_NODE = {
            "1", "2", "3", "4"
    };

    private static String randomResponseCode() {
        return RESPONSE_CODES[new Random(RESPONSE_CODES.length).nextInt(RESPONSE_CODES.length)];
    }

    private static String randomlogNode() {
        return LOG_NODE[new Random(LOG_NODE.length).nextInt(LOG_NODE.length)];
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(16);
        normalFields.put("msgType", "bar");
        normalFields.put("msgTime", cn.hutool.core.date.DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"));
        JSONObject msgBody = new JSONObject();
        msgBody.put("dealFlag", 0);
        msgBody.put("exchange", "gmas_v2_0@10.180.230.86_10.180.225.6700000000000000000000000");
        msgBody.put("funCode", "4112");
        msgBody.put("keyValue", "null");
        msgBody.put("logNode", "2");
        msgBody.put("msgId", "1234");
        msgBody.put("msgProperty", "2");
        msgBody.put("queueName", "");
        msgBody.put("remark", "0000022500047B6A20994A27");
        msgBody.put("rmqNode", "10.180.225.67");
        msgBody.put("routekey", "routekey");

        normalFields.put("msgBody", com.alibaba.fastjson.JSON.toJSONString(msgBody));
        normalFields.put("serviceName", "name=[ens192] ip=[10.180.227.232];nodeName =3");
        normalFields.put("type", "business");
        normalFields.put("message", "aaaaaaaaaaaaaaaaaaaaaaaaaaa");
        return normalFields;
    }


    public static void main(String[] args) throws Exception {
        long size = 10000000L * 1;
//        String topicName = "stream_guosen_log_data";
        String topicName = "xiesen";
        for (int i = 0; i < size; i++) {
            String logTypeName = "yd_jstorm_kafka_log";
            String timestamp = DateUtil.getUTCTimeStr();
            String source = "kafka";
            String offset = "0";

            Map<String, String> dimensions = getDimensions();
            Map<String, Double> measures = new HashMap<>();
            Map<String, String> normalFields = getRandomNormalFields();
            Map<String, Object> map = new HashMap<>();
            map.put("logTypeName", logTypeName);
            map.put("timestamp", timestamp);
            map.put("source", source);
            map.put("offset", offset);
            map.put("dimensions", dimensions);
            map.put("measures", measures);
            map.put("normalFields", normalFields);
            System.out.println(JSON.toString(map));
            CustomerProducer08 producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen\\xiesen-parent" +
                    "\\xiesn-mock-data-0.8\\src\\main\\resources\\config.properties").getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);

            Thread.sleep(10000);
        }
        Thread.sleep(1000);
    }
}
