package com.github.xiesen.mock.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author xiesen
 * @title: TestBlockingQueue
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/25 14:03
 */
public class TestBlockingQueue {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> genericRecordQueue = new LinkedBlockingQueue<String>();
        genericRecordQueue.put("aaa");
        genericRecordQueue.put("bbb");
        genericRecordQueue.offer("ccc", 3, TimeUnit.SECONDS);
        for (int i = 0; i < genericRecordQueue.size(); i++) {
            String s = genericRecordQueue.poll(3, TimeUnit.SECONDS);
            System.out.println(s);
        }


    }
}
