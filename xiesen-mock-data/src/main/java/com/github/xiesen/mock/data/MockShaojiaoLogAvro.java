package com.github.xiesen.mock.data;

import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockShaojiaoLogAvro {

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("hostname", "DVJTY4-WEB406-shaojiao12");
        dimensions.put("appprogramname", "DVJTY4-WEB406_80");
        dimensions.put("servicecode", "WEB");
        dimensions.put("clustername", "nginx");
        dimensions.put("appsystem", "dev_test");
        dimensions.put("servicename", "nginx");
        dimensions.put("ip", "192.168.1.1");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        measures.put("cpu_used",0.8);
        measures.put("memory_used",0.9);
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("message", "183.95.248.189 - - [23/Jul/2020:08:26:32 +0800] \"GET " +
                "/gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ" +
                "%3D%3D HTTP/1.1\" 200 872 ");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
//        normalFields.put("collecttime", "abc");
        return normalFields;
    }


    public static void main(String[] args) throws Exception {
        long size = 100000000L * 1;
        for (int i = 0; i < size; i++) {
            String logTypeName = "default_analysis_template";
            String timestamp = DateUtil.getUTCTimeStr();
            String source = "/var/log/nginx/access.log";
            String offset = "351870827";

            Map<String, String> dimensions = getRandomDimensions();
            Map<String, Double> measures = getRandomMeasures();
            Map<String, String> normalFields = getRandomNormalFields();
            Map<String, Object> map = new HashMap<>();
            map.put("logTypeName", logTypeName);
            map.put("timestamp", timestamp);
            map.put("source", source);
            map.put("offset", offset);
            map.put("dimensions", dimensions);
            map.put("measures", measures);
            map.put("normalFields", normalFields);
//            System.out.println(JSON.toString(map));
            CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen\\xiesen-parent" +
                    "\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);

//            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
