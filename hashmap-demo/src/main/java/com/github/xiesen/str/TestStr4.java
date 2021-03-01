package com.github.xiesen.str;

/**
 * @author 谢森
 * @since 2021/2/2
 */
public class TestStr4 {
    public static void main(String[] args) {
        String s1 = new String("Java");
        String s2 = s1.intern();
        String s3 = "Java";
        System.out.println(s1 == s2);
        System.out.println(s2 == s3);

        String s4 = "Ja" + "va";
        System.out.println(s3 == s4);
    }
}
