package com.github.xiesen.str;

/**
 * 字符串 == 与 equals
 *
 * @author 谢森
 * @since 2021/2/2
 */
public class TestStr {
    public static void main(String[] args) {
        String s1 = new String("abc");
        String s2 = "abc";
        String s3 = new String("abc");
        System.out.println(s1 == s2);
        System.out.println(s2 == s3);

        System.out.println(s1.equals(s2));
        System.out.println(s2.equals(s3));

        System.out.println(s1 == s2.intern());
        System.out.println(s2 == s3.intern());
        System.out.println(s1.intern() == s3.intern());

        System.out.println(s1.equals(s2.intern()));
        System.out.println(s2.equals(s3.intern()));
        System.out.println(s2.intern().equals(s3.intern()));
    }
}
