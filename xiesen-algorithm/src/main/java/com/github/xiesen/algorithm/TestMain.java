package com.github.xiesen.algorithm;

import com.github.xiesen.algorithm.xxhash.AbstractLongHashFunction;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/31 15:32
 */
public class TestMain {
    public static final String a = "{\"severity\":21,\"extFields\":{\"searchSentence\":\"SELECT mean(\\\"cores\\\") " +
            "AS value  FROM cpu_system_metricbeat WHERE ( \\\"hostname\\\" =~ /\\\\.*/ ) AND ( \\\"ip\\\" =~ /\\\\.*/" +
            " ) AND ( \\\"appsystem\\\" = 'dev_test') AND time >= 1594209600000ms AND time < 1594209720000ms GROUP BY" +
            " time(1m),\\\"hostname\\\",\\\"ip\\\",\\\"appsystem\\\" fill(null)\"," +
            "\"alarmSuppress\":\"alarmSuppress\",\"alarmWay\":\"2,2,2\"," +
            "\"uuid\":\"2a094fd38e894de485ae09820bf5a08c\",\"successFlag\":\"1\",\"expressionId\":\"2\"," +
            "\"sourSystem\":\"1\",\"alarmtime\":\"2020-07-08T20:02:00.000+08:00\",\"actionID\":\"0\"," +
            "\"mergeTag\":\"1\",\"calenderId\":\"1\",\"connectId\":\"2a094fd38e894de485ae09820bf5a08c\"," +
            "\"reciTime\":\"1594209785705\",\"alarmDetailType\":\"1\",\"eventNum\":\"2\",\"revUsers\":\"[]\"}," +
            "\"expressionId\":7,\"sources\":{\"appsystem\":\"tdx\",\"hostname\":\"zorkdata7\",\"sourSystem\":7," +
            "\"ip\":\"192.168.1.7\"},\"alarmTypeName\":\"alarm_metric\",\"metricSetName\":\"cpu_system_metricbeat\"," +
            "\"title\":\"192.168.1.7 指标告警\",\"content\":\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"," +
            "\"status\":\"PROBLEM\",\"timestamp\":\"2021-01-13T11:30:10.105+08:00\"}";

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            AbstractLongHashFunction.xx().hashChars("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms");
    }
}
