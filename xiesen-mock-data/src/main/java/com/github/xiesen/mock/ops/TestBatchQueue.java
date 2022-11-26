package com.github.xiesen.mock.ops;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * @author xiesen
 * @title: TestBatchQueue
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/26 11:26
 */
public class TestBatchQueue {
    static BatchQueue batchQueue = new BatchQueue<List<String>>(3, 5000, new Consumer<List<List<String>>>() {
        @Override
        public void accept(List<List<String>> lists) {
            lists.forEach(x -> System.out.println(x));
        }
    });

    public static void main(String[] args) {
        while (true) {
            String line = new Scanner(System.in).nextLine();
            if (line.equals("done")) {
                batchQueue.completeAll();
                break;
            }
            batchQueue.add(line);
        }

    }

    private static void exe(List<String> o) {
        System.out.println("处理数据：" + o);
    }
}
