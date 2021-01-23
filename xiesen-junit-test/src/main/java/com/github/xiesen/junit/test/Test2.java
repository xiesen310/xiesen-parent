package com.github.xiesen.junit.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/22 19:27
 */
public class Test2 {
    private static Map<Integer, String> map = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++)
                    map.put(i, String.valueOf(i));
            }
        };

        runnable.run();

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(map.toString());
                }

            }
        };
        runnable2.run();
    }
}
