package com.github.xiesen.junit.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/24 14:57
 */
public class Test1 {
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("a","aa");

        Map<String,Object> map2 = new HashMap<>();
        map2.put("b","bb");

        ZorkDataMap dataMap = new ZorkDataMap();
        dataMap.setMap(map);
        dataMap.setMap(map2);

        System.out.println(dataMap.toString());
    }
}
