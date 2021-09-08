package org.xiesen;

import com.github.xiesen.TestLog;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/22 20:08
 */
public class Test01 {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(Test01.class);
    public static void run() {
        log.error("Test01 error");
        log.info("Test01 info");
        log.debug("Test01 debug");
    }
}
