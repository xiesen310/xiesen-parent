package com.github.xiesen.common.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;

public class MockDataUtil {
    private static Map<String, String> getRandomDimensions() {
        Map<String, String> dimensions = new HashMap<>(8);
        dimensions.put("appprogramname", "组件");
        dimensions.put("clustername", "模块");
        dimensions.put("servicename", "组件");
        dimensions.put("servicecode", "组件");
        if (RandomUtil.randomBoolean()) {
            dimensions.put("appsystem", "dev");
            dimensions.put("hostname", "11");
            dimensions.put("ip", "192.168.2.68");
        } else {
            dimensions.put("appsystem", "dev");
            dimensions.put("hostname", "zork-rd-dev-7064");
            dimensions.put("ip", "192.168.70.64");
        }

        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(1);
        Random random = new Random();
        int i = random.nextInt(90) + 5;
        measures.put("lags", Double.valueOf(i));
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("message", "183.95.248.189 - - [23/Jul/2020:08:26:32 +0800] \"GET " +
                "/gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ" +
                "%3D%3D HTTP/1.1\" 200 872 error ERROR");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        if (RandomUtil.randomBoolean()) {
            normalFields.put("nullKey", "1");
        }else {
            normalFields.put("nullKey", "");
        }
        return normalFields;
    }

    private static Map<String, Object> mockLogData(String logTypeName) {
        Map<String, Object> map = new HashMap<>();
        if (StrUtil.isBlank(logTypeName)) {
            logTypeName = "default_analysis_template";
        }
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/var/log/test/access.log";
        String offset = RandomUtil.randomLong(1, 999999999) + "";

        Map<String, String> dimensions = getRandomDimensions();
        Map<String, Double> measures = getRandomMeasures();
        Map<String, String> normalFields = getRandomNormalFields();

        map.put("logTypeName", logTypeName);
        map.put("timestamp", timestamp);
        map.put("source", source);
        map.put("offset", offset);
        map.put("dimensions", dimensions);
        map.put("measures", measures);
        map.put("normalFields", normalFields);
        return map;
    }

    public static List<Map<String, Object>> mockLogDataList(String logTypeName, int num) {
        List<Map<String, Object>> list = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            list.add(mockLogData(logTypeName));
        }
        return list;
    }

}
