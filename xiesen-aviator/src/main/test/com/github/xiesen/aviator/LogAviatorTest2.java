package com.github.xiesen.aviator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.xiesen.aviator.func.ListContainsFunction;
import com.github.xiesen.aviator.func.MapContainsFunction;
import com.github.xiesen.aviator.func.StrContainsFunction;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogAviatorTest2 {
    /**
     * 模拟构建日志格式数据
     *
     * @return {@link  Map}
     */
    private static Map<String, Object> mockLogData() {
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

    private static Map<String, Object> logData;
    private static Map<String, Object> ipsMap = new HashMap();
    private static String ips = "";
    private static final List<String> ipsList = new ArrayList<>();
    private static final int ipNums = 100;

    /**
     * 日志规则表达式判断
     *
     * @param expression 规则表达式
     * @param data       规则数据
     * @return
     */
    private static Boolean logCheck(String expression, Map<String, Object> data) {
        Boolean flag = false;
        if (StrUtil.isNotEmpty(expression)) {
            flag = (Boolean) AviatorEvaluator.execute(expression, data);
        }
        return flag;
    }

    private static Boolean logCheck(Expression compile, Map<String, Object> data) {
        Boolean flag = false;
        if (ObjectUtil.isNotNull(compile)) {
            flag = (Boolean) compile.execute(data);
        }
        return flag;
    }

    private static String expression1;
    private static String expression2;
    private static String expression3;
    private static String expression4;
    private static String expression5;

    private static Expression compile1;
    private static Expression compile2;
    private static Expression compile3;
    private static Expression compile4;
    private static Expression compile5;

    @BeforeAll
    static void init() {
        initData();
        initExpression();
    }

    /**
     * 初始化规则表达式
     */
    private static void initExpression() {
        expression1 = "dimensions.ip=='192.168.70.2'";
        compile1 = AviatorEvaluator.compile(expression1);

        AviatorEvaluator.addFunction(new MapContainsFunction(ipsMap));
        expression2 = "map.contains(dimensions.ip)";
        compile2 = AviatorEvaluator.compile(expression2);

        AviatorEvaluator.addFunction(new StrContainsFunction(ips));
        expression3 = "str.contains(dimensions.ip)";
        compile3 = AviatorEvaluator.compile(expression3);

        AviatorEvaluator.addFunction(new ListContainsFunction(ipsList));
        expression4 = "list.contains(dimensions.ip)";
        compile4 = AviatorEvaluator.compile(expression4);

        expression5 = "string.contains(ips,dimensions.ip)";
        compile5 = AviatorEvaluator.compile(expression5);
    }

    /**
     * 初始化构造数据
     */
    private static void initData() {
        logData = mockLogData();
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
    }

    @JunitPerfConfig(duration = 10000, warmUp = 10, threads = 1)
    @Test
    public void method1() {
        logCheck(compile1, logData);
    }

    @JunitPerfConfig(duration = 10000, warmUp = 10, threads = 1)
    @Test
    public void method2() {
        logCheck(compile2, logData);
    }

    @JunitPerfConfig(duration = 10000, warmUp = 10, threads = 1)
    @Test
    public void method3() {
        logCheck(compile3, logData);
    }

    @JunitPerfConfig(duration = 10000, warmUp = 10, threads = 1)
    @Test
    public void method4() {
        logCheck(compile4, logData);
    }

    @JunitPerfConfig(duration = 10000, warmUp = 10, threads = 1)
    @Test
    public void method5() {
        if (CollUtil.isNotEmpty(logData)) {
            logData.put("ips", ips);
        }
        logCheck(compile5, logData);
    }

    @JunitPerfConfig(duration = 1000, warmUp = 10, threads = 1)
    @Test
    public void method6() {
        String key = "192.168.70.2";
        ipsMap.containsKey(key);
    }

    @JunitPerfConfig(duration = 1000, warmUp = 10, threads = 1)
    @Test
    public void method7() {
        String key = "192.168.70.2";
        key.equals("192.168.70.2");
//        int a = 1;
//        boolean b = (a == 1);
    }

}
