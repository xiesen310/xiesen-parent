package org.xiesen;

import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiesen
 * @title: Test5
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/11/10 18:03
 */
public class Test5 {
    private static final String FUNCTION_ID_STR = "tags_function_id";

    public static void main(String[] args) {
        Map<String, String> normalFields = new HashMap<>();
//        normalFields.put(FUNCTION_ID_STR, "111111");
        final String functionId = MapUtil.getStr(normalFields, FUNCTION_ID_STR);
        if (null != functionId) {
            System.out.println(FUNCTION_ID_STR + " = " + functionId);
        } else {
            System.out.println(FUNCTION_ID_STR + " is null.");
        }


        for (int i =0; i < 100; i++) {
            final int num = new Random().nextInt(10);
            System.out.println(num);

        }
    }
}
