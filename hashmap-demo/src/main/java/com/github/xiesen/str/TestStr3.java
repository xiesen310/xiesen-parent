package com.github.xiesen.str;

/**
 * @author 谢森
 * @since 2021/2/2
 */
public class TestStr3 {
    public static void main(String[] args) {
        Person p1 = new Person("allen");
        Person p2 = new Person("allen");
        System.out.println(p1 == p2);
        System.out.println(p1.equals(p2));
        System.out.println("p1 hashcode: " + p1.hashCode() + " p2 hashcode: " + p2.hashCode());

        System.out.println(p1.equals(p2));

    }
}
