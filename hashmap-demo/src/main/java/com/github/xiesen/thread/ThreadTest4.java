package com.github.xiesen.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
class Phone {
    synchronized public void getIos() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("-------getIos....");
    }

    synchronized public void getAndroid() {
        log.debug("-------getAndroid....");
    }

    public void getHello() {
        log.debug("-------getHello....");
    }
}

/**
 * 普通方法调用, 先输出ios 还是 android  ios
 * synchronized 方法调用, 先输出ios 还是 android  ios
 * ios 中停顿5s,synchronized 方法调用, 先输出ios 还是 android  ios
 * 添加一个 hello普通方法,一个 synchronized 方法调用,先输出 hello,还是 ios hello
 *
 * @author 谢森
 * @since 2021/2/5
 */
public class ThreadTest4 {
    public static void main(String[] args) {

        Phone phone = new Phone();
        new Thread(() -> {
            phone.getIos();
        }, "t1").start();

        new Thread(() -> {
            phone.getHello();
        }, "t1").start();
    }
}
