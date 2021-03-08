package com.github.xiesen.hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/24 20:08
 */
public class HashMapTest {
    public static void main(String[] args) {
        HashMap map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put(null, "3");


        List<String> list = new ArrayList<>();
        list.add(null);
        list.add("1 ");
        list.add("2");
        for (String key : list) {
            if (null != key) {
                key = key.trim();
            } else {
                continue;
            }
            System.out.println(key);
        }


        StringBuilder builder = null;

        if (null == builder) {
            builder = new StringBuilder();
            builder.append("");
            System.out.println(builder.toString());
        }
    }
}
