package com.github.xiesen.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ShareData {
    private int number = 0;

    synchronized public void add() {
        while (number != 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ++number;
        log.info("{} {}", Thread.currentThread().getName(), number);
        this.notifyAll();
    }

    synchronized public void deAdd() {
        while (number == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        --number;
        log.info("{} {}", Thread.currentThread().getName(), number);
        this.notifyAll();
    }

}

/**
 * @author 谢森
 * @since 2021/2/4
 */
@Slf4j
public class ThreadTest2 {
    public static void main(String[] args) {
        final ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.add();
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.deAdd();
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.deAdd();
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.add();
            }
        }, "D").start();

    }
}
