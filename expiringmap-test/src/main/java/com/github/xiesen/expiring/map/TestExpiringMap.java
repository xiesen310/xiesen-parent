package com.github.xiesen.expiring.map;

import com.alibaba.fastjson.JSON;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author xiesen
 */
public class TestExpiringMap {

    public static void main(String[] args) throws InterruptedException {
//        testCreate1();
//        testCreate2();
//        testCreate3();
//        testMaxSize();
//        testExpirationListener();
        testDelete();
    }

    /**
     * 过期策略为ExpirationPolicy.CREATED*
     * expiration(3000, TimeUnit.MILLISECONDS) 过期时间为3秒
     * expirationPolicy(ExpirationPolicy.CREATED) 过期策略为创建后
     *
     * @throws InterruptedException
     */
    private static void testCreate1() throws InterruptedException {
        ExpiringMap<String, String> map = ExpiringMap.builder().expiration(3000, TimeUnit.MILLISECONDS).
                expirationPolicy(ExpirationPolicy.CREATED)
                .build();
        map.put("testkey", "testValue");
        System.out.println("map = " + JSON.toJSONString(map));
        ///等待3秒
        Thread.sleep(3000);
        System.out.println("sleep map = " + JSON.toJSONString(map));
    }

    /**
     * 过期策略为ExpirationPolicy.CREATED，过期前更新*
     * expiration(3000, TimeUnit.MILLISECONDS) 过期时间为3秒
     * expirationPolicy(ExpirationPolicy.CREATED) 过期策略为创建后
     *
     * @throws InterruptedException
     */
    private static void testCreate2() throws InterruptedException {
        ExpiringMap<String, String> map = ExpiringMap.builder().expiration(5000, TimeUnit.MILLISECONDS).
                expirationPolicy(ExpirationPolicy.CREATED)
                .build();
        map.put("testkey", "testValue2");
        System.out.println("map = " + JSON.toJSONString(map));
        Thread.sleep(4000);
        ///模拟过期前更新值,注意这里更新的值未变化时会过期
        map.put("testkey", "testValue3");
        System.out.println("putMap = " + JSON.toJSONString(map));
        Thread.sleep(2000);
        System.out.println("过期前更新update map= " + JSON.toJSONString(map));
    }

    private static void testCreate3() throws InterruptedException {
        ExpiringMap<String, String> map = ExpiringMap.builder().expiration(3000, TimeUnit.MILLISECONDS).
                expirationPolicy(ExpirationPolicy.ACCESSED)
                .build();
        map.put("testkey", "testValue");
        System.out.println("map = " + JSON.toJSONString(map));
        Thread.sleep(2000);
        System.out.println("get map key= " + map.get("testkey"));
        Thread.sleep(2000);
        System.out.println("sleep map = " + JSON.toJSONString(map));
    }

    private static void testMaxSize() throws InterruptedException {
        ExpiringMap<String, String> map = ExpiringMap.builder().maxSize(10)
                .build();
        for (int i = 0; i < 10; i++) {
            map.put("key" + i, "value" + i);
        }
        System.out.println("key0= " + map.get("key0"));
        map.put("key100", "value100");
        System.out.println("key0= " + map.get("key0"));
        System.out.println("key1=" + map.get("key1"));
    }

    private static void testExpirationListener() throws InterruptedException {
        ExpiringMap<String, String> map = ExpiringMap.builder().
                expiration(3000, TimeUnit.MILLISECONDS).
                expirationListener((key, value) -> {
                    System.out.println("过期监听1，key = " + key + ",value = " + value);
                }).expirationListener((key, value) -> {
                    System.out.println("过期监听2，key = " + key + ",value = " + value);
                }).build();

        map.put("testkey", "testValue");
        System.out.println("map = " + JSON.toJSONString(map));
        Thread.sleep(3000);
        System.out.println("sleep map={}" + JSON.toJSONString(map));
    }

    private static void testDelete() throws InterruptedException {
        ExpiringMap<String, String> map = ExpiringMap.builder().
                expiration(3000, TimeUnit.MILLISECONDS)
                .expirationPolicy(ExpirationPolicy.CREATED).build();

        map.put("testkey", "testValue");
        System.out.println("map size = " + map.size() + ",map = " + JSON.toJSONString(map));
        Thread.sleep(500);
        final String testkey = map.remove("testkey");
        System.out.println(testkey);
        System.out.println("delete map after ,map size = " + map.size() + ",map = " + JSON.toJSONString(map));
    }

}
