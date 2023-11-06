package com.github.xiesen.common.utils;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author xiese
 * @Description 时间工具类
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:38
 */
public class DateUtil {
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");
    private static DateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static DateFormat format1 = new SimpleDateFormat("yyyyMMdd");

    private static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy.MM.dd");
        }
    };
    private static ThreadLocal<SimpleDateFormat> utcSdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }
    };

    public static Long timestamp(String timestamp) {
        return new DateTime(timestamp).toDate().getTime();
    }

    public static String format(String timestamp) throws ParseException {
        return sdf.get().format(new DateTime(timestamp).toDate());
    }

    public static Long utcDate2Timestamp(String utcDateStr) throws ParseException {
        return utcSdf.get().parse(utcDateStr).getTime();
    }

    /**
     * 获取本地 utc 时间
     *
     * @return
     */
    public static String getUTCTimeStr() {
        return format.format(new Date()).toString();
    }

    public static String getUTCTime() {
        return parseFormat.format(new Date()).toString();
    }

    public static String getParseTimeStr() {
        return parseFormat.format(new Date()).toString();
    }

    public static String getDate() {
        return String.valueOf(format1.format(new Date()));
    }

    /**
     * 获取当前时间戳
     *
     * @return @{link String}
     */
    public static String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getUTCTimeStr(long interval) {
        long currentTimeMillis = System.currentTimeMillis();
        return format.format(new Date(currentTimeMillis + interval)).toString();
    }

    public static long utcToTimestamp(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
            TemporalAccessor temporalAccessor = formatter.parse(dateTimeString);
            Instant instant = Instant.from(temporalAccessor);
            return instant.toEpochMilli();
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
            return 0L;
        }
    }

    public static void main(String[] args) throws ParseException {
        String timeStr = getUTCTimeStr();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");
        Date date = sdf.parse(timeStr);
        System.out.println(date);
        System.out.println(date.getTime());

        System.out.println(getParseTimeStr());

        System.out.println("===================================");
        String dateTimeString1 = "2023-08-14T05:38:15-140813659Z";
        String dateTimeString2 = "2023-08-14T05:38:15.099Z";

        long timestamp1 = utcToTimestamp(dateTimeString1);
        long timestamp2 = utcToTimestamp(dateTimeString2);

        System.out.println(timestamp1);
        System.out.println(timestamp2);

    }
}
