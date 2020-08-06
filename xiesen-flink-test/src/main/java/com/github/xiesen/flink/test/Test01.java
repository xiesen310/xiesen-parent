package com.github.xiesen.flink.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.Charsets;

import java.net.URLDecoder;
import java.util.List;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/7/27 13:26
 */
public class Test01 {
    public Integer eval(String s, String fields) {
        return s.startsWith(fields) ? 1 : 0;
    }

    public static void main(String[] args) throws Exception {
        Test01 test01 = new Test01();
        Integer eval = test01.eval("1", "1");
        System.out.println(eval);
    }
}
