package com.github.xiesen.mock.test;

/**
 * @author xiesen
 * @title: Test2
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/9/22 10:54
 */
public class Test2 {
    public static void main(String[] args) {
        long duration = 14000000;
        System.out.println((double) (duration));

        Double errors = 3.0;
        Double total = 19.0;
        System.out.println(errors / total);

        Long a = 3L;
        Long b = 19L;
        System.out.println(a / b);


        String indexType = "null";
        boolean flag = indexType.equalsIgnoreCase("DYL");
        System.out.println(flag);

    }
}
