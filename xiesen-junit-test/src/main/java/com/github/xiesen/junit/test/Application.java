package com.github.xiesen.junit.test;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/27 19:25
 */
public class Application {
    public static void main(String[] args) throws Exception {
        File file = new File("D:/tmp/flink-1.11.3-bin-scala_2.11.tgz");
        FileInputStream fileInputStream = new FileInputStream(file);
        String hex = DigestUtils.sha512Hex(fileInputStream);
        System.out.println(hex);
    }

}
