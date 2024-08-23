package com.github.xiesen.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class TestDate {
    public static Set<String> dateRangeTransform(String start, String end) {
        TreeSet<String> result = new TreeSet<>(Collections.reverseOrder());
        try {
            if (StrUtil.isNotBlank(start) && StrUtil.isNotBlank(end)) {
                DateTime startDate = DateUtil.parse(start, "yyyyMMdd");
                DateTime endDate = DateUtil.parse(end, "yyyyMMdd");

                // 确保endDate也包含在结果中
                while (!startDate.equals(endDate) && startDate.isBefore(endDate)) {
                    result.add(DateUtil.format(startDate, "yyyyMMdd"));
                    startDate = DateUtil.offsetDay(startDate, 1);
                }

                // 添加最后一天
                if (!startDate.isAfter(endDate)) {
                    result.add(DateUtil.format(startDate, "yyyyMMdd"));
                }
            } else {
                throw new IllegalArgumentException("Start and end dates must not be blank.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Expected 'yyyyMMdd'.", e);
        }
        return result;
    }


    public static void main(String[] args) {
        Set<String> set = dateRangeTransform("20240814", "20240816");
        System.out.println(set);
    }
}
