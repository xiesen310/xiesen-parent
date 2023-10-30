package org.xiesen;

import cn.hutool.core.date.DateUtil;

import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author xiesen
 */
public class TimestampConversionExample {

    public static void main(String[] args) {
        String dateString = "Oct 11 16:30:00";
        final String[] s = dateString.split(" ");
        final String monthAbbreviation = s[0];

        // 将月份缩写转换为阿拉伯数字
        int monthNumber = Month.from(DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH).parse(monthAbbreviation)).getValue();
        System.out.println(monthNumber);

        final String replace = dateString.replace(monthAbbreviation, String.valueOf(monthNumber));
        int currentYear = Year.now().getValue();

        StringBuilder builder = new StringBuilder();
        builder.append(currentYear).append(" ").append(replace);
        System.out.println("新时间: " + builder.toString());

        final long time = DateUtil.parse(builder.toString(), "yyyy MM dd HH:mm:ss").getTime();
        System.out.println(time);
    }
}
