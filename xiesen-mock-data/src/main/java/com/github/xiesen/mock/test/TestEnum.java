package com.github.xiesen.mock.test;

import org.apache.commons.collections.MapUtils;

/**
 * @author xiesen
 */
public class TestEnum {
    public static final String UNKNOWN = "unknown";
    private static final int SIZE = 1000000;
    public static void main(String[] args) {
        test1();

        test2();
    }

    private static void test1() {
        Long funcIdNum = 120118L;
        long start = System.currentTimeMillis();

        for (int i = 0; i < SIZE; i++) {
            final String funcName = MapUtils.getString(KcbpFuncIdConstant.FUNC_CACHE, funcIdNum, UNKNOWN);
//            System.out.println(funcName);
        }
        long end = System.currentTimeMillis();
        System.out.println("test1 一共 " + SIZE + " 条查询,总耗时: " + (end - start) + "ms");
    }

    private static void test2() {
        Long funcIdNum = 120118L;
        long start = System.currentTimeMillis();

        for (int i = 0; i < SIZE; i++) {
            final String funcName = KcbpFuncIdConstant2.FUNC_CACHE.getIfPresent(funcIdNum);
//            System.out.println(funcName);
        }
        long end = System.currentTimeMillis();
        System.out.println("test2 一共 " + SIZE + " 条查询,总耗时: " + (end - start) + "ms");
    }
}
