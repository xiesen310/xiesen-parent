package com.github.xiesen.flink.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/17 16:47
 */
public class Test5 {

    public static void main(String[] args) {
        Map<String, String> data = getData();
        Map<String, String> tmpMap = new HashMap<>();

        if (isNotEmpty(data)) {
            List<String> schema = getSchema();
            tmpMap.clear();
            StringBuilder errorBuilder = new StringBuilder();
            for (String key : schema) {
                String value = data.get(key);
                if (isNotBlank(value)) {
                    tmpMap.put(key, value);
                } else {
                    errorBuilder.append(key).append(",");
                }
            }
            String error = errorBuilder.toString();
            if (isNotBlank(error)) {
                error = error.substring(0, error.length() - 1);
                System.err.println("维度数据 " + error + " 是空");
            }
        } else {
            System.err.println("维度数据为空");
        }


        tmpMap.forEach((k, v) -> {
            System.out.println("k = " + k + ", v = " + v);
        });
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    public static boolean isBlank(CharSequence str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }


    public static boolean isNotEmpty(Map<?, ?> map) {
        return null != map && false == map.isEmpty();
    }

    private static List<String> getSchema() {
        List<String> list = Arrays.asList("appprogram", "appsystem", "ip", "host");
//        List<String> list = Arrays.asList();
        return list;
    }

    /**
     * 获取数据
     */
    private static Map<String, String> getData() {
        Map<String, String> map = new HashMap<>();
        map.put("appsystem", "tdx");
        map.put("hostname", "zorkdata01");
//        map.put("ip", "192.168.1.1");
        map.put("ip", "");
        return map;
    }
}
