package com.github.xiesen.hashmap;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/3/24
 */
public class HashMapTest3 {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", null);

        map.forEach((k, v) -> {
            System.out.println("k = " + k + " , v = " + v);
        });

        String json = JSON.toJSONString(map);
        System.out.println(json);
    }
}
