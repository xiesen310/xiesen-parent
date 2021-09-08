package com.github.xiesen;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/22 19:21
 */
public class TestLog {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(TestLog.class);
    public static void main(String[] args) {
        Logger.getLogger("com").setLevel(Level.OFF);
        log.info("我是 info");
        log.warn("我是 warn");
        log.debug("我是 debug");
        log.error("我是 error");
        System.out.println("aaa");
        System.err.println("bbbb");
    }
}
