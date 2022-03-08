package com.github.xiesen.mock.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiesen
 * @title: Test02
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/2/25 9:50
 */
public class Test02 {

    public static ConcurrentHashMap<Integer, AlarmMaintenancePeriod> ruleMap = null;

    public static void main(String[] args) {
        ruleMap = new ConcurrentHashMap<>();
        Iterator<AlarmMaintenancePeriod> iterator = ruleMap.values().iterator();
        while (iterator.hasNext()) {
            AlarmMaintenancePeriod alarmMaintenancePeriod = iterator.next();
            if (alarmMaintenancePeriod.getAlarmRuleList().contains(null)) {
                System.out.println("true");
            } else {
                System.out.println("false");
            }
        }

        AlarmMaintenancePeriod alarmMaintenancePeriod = new AlarmMaintenancePeriod();
        alarmMaintenancePeriod.setComment("aa");
        deal(alarmMaintenancePeriod);
        System.out.println(alarmMaintenancePeriod);
        boolean contains = alarmMaintenancePeriod.getAlarmRuleList().contains("1");
        System.out.println(contains);

        List<String> list = new ArrayList<>();
        list.add("aaa");
        System.out.println(list.contains(null));
    }

    private static void deal(AlarmMaintenancePeriod alarmMaintenancePeriod) {
        alarmMaintenancePeriod.setBizId(111);
        alarmMaintenancePeriod.setAlarmRuleList(new ArrayList<>());
    }
}
