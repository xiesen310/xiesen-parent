package com.github.xiesen.mock.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @author 谢森
 * @since 2021/5/27
 */
public class Test01 {
    public static final String a = "{\"alarmContent\":\"[CST May 25 10:49:15] error    : 'noahtest-215' mem usage of " +
            "81.4% matches resource limit [mem usage > 80.0%]\",\"lastTime\":\"2021-05-27T09:53:51.057+08:00\"," +
            "\"eventId\":\"7743fe84334d4c1da917e50eb14330ff\",\"recvUser\":\"recvUser\"," +
            "\"alarmTime\":\"2021-05-27T09:53:51.294+08:00\",\"ip\":\"192.168.70.215\",\"dataType\":1," +
            "\"operator\":\"operator\",\"alarmCount\":\"1\",\"hostname\":\"noahtest-215\"," +
            "\"alarmObject\":\"122_ff89f1d295fdf1ec6ed5ffae0f33c7e5\",\"eventStatus\":\"1\"," +
            "\"alarmId\":\"9307365bf52d4345a3f42e2be5b0d894\",\"eventTime\":\"2021-05-27T09:53:51.294+08:00\"," +
            "\"alarmLevel\":\"5\",\"alarmTitle\":\"noahtest-215\",\"beginTime\":\"2021-05-27T09:53:51.294+08:00\"," +
            "\"appSystem\":\"tdx\",\"alarmObjectValue\":\"122_dev_test_noahtest-215\",\"ruleId\":\"122\"}";

    public static void main(String[] args) {

        AlarmEventPojo alarmEventPojo = JSON.parseObject(a, new TypeReference<AlarmEventPojo>() {
        });

        System.out.println(alarmEventPojo);
    }
}
