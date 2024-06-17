package com.github.xiesen.common.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;


public class CalculatedAge {
    public static void main(String[] args) {
        // 指定日期
        String specifiedDate = "2023-12-25";
        // 计算时间差
        long daysDiff = DateUtil.betweenDay(DateUtil.parse(specifiedDate), DateUtil.date(), true);
        // 输出时间差
        Console.log("儿子今天出生 {} 天了", daysDiff);
    }
}
