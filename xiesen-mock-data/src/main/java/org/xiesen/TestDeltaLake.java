package org.xiesen;

import com.github.xiesen.common.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiesen
 */
public class TestDeltaLake {
    private static final Pattern PATTERN = Pattern.compile("(.*)\\['(.*)']");

    private static ThreadLocal<SimpleDateFormat> sdf1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    private static ThreadLocal<SimpleDateFormat> utcSdf1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");
        }
    };

    public static String yyyymmddFormat(String utcDateStr) {
        Date date = null;
        try {
            date = utcSdf1.get().parse(utcDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf1.get().format(date);
    }

    public static String getPartitionWithDelta(Map<String, Object> mapData, String partitionFields) {
        StringBuilder builder = new StringBuilder();
        final String[] columns = partitionFields.split(";");
        for (String column : columns) {
            final String[] fieldList = column.split("=");
            String key = fieldList[0];
            String valueKey = fieldList[1];
            if ("timestamp".equals(valueKey)) {
                String timestamp = (String) mapData.get("timestamp");
                String yyyymmddFormat = yyyymmddFormat(timestamp);
                builder.append("/").append(key).append("=").append(yyyymmddFormat);

            } else if (valueKey.contains("[")) {
                //dimensions['appsystem']
                Matcher matcher = PATTERN.matcher(valueKey);
                if (matcher.find()) {
                    String realField = matcher.group(1);
                    String subField = matcher.group(2);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> subMap = (Map<String, Object>) mapData.get(realField);
                    Object subValue = subMap.get(subField);
                    builder.append("/").append(key).append("=").append(subValue.toString());
                } else {
                    throw new RuntimeException("指定的分区字段与数据不一致！");
                }
            } else {
                Object value = mapData.get(valueKey);
                builder.append("/").append(key).append("=").append(value.toString());
            }
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        Map<String, Object> mapData = mockData();
        String partitionFields = "logdate=timestamp;appsystem=dimensions['appsystem'];logname=logTypeName";
        System.out.println(partitionFields);
        System.out.println(getPartitionWithDelta(mapData, partitionFields));
    }


    private static Map<String, Object> mockData() {
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
        return map;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(16);
        normalFields.put("indexTime", DateUtil.getUTCTimeStr());
        normalFields.put("method", "GET");
        normalFields.put("full_url", "/gsnews/gsf10/capital/main/1.0");
        normalFields.put("RequestMethod", "");
        normalFields.put("IP", "183.95.248.189");
        normalFields.put("logstash_deal_name", "logstash-0");
        normalFields.put("logchecktime", DateUtil.getUTCTimeStr());
        normalFields.put("message", "183.95.248.189 - - [23/Jul/2020:08:26:32 +0800] \"GET " +
                "/gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ" +
                "%3D%3D HTTP/1.1\" 200 872 ");
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

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        measures.put("functionnum", 1.0);
        return measures;
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
        dimensions.put("browser", "nil");
        dimensions.put("appsystem", "JJR");
        dimensions.put("servicename", "金太阳4接入服务nginx");
        dimensions.put("url", "/gsnews/gsf10/capital/main/");
        return dimensions;
    }


}
