package com.github.xiesen.aviator.func;

import com.github.xiesen.common.utils.DateUtil;
import com.googlecode.aviator.AviatorEvaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestLogAviator {


    private static final String[] RESPONSE_CODES = {"200", "200", "404", "500"};

    private static Map<String, String> getRandomDimensions() {
        int num = new Random().nextInt(5);
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("hostname", "zork_" + num+".host.com");
        dimensions.put("appsystem", "dev_test");
        dimensions.put("ip", "192.168.1." + num);
        return dimensions;
    }

    /**
     * 获取随机响应状态码
     *
     * @return
     */
    private static String getRandomResponseCodes() {
        return RESPONSE_CODES[new Random().nextInt(RESPONSE_CODES.length)];
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
        normalFields.put("httpResponseCode", getRandomResponseCodes());
        return normalFields;
    }

    public static Boolean logCheck(String expression, Map<String, Object> data) {
        Boolean flag = (Boolean) AviatorEvaluator.execute(expression, data);
        return flag;
    }

    public static Map<String, Object> mockLogData() {
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
        return map;
    }

    public static void main(String[] args) {
//        AviatorEvaluator.addFunction(new MapsFunction());
        String expression = "logTypeName=='default_analysis_template' && dimensions.appsystem=='dev_test' && string.endsWith(source,'.log')";

        AviatorEvaluator.compile(expression);
        System.out.println(logCheck(expression, mockLogData()));
    }
}
