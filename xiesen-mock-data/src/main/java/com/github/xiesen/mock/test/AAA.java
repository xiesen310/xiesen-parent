package com.github.xiesen.mock.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiesen
 */
public class AAA {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        final String s = map.get("servicecode");
        System.out.println(s);
        String indexType = null;
        if ("aa".equalsIgnoreCase(indexType)) {
            System.out.println("aa");
        }else {
            System.out.println("no aa");
        }
    }
}
