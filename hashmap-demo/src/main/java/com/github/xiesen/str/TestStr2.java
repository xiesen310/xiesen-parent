package com.github.xiesen.str;

/**
 * 字符串拼接测试
 *
 * @author 谢森
 * @since 2021/2/2
 */
public class TestStr2 {
    public static void main(String[] args) {
        String s1 = "java";
        String s2 = "ja";
        String s3 = "va";

        System.out.println(s1 == s2 + s3);
        System.out.println(s1.equals(s2 + s3));
        System.out.println(s1.equals(s2 + "va"));
    }
}
