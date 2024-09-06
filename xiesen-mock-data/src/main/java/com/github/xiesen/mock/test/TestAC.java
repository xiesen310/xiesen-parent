package com.github.xiesen.mock.test;

import java.util.ArrayList;
import java.util.List;

public class TestAC {
    public static void main(String[] args) {
        double floor = Math.floor(Math.floorDiv(1, 3));
        System.out.println(floor);

        List<String> list = new ArrayList<>(10);
        System.out.println(list.size());
    }
}
