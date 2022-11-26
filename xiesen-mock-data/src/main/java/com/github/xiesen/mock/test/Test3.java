package com.github.xiesen.mock.test;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import com.github.xiesen.common.utils.DateUtil;
import lombok.val;
import org.apache.hadoop.util.hash.Hash;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiesen
 * @title: Test3
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/19 18:48
 */
public class Test3 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            long start = getNanoTime();
            mockData();
            long end = getNanoTime();
            System.out.println("耗时: " + (end - start) + " ns");
        }

    }

    public static Map<String, Object> mockData() {
        Map<String, Object> map = new HashMap<>();
        String logTypeName = "default_analysis_template";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/var/log/test/access.log";
        String offset = "351870827";

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

    public static Long getNanoTime() {
        /// 纳秒
        long current = System.currentTimeMillis() * 1000 * 1000;
        /// 纳秒
        long nanoTime = System.nanoTime();
        return current + (nanoTime - nanoTime / 1000000 * 1000000);
    }

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("clustername", "集群");
        dimensions.put("hostname", "zork-rd-dev-7092");
        dimensions.put("appprogramname", "模块");
        dimensions.put("appsystem", "poctest");
        dimensions.put("ip", "192.168.70.92");
        dimensions.put("servicecode", "模块");
        dimensions.put("servicename", "模块");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        Random random = new Random();
        int i = random.nextInt(90) + 5;
        measures.put("lags", Double.valueOf(i));
//        measures.put("memory_used",0.9);
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

}
