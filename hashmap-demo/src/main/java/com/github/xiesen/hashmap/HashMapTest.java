package com.github.xiesen.hashmap;

import java.util.HashMap;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/24 20:08
 */
public class HashMapTest {
    public static void main(String[] args) {
        HashMap map = new HashMap<>();
        map.put("1", "2");
        System.out.println(map.get("1"));
    }
}
