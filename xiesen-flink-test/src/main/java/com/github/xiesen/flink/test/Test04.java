package com.github.xiesen.flink.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/14 9:50
 */
public class Test04 {
    public static void main(String[] args) {
        Test04 test04 = new Test04();
        List<String> indexStr = test04.getIndex();
        for (String index : indexStr) {
            System.out.println(index);
        }
    }

    private List<String> getIndex() {
        List<String> result = new ArrayList<>();
        return result;
    }

    private String getDataRelationShip() {
        return null;
    }
}
