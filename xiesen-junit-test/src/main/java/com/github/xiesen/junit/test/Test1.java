package com.github.xiesen.junit.test;

/**
 * @author 谢森
 * @Description Test1
 * @Email xiesen310@163.com
 * @Date 2020/12/24 14:57
 */
public class Test1 {
    public static void main(String[] args) {
        boolean notBlank = isNotBlank("");
        System.out.println(notBlank);
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
