package org.xiesen;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author xiesen
 * @title: Test3
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/17 14:03
 */
@Slf4j
public class Test3 {

    public static void main(String[] args) {
        int i = new Random().nextInt(5);
        System.out.println(i);
    }

}
