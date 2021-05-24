package com.github.xiesen.junit.test;

/**
 * @author 谢森
 * @since 2021/4/23
 */
public class Test3 {
    public static void main(String[] args) {
        boolean aNull = isBlank("null");
        System.out.println(aNull);
    }

    public static boolean isBlank(CharSequence str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        } else {
            return false;
        }
    }
}
