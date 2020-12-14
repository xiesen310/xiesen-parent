package com.github.xiesen.flink.test;

import org.joda.time.DateTime;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/2 9:51
 */
public class Test03 {
    public static void main(String[] args) {
        System.out.println(new DateTime("2033-02-13T00:37:38.778Z").getMillis());
        System.out.println(new DateTime("0000-00-00T00:00:00.000Z").getMillis());
    }


}
