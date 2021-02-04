package com.github.xiesen.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 优雅关闭线程
 *
 * @author 谢森
 * @since 2021/2/4
 */
@Slf4j
public class TreadClose {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                log.debug("run start ...");
                log.debug("当前线程状态: {}", Thread.currentThread().getState());
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                log.debug("run end...");
            }
        }, "A");

        t1.start();

        log.debug("t1 状态是: {}", t1.getState());

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();
    }
}
