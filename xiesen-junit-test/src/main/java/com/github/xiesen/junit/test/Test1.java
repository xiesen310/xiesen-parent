package com.github.xiesen.junit.test;

import java.util.regex.Pattern;

/**
 * @author 谢森
 * @Description Test1
 * @Email xiesen310@163.com
 * @Date 2020/12/24 14:57
 */
public class Test1 {
    private static Pattern waterMarkKeyPattern = Pattern.compile("(?i)^\\s*WATERMARK\\s+FOR\\s+(\\S+)" +
            "\\s+AS\\s+withOffset\\(\\s*(\\S+)\\s*,\\s*(\\d+)\\s*\\)$*");

    public static void main(String[] args) {
        String text = " WATERMARK FOR timestamp AS withOffset('timestamp', 60000), appprogramname varchar, appsystem " +
                "varchar, hostname varchar, ip varchar, measure double , tmpNum double , timestamp TIMESTAMP ";
        boolean flag = text.contains("WATERMARK");
        if (flag) {
            String[] fields = text.split(",");
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < fields.length; i++) {
                builder.append(fields[i]).append(",");
            }
            System.out.println(builder.toString().substring(1, builder.toString().length() - 1));
        }
    }

    public static boolean isBlank(CharSequence str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串不是空白字符
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }
}
