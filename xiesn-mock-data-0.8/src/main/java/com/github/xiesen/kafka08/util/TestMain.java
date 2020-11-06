package com.github.xiesen.kafka08.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/10/28 10:18
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println(DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"));
    }
}
