package com.github.xiesen.mock.ops;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiesen
 */
public class Test1 {
    public static final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        final ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap();

        cache.put("1", concurrentHashMap);
        cache.put(null, concurrentHashMap);
        cache.put("2", null);


        for (String s : cache.keySet()) {
            for (String s1 : cache.get(s).keySet()) {
                System.out.println(s1);
            }
        }

    }

}

class SomeRunnable implements Runnable {
    private ConcurrentHashMap<String, String> map = null;
    private String key;

    public SomeRunnable(ConcurrentHashMap<String, String> map, String key) {
        this.map = map;
        this.key = key;
    }

    public void run() {
        try {
            map.remove(key);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}