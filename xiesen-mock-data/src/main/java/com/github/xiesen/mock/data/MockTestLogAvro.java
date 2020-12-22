package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;
import org.mortbay.util.ajax.JSON;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockTestLogAvro {
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
        dimensions.put("servicecode", "WEB");
        dimensions.put("clustername", "nginx");
        dimensions.put("appsystem", "tdx");
        dimensions.put("servicename", "nginx");
        dimensions.put("ip", "192.168.1.1");
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

    private static String randomResponseCode() {
        return RESPONSE_CODES[new Random(RESPONSE_CODES.length).nextInt(RESPONSE_CODES.length)];
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("message", "183.95.248.189 - - [23/Jul/2020:08:26:32 +0800] \"GET " +
                "/gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ" +
                "%3D%3D HTTP/1.1\" 200 872 ");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        return normalFields;
    }


    public static void main(String[] args) throws Exception {
        long size = 10000000L * 1;
        String topicName = "xiesen_dwd_default_log";
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
            System.out.println(JSON.toString(map));
            CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen\\xiesen-parent" +
                    "\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);

            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
