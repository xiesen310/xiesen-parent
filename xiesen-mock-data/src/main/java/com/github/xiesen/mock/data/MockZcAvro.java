package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockZcAvro {

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");

    private static Map<String, String> getDimensions() {
        Map<String, String> dimensions = new HashMap<>(7);
        dimensions.put("appsystem", "poctest");
        dimensions.put("hostname", "ostemplate");
        dimensions.put("appprogramname", "模块");
        dimensions.put("ip", "192.168.70.98");
        dimensions.put("servicename", "模块");
        dimensions.put("servicecode", "模块");
        dimensions.put("clustername", "模块");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        return measures;
    }

    private static Map<String, String> getErrorNormalFieldsNoAppSystem() {
        Map<String, String> normalFields = new HashMap<>(6);

        normalFields.put("message", "你的名字叫什么啊");
        normalFields.put("collecttime", "1");
        normalFields.put("collecttime_2", "3");
        normalFields.put("logstash_deal_ip", "4");
        normalFields.put("logstash_deal_name", "5");
        normalFields.put("source1", "6");
        return normalFields;
    }

    public static void main(String[] args) throws Exception {
        long size = 100000L * 1;
        for (int i = 0; i < size; i++) {
            String logTypeName = "default_analysis_template_zhang";
            String timestamp = format.format(new Date()).toString();
            String source = "/var/log/zhangchang.log";
            // String offset = String.valueOf(new Random().nextInt(100000));
            String offset = String.valueOf(1082);

            Map<String, String> dimensions = getDimensions();

            Map<String, Double> measures = getRandomMeasures();
            Map<String, String> normalFields = getErrorNormalFieldsNoAppSystem();

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("logTypeName", logTypeName);
            map.put("timestamp", timestamp);
            map.put("source", source);
            map.put("offset", offset);
            map.put("dimensions", dimensions);
            map.put("measures", measures);
            map.put("normalFields", normalFields);
            CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen-parent\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);
//            System.out.println(JSON.toJSONString(map));
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
