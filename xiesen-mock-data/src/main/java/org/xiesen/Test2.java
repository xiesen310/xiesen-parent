package org.xiesen;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiesen
 * @title: Test2
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/7/7 9:33
 */
public class Test2 {
    public static final String OFFSET = "offset";
    public static final String NULL_STR = "null";

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put(OFFSET, NULL_STR);
        Object o = map.get(OFFSET);
        String s = String.valueOf(o);
        if (null == o || NULL_STR.equalsIgnoreCase(s)) {
            map.put(OFFSET, "0");
        } else {
            map.put(OFFSET, s);
        }

        map.forEach((k, v) -> System.out.println("k = " + k + ", v = " + v));
    }
}
