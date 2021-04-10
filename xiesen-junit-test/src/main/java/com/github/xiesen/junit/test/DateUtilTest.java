package com.github.xiesen.junit.test;

import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * @author 谢森
 * @since 2021/3/31
 */
public class DateUtilTest {
    private static final String NULL = "NULL";
    private static final String STRINGEMPTY = "";
    private static final String PREFIX1 = "1";
    private static final int INT13 = 13;
    private static final int INT10 = 10;
    private static final String STRING_EMPTY = " ";
    public static final String ts = "2021-03-31T17:19:09.624+08:00";

    public static void main(String[] args) {
//        test1();
//        test2();


        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        int i = 1;
        while (end - start < 1000) {
            end = System.currentTimeMillis();
            getMsTime("1617185943085");
            i++;
        }
        System.out.println("执行了 " + i + " 次");

    }

    private static void test2() {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        int i = 1;
        while (end - start < 1000) {
            end = System.currentTimeMillis();
            getIsoTime(ts);
            i++;
        }
        System.out.println("执行了 " + i + " 次");
    }

    private static void test1() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            getIsoTime(ts);
        }
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + " ms");
    }

    public static String getIsoTime(String str) {
        if (isNull(str)) {
            return str;
        }
        str = str.trim();
        try {
            DateTime datetime;
            // 1能管到2033年
            if (str.length() == INT13 && str.startsWith(PREFIX1)) {
                long t = Long.parseLong(str);
                datetime = new DateTime(t);
            } else if (str.length() == INT10 && str.startsWith(PREFIX1)) {
                // 秒数，1能管到2033年
                long t = Long.parseLong(str) * 1000;
                datetime = new DateTime(t);
            } else {
                datetime = new DateTime(str);
            }
            return datetime.toDateTimeISO().toString();
        } catch (Exception ex) {
            System.err.println("数据转换失败，数据为：" + str + ",失败原因为：" + ex.toString());
            return null;
        }
    }

    public static boolean isNull(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();

        return STRINGEMPTY.equals(str) || NULL.equalsIgnoreCase(str);
    }


    public static long getMsTime(String str) {
        if (str == null) {
            return -1;
        }
        str = str.trim();
        try {
            // 豪秒数，1能管到2033年
            if (str.length() == INT13 && str.startsWith(PREFIX1)) {
                return Long.parseLong(str);
                // 秒数，1能管到2033年
            } else if (str.length() == INT10 && str.startsWith(PREFIX1)) {
                return Long.parseLong(str) * 1000;
            } else if (str.contains(STRING_EMPTY)) {
                /// flink sql 转换的时间戳 特殊格式处理一下
                return Timestamp.valueOf(str).getTime();
            } else {
                return new DateTime(str).getMillis();
            }
        } catch (Exception ex) {
            return -1;
        }
    }
}
