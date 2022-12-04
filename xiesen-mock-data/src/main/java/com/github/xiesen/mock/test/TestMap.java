package com.github.xiesen.mock.test;

import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.mortbay.util.ajax.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xiesen
 * @title: TestMap
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/12/1 15:38
 */
public class TestMap {
    private static Map<String, String> getRandomDimensions() {
        Map<String, String> dimensions = new HashMap<>(7);
        dimensions.put("clustername", "集群");
        dimensions.put("hostname", "zork-rd-dev-7092");
        dimensions.put("appprogramname", "模块");
        dimensions.put("appsystem", "poctest");
        dimensions.put("ip", "192.168.70.92");
        dimensions.put("servicecode", "模块");
        dimensions.put("servicename", "模块");
        return dimensions;
    }

    public static void main(String[] args) {
        Map<String, Object> bigMap = new HashMap<>();
        bigMap.put("dimensions", getRandomDimensions());
        bigMap.put("logTypeName", "default_analysis_template");

        Map<String, Object> target = new HashMap<>();

        flatten(bigMap, target, "");
        target.forEach((k, v) -> System.out.println(k + " : " + v));
    }

    public static void flatten(Map<String, ?> src, Map<String, Object> target, String path) {
        src.forEach((k, v) -> {
            final String s = path.equalsIgnoreCase("") ? k : path + "." + k;
            if (v instanceof Map) {
                flatten((Map) v, target, s);
            } else {
                target.put(s, v);
            }
        });
    }


}
