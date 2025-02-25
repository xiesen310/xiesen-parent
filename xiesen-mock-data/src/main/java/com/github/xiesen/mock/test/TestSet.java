package com.github.xiesen.mock.test;

import java.util.HashSet;
import java.util.Set;

public class TestSet {
    public static void main(String[] args) {
        Set<String> actualObjectNameList = new HashSet<>();
        actualObjectNameList.add("a");
        actualObjectNameList.add("b");
        actualObjectNameList.add("c");
        Set<String> expectedObjectNameList = new HashSet<>();
        expectedObjectNameList.add("a");
        expectedObjectNameList.add("b");

        // 计算交集
        // 注意：此操作会修改set1，使其仅包含两个集合中共有的元素
        Set<String> intersection = new HashSet<>(actualObjectNameList); // 创建一个新Set来保存set1的副本
        intersection.retainAll(expectedObjectNameList);

        // 打印交集结果
        System.out.println("Intersection of set1 and set2: " + intersection);
    }
}
