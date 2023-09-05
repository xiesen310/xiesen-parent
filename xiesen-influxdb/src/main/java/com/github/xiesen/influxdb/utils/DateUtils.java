package com.github.xiesen.influxdb.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author xiesen
 */
public class DateUtils {
    public static Date formatTime(String timeString) {
        Date date = null;
        if (timeString.length() == 4) {
            // 解析时间字符串为日期对象
            date = DateUtil.parse(timeString, "yyyy");
        } else if (timeString.length() == 6) {
            // 解析时间字符串为日期对象
            date = DateUtil.parse(timeString, "yyyyMM");
        } else if (timeString.length() == 8) {
            // 解析时间字符串为日期对象
            date = DateUtil.parse(timeString, "yyyyMMdd");
        }
        return date;
    }

    /**
     * statScopeTime 这个字段格式是变的，
     * 如果分周期 yyyymmddhhmi,
     * 如果时周期yyyymmddhh，
     * 如果天周期yyyymmdd，
     * 如果月周期yyyymm，
     * 如果年周期yyyy，
     * 如果总计周期yyyyymmddhhmi
     *
     * @param timeString
     * @return
     */
    public static Date realFormatTime(String timeString) {
        Date date = null;
        if (timeString.length() == 4) {
            // 如果年周期yyyy
            date = DateUtil.parse(timeString, "yyyy");
        } else if (timeString.length() == 6) {
            // 解析时间字符串为日期对象
            // 如果月周期yyyymm
            date = DateUtil.parse(timeString, "yyyyMM");
        } else if (timeString.length() == 8) {
            // 解析时间字符串为日期对象 如果天周期yyyymmdd
            date = DateUtil.parse(timeString, "yyyyMMdd");
        } else if (timeString.length() == 10) {
            // 如果时周期yyyymmddhh
            date = DateUtil.parse(timeString, "yyyyMMddHH");
        } else if (timeString.length() == 12) {
            date = DateUtil.parse(timeString, "yyyyMMddHHmm");
        } else if (timeString.length() == 14) {
            date = DateUtil.parse(timeString, "yyyyMMddHHmmss");
        } else if (timeString.length() == 17) {
            date = DateUtil.parse(timeString, "yyyyMMddHHmmssSSS");
        }
        return date;
    }

    public static void main(String[] args) {
//        [202308291326] with format [yyyyMMddhhmm] error
        System.out.println(realFormatTime("20230904144526"));
    }
}
