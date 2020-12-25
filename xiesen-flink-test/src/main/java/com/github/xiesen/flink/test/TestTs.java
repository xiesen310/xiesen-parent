package com.github.xiesen.flink.test;

import org.joda.time.DateTime;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/23 10:36
 */
public class TestTs {
    public static void main(String[] args) {
        String timestamp = String.valueOf("2020-12-21 07:59:05.410");
//        String timestamp = String.valueOf("");
        timestamp = getIsoTime(timestamp);
        if (timestamp == null) {
            String failReason = "timestamp can't cast to datetime:" + timestamp;

        } else {

        }
    }

    /**
     * 获取iso时间
     *
     * @param str
     * @return
     */
    public static String getIsoTime(String str) {
        if (str == null) {
            return str;
        }
        str = str.trim();
        try {
            DateTime datetime;
            // 1能管到2033年
            if (str.length() == 13 && str.startsWith("1")) {
                long t = Long.parseLong(str);
                datetime = new DateTime(t);
            } else if (str.length() == 10 && str.startsWith("1")) {
                // 秒数，1能管到2033年
                long t = Long.parseLong(str) * 1000;
                datetime = new DateTime(t);
            } else {
                datetime = new DateTime(str);
            }
            return datetime.toDateTimeISO().toString();
        } catch (Exception ex) {
            return null;
        }
    }

}
