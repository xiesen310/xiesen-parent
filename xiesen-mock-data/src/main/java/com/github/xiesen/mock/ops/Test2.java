package com.github.xiesen.mock.ops;

import cn.hutool.core.map.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiesen
 * @title: Test2
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2023/3/16 17:49
 */
public class Test2 {

    public static void main(String[] args) {
        Map<String, Object> evnet = new HashMap<>();
        final Map<String, Object> map = MapUtil.get(evnet, "measures", Map.class);
        if (null != map && map.size() > 0) {
            System.out.println("不空");
        } else {
            System.out.println("空");

        }


    }

    public static List<Integer> searchByIndexOf(String message) {
        String newStr = "[20";
        int count = 0;
        int i = 0;
        List<Integer> list = new ArrayList<>();
        while (message.indexOf(newStr, i) >= 0) {
            count++;

            i = message.indexOf(newStr, i) + newStr.length();
            list.add(i);
        }

        System.out.println(newStr + "匹配到" + count + "次");
        return list;
    }

}
