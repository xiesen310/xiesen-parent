package com.github.xiesen.junit.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/22 19:27
 */
public class Test2 {
    private static Map<Integer, String> map = new HashMap<>();

    public static void main(String[] args) {
        String uploadPath = "D:/tmp/upload";
        if (!uploadPath.endsWith("/")) {
            uploadPath = uploadPath + "/";
        }
        System.out.println(uploadPath);
    }
}
