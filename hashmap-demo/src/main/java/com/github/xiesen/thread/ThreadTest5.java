package com.github.xiesen.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class Calculate {
    private int number = 0;
    Object obj = new Object();

    public void add() {
        synchronized (obj) {
            number++;
            log.debug("number is {}", number);

        }
    }
}


/**
 * @author 谢森
 * @since 2021/2/5
 */
public class ThreadTest5 {
    public static void main(String[] args) {
        Calculate calculate = new Calculate();
        new Thread(() -> {
            calculate.add();
        }, "t1").start();
    }
}
