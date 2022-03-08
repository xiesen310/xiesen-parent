package com.github.xiesen.mock.test;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author xiesen
 * @title: Test01
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/1/24 10:39
 */
public class Test01 {
    /**
     * 时间戳按照指定格式转换成日期
     *
     * @param ts      时间戳
     * @param pattern 格式
     * @return String
     */
    private static String converTimestamp2Date(Long ts, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date(ts));
    }

    public static void main(String[] args) throws InterruptedException {
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(new Random().nextInt(10));
            long ts = System.currentTimeMillis();
            long s = ts % (60 * 1000);
            if (s < 1000) {
                System.out.println("当前时间为整点: " + converTimestamp2Date(ts, pattern));
            } else {
                System.out.println("当前时间非整点: " + converTimestamp2Date(ts, pattern));
            }
        }
    }
}
