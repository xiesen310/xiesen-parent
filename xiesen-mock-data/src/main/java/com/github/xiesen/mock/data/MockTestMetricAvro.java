package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;
import org.mortbay.util.ajax.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockTestMetricAvro {
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

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("hostname", "DVJTY4-WEB406");
        dimensions.put("appprogramname", "DVJTY4-WEB406_80");
        dimensions.put("appsystem", "dev_test");
        dimensions.put("ip", "192.168.1.1");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        measures.put("measure", 1.0);
        return measures;
    }


    public static void main(String[] args) throws Exception {
        long size = 100000L * 1;

        for (int i = 0; i < size; i++) {
            String metricSetName = "log2metric";
            String timestamp = DateUtil.getUTCTimeStr();

            Map<String, String> dimensions = getRandomDimensions();
            Map<String, Double> metrics = getRandomMeasures();
            Map<String, Object> map = new HashMap<>();
            map.put("metricSetName", metricSetName);
            map.put("timestamp", timestamp);
            map.put("dimensions", dimensions);
            map.put("metrics", metrics);
            System.out.println(JSON.toString(map));
            CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen-parent\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();

            producer.sendMetric(metricSetName, timestamp, dimensions, metrics);

//            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
