package com.github.xiesen.mock.test;

import java.util.*;

/**
 * @author xiesen
 */
public class AAA {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        final String s = map.get("servicecode");
        System.out.println(s);
        String indexType = null;
        if ("aa".equalsIgnoreCase(indexType)) {
            System.out.println("aa");
        } else {
            System.out.println("no aa");
        }

        List<String> list = Arrays.asList("valueMax", "seriesNum", "valueAvg", "valueMin", "valueSum", "valueLast");
        Collections.sort(list);
        list.forEach(System.out::println);
        System.out.println("==========dim============");

        List<String> dims = Arrays.asList("hostname", "appsystem", "sys_name", "ip", "tags", "srcMetricSetName", "field", "source");
        Collections.sort(dims);
        dims.forEach(System.out::println);

    }
}

