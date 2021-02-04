package com.github.xiesen.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
class ShadeData1 {
    private int number = 0;
    private Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void increase() {
        try {
            lock.lock();
            while (number != 0) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ++number;
            log.info("{} {}", Thread.currentThread().getName(), number);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void deIncrease() {
        try {
            lock.lock();
            while (number == 0) {
                condition.await();
            }
            --number;
            log.info("{} {}", Thread.currentThread().getName(), number);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}

/**
 * @author 谢森
 * @since 2021/2/4
 */
public class ThreadTest3 {
    public static void main(String[] args) {

        ShadeData1 shadeData1 = new ShadeData1();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shadeData1.increase();
            }
        }, "A").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shadeData1.increase();
            }
        }, "B").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shadeData1.deIncrease();
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shadeData1.deIncrease();
            }
        }, "D").start();
    }
}
