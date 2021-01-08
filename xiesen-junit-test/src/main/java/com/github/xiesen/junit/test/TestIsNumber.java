package com.github.xiesen.junit.test;

import java.util.regex.Pattern;

/**
 * @author 谢森
 * @Description 判断是否是数字
 * @Email xiesen310@163.com
 * @Date 2021/1/7 20:24
 */
public class TestIsNumber {
    public static final String num = "123";
    public static final int size = 100000000;

    public static void main(String[] args) {
//        test();
        for (int i = 0; i < 10; i++) {
            test2();
        }
    }

    private static void test2() {
        long end4 = System.nanoTime();
        for (int i = 0; i < size; i++) {
            isNumeric4(num);
        }
        long end5 = System.nanoTime();
        System.out.println("isNumeric4 耗时: " + (end5 - end4) + " ns");
    }

    /**
     * 性能测试
     */
    private static void test() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            isNumeric1(num);
        }
        long end1 = System.currentTimeMillis();
        System.out.println("isNumeric1 耗时: " + (end1 - start));

        for (int i = 0; i < size; i++) {
            isInteger(num);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("isInteger 耗时: " + (end2 - end1));

        for (int i = 0; i < size; i++) {
            isNumeric2(num);
        }
        long end3 = System.currentTimeMillis();
        System.out.println("isNumeric2 耗时: " + (end3 - end2));

        for (int i = 0; i < size; i++) {
            isNumeric3(num);
        }
        long end4 = System.currentTimeMillis();
        System.out.println("isNumeric3 耗时: " + (end4 - end3));

        for (int i = 0; i < size; i++) {
            isNumeric4(num);
        }
        long end5 = System.currentTimeMillis();
        System.out.println("isNumeric4 耗时: " + (end5 - end4));
    }

    /**
     * 用JAVA自带的函数isDigit()方法判断
     * isDigit() 方法用于判断指定字符是否为数字。如果字符为数字，则返回 true；否则返回 false。
     *
     * @param str
     * @return
     */
    public static boolean isNumeric1(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为整数
     * 使用正则表达式"^[-\\+]?[\\d]*$"判断
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 使用正则表达式"[0-9]*"判断
     *
     * @param str
     * @return
     */
    public static boolean isNumeric2(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 使用正则表达式"^[0-9]*$"判断
     *
     * @param s
     * @return
     */
    public final static boolean isNumeric3(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }


    /**
     * 用ascii码
     *
     * @param str
     * @return
     */
    public static boolean isNumeric4(String str) {
        if (str != null && !"".equals(str.trim())) {
            for (int i = str.length(); --i >= 0; ) {
                int chr = str.charAt(i);
                if (chr < 48 || chr > 57)
                    return false;
            }
            return true;
        } else {
            return false;
        }

    }

}
