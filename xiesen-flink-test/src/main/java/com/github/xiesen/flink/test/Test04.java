package com.github.xiesen.flink.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/14 9:50
 */
public class Test04 {
    private final Logger logger = LoggerFactory.getLogger(Test04.class);

    public static void main(String[] args) {
        Test04 test04 = new Test04();
        test04.handle("ok");
        test04.handle("OK");
        test04.handle("PROBLEM");
        test04.handle("problem");
        test04.handle("NOBROKER");
        test04.handle("nobroker");
        test04.handle("aaa");
        test04.handle("ss");

    }

    private Boolean handle(String status) {
        // 告警状态1、PROBLEM=告警2、OK=恢复3、NOBROKER=不压缩不告警
        if (!checkStatus(status)) {
            if (logger.isDebugEnabled()) {
                logger.debug("[zorkdata] STATUS IS NOT PROBLEM OR OK, 当前状态是 {}", status);
            }
            System.out.println("不发送");
            return false;
        } else {
            System.out.println("发送");
            return true;
        }
    }

    private boolean checkStatus(String status) {
        if (status.equalsIgnoreCase("ok") || status.equalsIgnoreCase("PROBLEM")) {
            return true;
        } else {
            return false;
        }
    }

}
