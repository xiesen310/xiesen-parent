package com.github.xiesen.aviator;

import cn.hutool.core.lang.Console;
import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.houbb.junitperf.core.report.impl.ConsoleReporter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class HelloTest {
    public static final int ipNums = 200;
    public static Map<String, String> map = new HashMap<>();
    public static String str = "";

    @BeforeAll
    static void init() {
        for (int i = 1; i < ipNums; i++) {
            map.put("192.168.60." + i, "");
        }
        map.put("192.168.70.2", "");


        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < ipNums; i++) {
            builder.append("192.168.60." + i).append(",");
        }
        builder.append("192.168.70.2");
        str = builder.toString();
    }

    @JunitPerfConfig(duration = 1000)
    public void helloTest() throws InterruptedException {
        Thread.sleep(100);
        System.out.println("Hello Junit5");
    }

    //    @JunitPerfConfig(duration = 1000)
    @Test
    public void strContainTest() throws InterruptedException {
        String key = "192.168.70.2";
        long start = System.currentTimeMillis();
        for (int i = 0; i < ipNums; i++) {
            System.out.println(str.contains(key));
        }
        Console.log("耗时: {} ms", System.currentTimeMillis() - start);
    }

    @JunitPerfConfig(duration = 10000, warmUp = 1L, reporter = ConsoleReporter.class)
    @Test
    public void mapContainTest() throws InterruptedException {
        String key = "192.168.70.2";
//        long start = System.currentTimeMillis();
        map.containsKey(key);
//        Console.log("耗时: {} ms", System.currentTimeMillis() - start);
    }
}
