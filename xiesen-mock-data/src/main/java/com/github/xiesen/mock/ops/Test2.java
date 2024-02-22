package com.github.xiesen.mock.ops;

import cn.hutool.core.map.MapUtil;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiesen
 * @title: Test2
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2023/3/16 17:49
 */
public class Test2 {
    public static List<LocalDateTime> calculateNextExecutionTimes(LocalDateTime currentTime, Duration windowSize, int count) {
        List<LocalDateTime> executionTimes = new ArrayList<>();

        LocalDateTime nextExecutionTime = currentTime.minusSeconds(currentTime.getSecond() % windowSize.getSeconds())
                .withNano(0);

        if (nextExecutionTime.isBefore(currentTime)) {
            nextExecutionTime = nextExecutionTime.plus(windowSize);
        }

        for (int i = 0; i < count; i++) {
            executionTimes.add(nextExecutionTime);
            nextExecutionTime = nextExecutionTime.plus(windowSize);
        }

        return executionTimes;
    }

    public static LocalDateTime calculateNextExecutionTimes(Duration windowSize) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime nextExecutionTime = null;
        if (windowSize.getSeconds() > 0 && windowSize.getSeconds() <= 60) {
            nextExecutionTime = currentTime.minusSeconds(currentTime.getSecond() % windowSize.getSeconds())
                    .withNano(0);
        } else if (windowSize.toMinutes() > 0 && windowSize.toMinutes() <= 60) {
            nextExecutionTime = currentTime.minusMinutes(currentTime.getMinute() % windowSize.toMinutes())
                    .withSecond(0)
                    .withNano(0);
        } else if (windowSize.toHours() > 0 && windowSize.toHours() <= 24) {
            nextExecutionTime = currentTime.minusHours(currentTime.getHour() % windowSize.toHours())
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
        } else {
            System.out.println("不支持大于24小时的窗口");
            return null;
        }
        if (nextExecutionTime.isBefore(currentTime)) {
            nextExecutionTime = nextExecutionTime.plus(windowSize);
        }

        return nextExecutionTime;
    }

    /**
     * 根据时间窗口计算延迟
     *
     * @param windowSize 窗口大小
     * @return 延时数，单位毫秒
     */
    public static long calculateDelay(Duration windowSize) {
        LocalDateTime currentTime = LocalDateTime.now();
        long delay = 0L;
        LocalDateTime nextExecutionTime = null;
        if (windowSize.getSeconds() > 0 && windowSize.getSeconds() <= 60) {
            nextExecutionTime = currentTime.minusSeconds(currentTime.getSecond() % windowSize.getSeconds())
                    .withNano(0);
        } else if (windowSize.toMinutes() > 0 && windowSize.toMinutes() <= 60) {
            nextExecutionTime = currentTime.minusMinutes(currentTime.getMinute() % windowSize.toMinutes())
                    .withSecond(0)
                    .withNano(0);
        } else if (windowSize.toHours() > 0 && windowSize.toHours() <= 24) {
            nextExecutionTime = currentTime.minusHours(currentTime.getHour() % windowSize.toHours())
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
        } else {
            System.out.println("不支持大于24小时的窗口");
            return 0L;
        }

        if (nextExecutionTime.isBefore(currentTime)) {
            nextExecutionTime = nextExecutionTime.plus(windowSize);
        }
        delay = nextExecutionTime.toInstant(ZoneOffset.UTC).toEpochMilli() - currentTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        return delay;
    }

    public static void main(String[] args) {
        Map<String, Object> evnet = new HashMap<>();
        final Map<String, Object> map = MapUtil.get(evnet, "measures", Map.class);
        if (null != map && map.size() > 0) {
            System.out.println("不空");
        } else {
            System.out.println("空");

        }

        System.out.println("============");
        // 窗口大小
        Duration windowSize = Duration.ofSeconds(60);
//        Duration windowSize = Duration.ofMinutes(10);
//        Duration windowSize = Duration.ofHours(30);

        // 当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 计算最近5次的执行时间
//        List<LocalDateTime> executionTimes = calculateNextExecutionTimes(currentTime, windowSize, 1);
        LocalDateTime executionTime = calculateNextExecutionTimes(windowSize);

        System.out.println("Next 5 execution times:");
//        for (LocalDateTime executionTime : executionTimes) {
        System.out.println(executionTime);
//        }

        System.out.println("计算延迟：");
        System.out.println(calculateDelay(windowSize));

    }

    public static List<Integer> searchByIndexOf(String message) {
        String newStr = "[20";
        int count = 0;
        int i = 0;
        List<Integer> list = new ArrayList<>();
        while (message.indexOf(newStr, i) >= 0) {
            count++;

            i = message.indexOf(newStr, i) + newStr.length();
            list.add(i);
        }

        System.out.println(newStr + "匹配到" + count + "次");
        return list;
    }

}
