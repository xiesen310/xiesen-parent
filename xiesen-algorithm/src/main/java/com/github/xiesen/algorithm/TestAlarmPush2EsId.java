package com.github.xiesen.algorithm;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @Description test
 * @Email xiesen310@163.com
 * @Date 2020/12/18 19:21
 */
public class TestAlarmPush2EsId {
    public static void main(String[] args) {
        Map<String, Object> map = buildAlarmPushData();
        System.out.println(JSON.toJSONString(map));
        System.out.println(EsIdGenerate.xxHashAlarmPush2es(map));
    }

    /**
     * {
     * "alarmChannelId": -6138543741811376368,
     * "level": 1,
     * "recipient": "recipient",
     * "contactWay": "contactWay",
     * "id": "db347c87-ac62-456c-8765-fe48b285e8fe",
     * "time": "2020-12-24T21:39:05.965+08:00",
     * "appSystem": "tdx",
     * "title": "time",
     * "type": 1,
     * "sendMode": 1,
     * "pushResult": 1,
     * "content": "content"
     * }
     *
     * @return
     */
    public static Map<String, Object> buildAlarmPushData() {
        Map<String, Object> map = new HashMap<>();
        map.put("alarmChannelId", "-6138543741811376368");
        map.put("level", 1);
        map.put("recipient", "recipient");
        map.put("contactWay", "contactWay");
        map.put("id", "db347c87-ac62-456c-8765-fe48b285e8fe");
        map.put("time", "2020-12-24T21:39:05.965+08:00");
        map.put("appSystem", "tdx");
        map.put("type", 1);
        map.put("sendMode", 1);
        map.put("pushResult", 1);
        map.put("content", "content");
        return map;
    }
}
