package com.github.xiesen.mock.test;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiesen
 */
public class TestDate {
    public static void main(String[] args) {
        final long l = System.currentTimeMillis();
        System.out.println(l);
        final DateTime date = DateUtil.date(l);
        System.out.println(date);

        final String s = date.toString(DatePattern.NORM_DATETIME_MS_PATTERN);
        System.out.println(s);

        String encode = DigestUtil.md5Hex("abcdefg");

        String encode2 = SecureUtil.md5("abcdefg");
        System.out.println(encode);
        System.out.println(encode2);

        System.out.printf("===============");

        String timestamp = "";

//        org.joda.time.DateTime(timestamp).toDate().getTime();


    }
}
