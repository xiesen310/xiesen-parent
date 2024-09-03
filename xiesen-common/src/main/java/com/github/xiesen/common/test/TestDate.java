package com.github.xiesen.common.test;

import com.github.xiesen.common.utils.DateUtil;
import org.joda.time.DateTime;

public class TestDate {
    private final static int NUM10 = 10;
    public static void main(String[] args) {
        long time = 0L;
        String timestamp = "" + System.currentTimeMillis();
        timestamp = DateUtil.getUTCTimeStr();
        time = new DateTime(timestamp).getMillis();
        System.out.println(time);
        String tsStr = timestampToDate(time);
        System.out.println(tsStr);
    }
    public static String timestampToDate(long timestamp) {
        if (String.valueOf(timestamp).length() == NUM10) {
            timestamp = timestamp * 1000;
        }
        return new DateTime(timestamp).toString();
    }
}
