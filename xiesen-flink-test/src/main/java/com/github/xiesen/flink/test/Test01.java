package com.github.xiesen.flink.test;

import org.apache.commons.lang3.time.DateUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/7/27 13:26
 */
public class Test01 {

    public String generateEsId(Map<String, Object> bigMap) {
        StringBuilder builder = new StringBuilder();

        if (bigMap.containsKey(LogConstants.TIMESTAMP)) {
            builder.append(bigMap.get(LogConstants.TIMESTAMP));
        }
        if (bigMap.containsKey(LogConstants.SOURCE)) {
            builder.append(bigMap.get(LogConstants.SOURCE));
        }
        if (bigMap.containsKey(LogConstants.APPSYSTEM)) {
            builder.append(bigMap.get(LogConstants.APPSYSTEM));
        }

        if (bigMap.containsKey(LogConstants.OFFSET)) {
            builder.append(bigMap.get(LogConstants.OFFSET));
        }

        if (bigMap.containsKey(LogConstants.NORMAL_FIELDS)) {
            Map<String, String> normalFields = (Map<String, String>) bigMap.get(LogConstants.NORMAL_FIELDS);
            if (normalFields.containsKey(LogConstants.DES_TIME)) {
                builder.append(normalFields.get(LogConstants.DES_TIME));
            }
        }

        String id = UUID.nameUUIDFromBytes(builder.toString().getBytes()).toString();

        return id;
    }

    public static void main(String[] args) {
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

        System.out.println(bigMap);
        Test01 test01 = new Test01();
        System.out.println(test01.generateEsId(bigMap));
    }
}
