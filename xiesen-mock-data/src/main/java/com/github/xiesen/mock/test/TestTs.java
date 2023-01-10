package com.github.xiesen.mock.test;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * @author xiesen
 * @title: TestTs
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/12/4 15:02
 */
public class TestTs {

    public static void main(String[] args) {
        String ts = "2022-12-04T06:44:47.126Z";

        final String s = beatTimestampFormat(ts);
        System.out.println(s);
    }

    private static String beatTimestampFormat(String beatTimestamp) {
        LocalDateTime localDateTime = LocalDateTimeUtil.parse(beatTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        final LocalDateTime dateTime = LocalDateTimeUtil.offset(localDateTime, +8, ChronoUnit.HOURS);
        return LocalDateTimeUtil.format(dateTime, DatePattern.NORM_DATETIME_MS_PATTERN);
    }
}
