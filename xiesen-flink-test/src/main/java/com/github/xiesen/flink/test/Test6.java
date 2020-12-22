package com.github.xiesen.flink.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/21 15:06
 */
public class Test6 {
    public static void main(String[] args) {
        String msg = "{\"@timestamp\":\"2020-12-21T13:46:32.009Z\",\"@metadata\":{\"beat\":\"filebeat\"," +
                "\"type\":\"doc\",\"version\":\"6.8.13\",\"topic\":\"filebeat\"},\"message\":\"[  1695.691] AUDIT: " +
                "Mon Dec 21 20:28:56 2020: 8386: client 15 disconnected\",\"offset\":30905,\"source\":\"/var/log/Xorg" +
                ".0.log\",\"fields\":{\"ip\":\"192.168.42.138\",\"logTopic\":\"filebeat\",\"apps\":\"dev_test\"}," +
                "\"host\":{\"name\":\"elastic\"}}";
        LogRecords logRecords = JSON.parseObject(msg, new TypeReference<LogRecords>() {
        });

        System.out.println(logRecords);

    }
}
