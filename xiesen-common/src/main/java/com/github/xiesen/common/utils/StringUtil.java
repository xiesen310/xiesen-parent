package com.github.xiesen.common.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiese
 * @Description 字符串工具类
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:39
 */
public class StringUtil {

    public static void main(String[] args) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long a = Long.parseLong("1487810258000");
        System.out.println(df.format(new Date(a)));
        System.out.println(getMSTime("2017-02-23T00:37:38Z"));
    }

    public static String getISOTime(String str) {
        if (str == null) {
            return str;
        }
        str = str.trim();
        try {
            DateTime datetime;
            if (str.length() == 13 && str.startsWith("1")) {// 1能管到2033年
                long t = Long.parseLong(str);
                datetime = new DateTime(t);
            } else if (str.length() == 10 && str.startsWith("1")) {// 秒数，1能管到2033年
                long t = Long.parseLong(str) * 1000;
                datetime = new DateTime(t);
            } else {
                datetime = new DateTime(str);
                // datetime = new DateTime("2033-02-13T00:37:38.778Z");
                // datetime = new DateTime("2017-02-23T00:37:38.778+08:00");
            }
            // "2017-02-23T00:37:38.778+08:00"
            return datetime.toDateTimeISO().toString();
        } catch (Exception ex) {
            return null;
        }
    }

    public static long getMSTime(String str) {
        if (str == null) {
            return -1;
        }
        str = str.trim();
        try {
            if (str.length() == 13 && str.startsWith("1")) {// 豪秒数，1能管到2033年
                return Long.parseLong(str);
            } else if (str.length() == 10 && str.startsWith("1")) {// 秒数，1能管到2033年
                return Long.parseLong(str) * 1000;
            } else {
                // datetime = new DateTime("2033-02-13T00:37:38.778Z");
                // datetime = new DateTime("2017-02-23T00:37:38.778+08:00");
                return new DateTime(str).getMillis();
            }
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * 指标入kafka的后，spark处理不了特殊字符
     */
    public static String replaceChar4MetricKey(String str) {
        str = str.replaceAll("\"", "").replaceAll(",", "_").replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\\\", "").replaceAll(" ", "_").replaceAll("=", "").replaceAll(":", "")
                .replaceAll("\\.", "_");
        return str;
    }

    public static List<String> numbers = new ArrayList<String>() {
        {
            add("0");
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
            add("8");
            add("9");
        }
    };

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);

        return isNum.matches();
    }

    public static boolean isNull(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();

        return str.equals("") || str.equalsIgnoreCase("NULL");
    }

    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static Double getDouble(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        try {
            return Double.valueOf(str);
        } catch (Exception Ex) {
            return null;
        }
    }

    public static double getDouble(String str, double defaultValue) {
        Double d = getDouble(str);
        return d == null ? defaultValue : d;
    }

    public static long getLong(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        str = str.trim();
        try {
            return Long.valueOf(str);
        } catch (Exception Ex) {
            return defaultValue;
        }
    }

    public static int getInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        str = str.trim();
        try {
            return Integer.valueOf(str);
        } catch (Exception Ex) {
            return defaultValue;
        }
    }
}
