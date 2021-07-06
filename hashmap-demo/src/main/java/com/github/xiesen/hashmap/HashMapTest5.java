package com.github.xiesen.hashmap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 谢森
 * @since 2021/3/24
 */
public class HashMapTest5 {
    public static Map<String, Object> result = new ConcurrentHashMap<>(16);

    public static void main(String[] args) {
        boolean b = result.containsKey(null);
        System.out.println(b);
    }
}
