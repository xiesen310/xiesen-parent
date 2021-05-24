package com.github.xiesen.junit.test;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author 谢森
 * @since 2021/4/18
 */
public class HutoolsDateTimeTest {

    public static void main(String[] args) {
        String format = DateUtil.format(new Date(), "yyyy/MM/dd");
        System.out.println(format);
    }
}
