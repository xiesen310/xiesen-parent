package com.github.xiesen.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/3/24
 */
public class HashMapTest3 {
    public static Map<String, Object> result = new HashMap<>(16);

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        Map<String, Object> result = elementSelector(map, "1", "2","4");
        System.out.println(result);

    }

    public static Map<String, Object> elementSelector(Map<String, Object> req, String... str) {
        result.clear();
        if (req != null) {
            for (String strIndex : str) {
                if (req.containsKey(strIndex)) {
                    result.put(strIndex, req.get(strIndex));
                }
            }
        }
        return result;
    }
}
