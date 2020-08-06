package com.github.xiesen.kafka08.mock;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.kafka08.util.CustomerProducer08;
import com.github.xiesen.kafka08.util.ProducerPool;
import org.mortbay.util.ajax.JSON;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/7/23 20:29
 */
public class MockGuoSenLogAvro {
    public static void printMsg(String logTypeName, String timestamp, String source, String offset,
                                Map<String, String> dimensions, Map<String, Double> measures, Map<String, String> normalFields) {
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
        dimensions.put("url1", "/gsnews/");
        dimensions.put("clustername", "东莞");
        dimensions.put("url2", "/gsnews/gsf10/");
        dimensions.put("httpresponsecode", randomResponseCode());
        dimensions.put("browser", "nil");
        dimensions.put("appsystem", "JJR");
        dimensions.put("servicename", "金太阳4接入服务nginx");
        dimensions.put("url", "/gsnews/gsf10/capital/main/");
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
        Random random = new Random();
        Map<String, Double> measures = new HashMap<>(4);
        measures.put("functionnum", 1.0);
        measures.put("alltime", randomDouble());
        measures.put("bgtime", 1.0);
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(4);
        normalFields.put("indexTime", DateUtil.getUTCTimeStr());
        normalFields.put("method", "GET");
        normalFields.put("full_url", "/gsnews/gsf10/capital/main/1.0");
        normalFields.put("RequestMethod", "");
        normalFields.put("IP", "183.95.248.189");
        normalFields.put("logstash_deal_name", "logstash-0");
        normalFields.put("logchecktime", DateUtil.getUTCTimeStr());
        normalFields.put("message", "183.95.248.189 - - [23/Jul/2020:08:26:32 +0800] \"GET /gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ%3D%3D HTTP/1.1\" 200 872 ");
        normalFields.put("res_url", "http://goldsundg.guosen.com.cn:1445/asset/webF10/dist/pages/index.html");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        normalFields.put("logcheckip", "10.33.209.53");
        normalFields.put("GoldSumTime", "");
        normalFields.put("deserializerTime", DateUtil.getUTCTimeStr());
        normalFields.put("target_ip", "10.33.124.240");
        normalFields.put("logstash_deal_ip", "10.33.196.130");
        normalFields.put("DevPath", "[\"Thanos-WebEngine-Android\"]");
        return normalFields;
    }


    public static void main(String[] args) throws Exception {
        long size = 10000000L * 1;
//        String topicName = "stream_guosen_log_data";
        String topicName = "xiesen";
        for (int i = 0; i < size; i++) {
            String logTypeName = "nginx_access_filebeat";
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
            CustomerProducer08 producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen\\xiesen-parent\\xiesn-mock-data-0.8\\src\\main\\resources\\config.properties").getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);

            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
