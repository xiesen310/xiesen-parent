package com.github.xiesen.mock.test;

import cn.hutool.crypto.SecureUtil;

public class Md5Test {
    public static void main(String[] args) {
        String s = "123456";
        long offset = 123;
        String path = "/var/log/message";
        String md5 = SecureUtil.md5(s + offset + path);
        System.out.println(md5);
    }
}
