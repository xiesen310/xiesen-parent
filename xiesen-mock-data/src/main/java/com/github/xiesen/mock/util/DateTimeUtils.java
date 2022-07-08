package com.github.xiesen.mock.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiesen
 * @title: DateTimeUtils
 * @projectName smartdata-streamx
 * @description: 时间转换工具类
 * @date 2021/11/30 14:55
 */
@Slf4j
public class DateTimeUtils {

    /**
     * 格式化时间,时间转换失败,默认取当前时间
     *
     * @param dateStr
     * @return
     */
    public static String utc2date(String dateStr) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00").parse(dateStr);
        } catch (Exception e) {
//            log.warn("日期字符 {} ,解析时间格式失败,获取当前系统时间", dateStr);
            date = new Date();
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 获取当前时间 ,时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTime() {
        DateTime now = DateTime.now();
        String result = now.toString(DatePattern.NORM_DATETIME_FORMAT);
        return result;
    }


    public static String getDate() {
        String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
        return s;
    }

    public static void main(String[] args) {
        String ss = "2021-11-30T10:30:15.345+08:00";
        System.out.println(utc2date(ss));
        System.out.println(getDateTime());

        Map<String, String> map = new HashMap<>(16);
        System.out.println(map.getOrDefault("key", "xiesen"));
        System.out.println(getDate());

    }
}
