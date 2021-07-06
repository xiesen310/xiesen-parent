package com.github.xiesen.hashmap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 谢森
 * @since 2021/3/24
 */
public class HashMapTest6 {
    public static Map<String, Object> result = new ConcurrentHashMap<>(16);

    public static void main(String[] args) throws InterruptedException {
        result.put("a", "a");
        Thread.sleep(2000);
        result.put("a", null);
    }
}
