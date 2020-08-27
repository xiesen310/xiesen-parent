package com.github.xiesen.flink.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/7/27 13:26
 */
public class Test01 {

    private static final String STRIKE_THROUGH = "-";
    private static final String EMPTY_STR = "";

    /**
     * 生成 es id
     *
     * @param bigMap
     * @return
     */
    public static String generateEsId(Map<String, Object> bigMap) {
        StringBuilder builder = new StringBuilder();
        String a = bigMap.toString();
//        if (bigMap.containsKey(LogConstants.TIMESTAMP)) {
//            builder.append(bigMap.get(LogConstants.TIMESTAMP));
//        }
//
//        if (bigMap.containsKey(LogConstants.SOURCE)) {
//            builder.append(bigMap.get(LogConstants.SOURCE));
//        }
//
//        if (bigMap.containsKey(LogConstants.OFFSET)) {
//            builder.append(bigMap.get(LogConstants.OFFSET));
//        }
//
//        if (bigMap.containsKey(LogConstants.DIMENSIONS)) {
//            Map<String, String> dis = (Map<String, String>) bigMap.get(LogConstants.DIMENSIONS);
//            if (dis.containsKey(LogConstants.APPSYSTEM)) {
//                builder.append(dis.get(LogConstants.APPSYSTEM));
//            }
//        }
//
//        if (bigMap.containsKey(LogConstants.NORMAL_FIELDS)) {
//            Map<String, String> normalFields = (Map<String, String>) bigMap.get(LogConstants.NORMAL_FIELDS);
//            if (normalFields.containsKey(LogConstants.DES_TIME)) {
//                builder.append(normalFields.get(LogConstants.DES_TIME));
//            }
//        }
//
        IdWorker idWorker=new IdWorker(0,0);
        long id = idWorker.nextId();
        System.out.println(id);
        return String.valueOf(id);
    }


    public static void main(String[] args) {
        Map<String, Object> bigMap = mockLogData();
        int size = 1000;

        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            System.out.println(i);
            generateEsId(bigMap);
        }
        long end = System.nanoTime();

        System.out.println("平均耗时: " + (end - start) / size + " ns");

    }

    /**
     * 模拟数据
     *
     * @return
     */
    private static Map<String, Object> mockLogData() {
        Map<String, Object> bigMap = new HashMap<>(20);
        bigMap.put(LogConstants.LOG_TYPE_NAME, "logTypeName");
        bigMap.put(LogConstants.TIMESTAMP, "2222222222");
        bigMap.put(LogConstants.SOURCE, "/var/log/a/log");
        bigMap.put(LogConstants.OFFSET, "100");
        Map<String, String> dimensions = new HashMap(20);
        Map<String, String> normalFields = new HashMap(20);
        Map<String, Double> measures = new HashMap(20);
        dimensions.put("ip", "192.168.1.1");
        dimensions.put("hostname", "localhost");
        dimensions.put("appsystem", "streamx");
        bigMap.put(LogConstants.DIMENSIONS, dimensions);
        bigMap.put(LogConstants.MEASURES, measures);
        String deserializerTime = "111111111111111";
        normalFields.put(LogConstants.DES_TIME, deserializerTime);
        bigMap.put(LogConstants.NORMAL_FIELDS, normalFields);
        return bigMap;
    }
}
