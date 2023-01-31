package com.github.xiesen.redis.xxhash;

/**
 * @author xiese
 * @Description 测试 xxhash 算法
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:33
 */
public class TestXxHash {
    public static void main(String[] args) {
        int size = 10;
        for (int i = 0; i < size; i++) {
            method1();
        }
    }

    /**
     * 测试单位时间内执行的次数
     */
    private static void method1() {
        int i = 0;
        int size = 1000;
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        while (end - start <= size) {
            long hello = AbstractLongHashFunction.xx().hashChars("xiesen");
            end = System.currentTimeMillis();
            i++;
        }
        System.out.println(i);
    }
}
