package com.github.xiesen.mock.test;

/**
 * @author xiesen
 * @title: Collector
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/20 9:44
 */
public class Collector<R> {

    public void collector(R r) {

        System.out.println(r);
        throw new RuntimeException("异常了");
    }
}
