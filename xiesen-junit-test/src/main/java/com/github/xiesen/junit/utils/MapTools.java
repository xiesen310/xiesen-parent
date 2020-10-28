package com.github.xiesen.junit.utils;

import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/10/26 20:06
 */
public class MapTools {
    private static Map<String, List<String>> bigMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", "xiesen");
        map.put("age", 12);
        map.put("ts", System.currentTimeMillis());

//        System.out.println(MapUtil.getStr(map, "age"));
//        System.out.println(MapUtil.getStr(map, "ts"));
//        System.out.println(MapUtil.getStr(map, "name"));
//        System.out.println(MapUtil.getStr(map, "xiesem"));
//        System.out.println(MapUtil.getInt(map, "xiesem",1));

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("a",1);
//        jsonObject.put("a",2);
//        System.out.println(jsonObject.toString());
//        List<String> list = bigMap.get("msg");

        List list = MapUtil.get(bigMap, "msg", List.class);
        list.forEach(s -> System.out.println(s));
    }
}
