package com.github.xiesen.mock.test;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiesen
 * @title: TestCmdb
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/12/5 18:17
 */
public class TestCmdb {
    public static void main(String[] args) {
        Map<String, Object> bigMap = new HashMap<>();
        Map<String, Object> defaultIp = new HashMap<>();
        defaultIp.put("id", "1");
        defaultIp.put("appsystem", "test");
        Map<String, Object> defaultLatency = new HashMap<>();

        defaultLatency.put("id", "2");
        defaultLatency.put("appsystem", "test");
        bigMap.put("192.168.1.1", defaultIp);
        bigMap.put("192.168.1.1", defaultLatency);

        System.out.println(JSON.toJSONString(bigMap));
    }
}
