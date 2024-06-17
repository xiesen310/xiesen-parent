package com.github.xiesen.aviator;

import cn.hutool.core.util.RandomUtil;
import com.github.xiesen.aviator.func.ListContainsFunction;
import com.github.xiesen.aviator.func.MapContainsFunction;
import com.github.xiesen.aviator.func.StrContainsFunction;
import com.googlecode.aviator.AviatorEvaluator;

import java.util.*;

/**
 * @author xiesen
 * @title: TestAviator
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/26 18:49
 */
public class LogAviator {
    public static final Map<String, Object> ipsMap = new HashMap();
    public static String ips = "";
    public static final List<String> ipsList = new ArrayList<>();
    public static final int ipNums = 500;

    static {
        for (int i = 1; i < ipNums; i++) {
            ipsMap.put("192.168.60." + i, "");
        }
        ipsMap.put("192.168.70.2", "");

        for (int i = 1; i < ipNums; i++) {
            ipsList.add("192.168.60." + i);
        }
        ipsList.add("192.168.70.2");

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < ipNums; i++) {
            builder.append("192.168.60." + i).append(",");
        }
        builder.append("192.168.70.2");
        ips = builder.toString();
        System.out.println();
    }


    public static Boolean logCheck(String expression, Map<String, Object> data) {
        Boolean flag = (Boolean) AviatorEvaluator.execute(expression, data);
        return flag;
    }

    public static Map<String, Object> mockLogData() {
        Map<String, Object> data = new HashMap<>();
        data.put("logTypeName", "default_log_parse");
        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("ip", "192.168.70.2");
        dimensions.put("appsystem", "bigdata");
        data.put("dimensions", dimensions);
        Map<String, String> normalFields = new HashMap<>();
        dimensions.put("message", "/Library/Java/JavaVirtualMachines/jdk-1.8.jdk/Contents/Home/bin/java");

        data.put("timestamp", System.currentTimeMillis() + "");
        data.put("source", "/var/log/nginx.log");
        data.put("offset", String.valueOf(RandomUtil.randomLong()));
        data.put("dimensions", dimensions);
        data.put("measures", new HashMap<String, Double>());
        data.put("normalFields", normalFields);
        return data;
    }


    public static void main(String[] args) {
//        method1();
//        method2();
//        method3();
//        method4();
        method5();
    }

    public static void method5() {
        String expression = "string.contains(ips,dimensions.ip)";
        AviatorEvaluator.compile(expression);
        Map<String, Object> logData = mockLogData();
        logData.put("ips", ips);
        System.out.println(logCheck(expression, logData));
    }

    public static void method4() {
        AviatorEvaluator.addFunction(new ListContainsFunction(ipsList));
        String expression = "list.contains(dimensions.ip)";
        AviatorEvaluator.compile(expression);
        System.out.println(logCheck(expression, mockLogData()));
    }

    public static void method3() {
        AviatorEvaluator.addFunction(new StrContainsFunction(ips));
        String expression = "str.contains(dimensions.ip)";
        AviatorEvaluator.compile(expression);
        System.out.println(logCheck(expression, mockLogData()));
    }

    public static void method2() {
        AviatorEvaluator.addFunction(new MapContainsFunction(ipsMap));
        String expression = "map.contains(dimensions.ip)";
        AviatorEvaluator.compile(expression);
        System.out.println(logCheck(expression, mockLogData()));
    }

    public static void method1() {
        String expression = "dimensions.ip=='192.168.70.2'";
        AviatorEvaluator.compile(expression);
        System.out.println(logCheck(expression, mockLogData()));
    }
}
