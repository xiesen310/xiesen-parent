package com.github.xiesen.flink.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/8/30 18:15
 */
public class Test02 {
    public static void main(String[] args) {
        int capacity = 50;

        Map<String, Object> map = mockData();
        Map<String, Double> measures = new HashMap<>(capacity);
        Map<String, Double> tmp1 = (Map<String, Double>) map.get(LogConstants.MEASURES);

        Map<String, String> normalFields = new HashMap<>(capacity);
        Map<String, String> tmp2 = (Map<String, String>) map.get(LogConstants.NORMAL_FIELDS);

        try {
            if (tmp1 != null) {
                for (Map.Entry entry : tmp1.entrySet()) {
                    measures.put(String.valueOf(entry.getKey()), Double.parseDouble(String.valueOf(entry.getValue())));
                }
            }
            if (tmp2 != null) {
                for (Map.Entry entry : tmp2.entrySet()) {
                    normalFields.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        String tmpOffset = map.get(LogConstants.OFFSET).toString();
//        System.out.println(str2long(tmpOffset, 0L));
    }

    private static Map<String, Object> mockData() {
        Map<String, Object> map = new HashMap<>();
        map.put(LogConstants.OFFSET, "123");
        map.put(LogConstants.APPSYSTEM, "test");
        map.put(LogConstants.LOG_TYPE_NAME, "test");
        map.put(LogConstants.SOURCE, "/var/log/a.log");
        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("ip", "192.168.1.1");
        dimensions.put("hostname", "zorkdata-1");
        map.put(LogConstants.DIMENSIONS, dimensions);

        Map<String, String> normalFields = new HashMap<>();
        normalFields.put("message", "aaaa");
        map.put(LogConstants.NORMAL_FIELDS, normalFields);

        Map<String, Double> measures = new HashMap<>();
        measures.put("a", 0.1);
        return map;
    }

    private static long str2long(String tmpOffset, long defaultValue) {
        long offset = defaultValue;
        try {
            boolean flag = null == tmpOffset || tmpOffset.equals(LogConstants.STRING);
            if (!flag) {
                offset = Long.parseLong(tmpOffset);
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return offset;
    }


}
