package com.github.xiesen.common.test;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }
    private static void test3() {
        List<String> list = new ArrayList<>();
        list.add("callspermin");
        list.add("errorrate");
        list.add("totalcalls");
        list.add("totalduration");
        list.add("totalerrors");
        list.add("meanduration");
        list.sort(String::compareTo);
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            System.out.println(s + " double,");
//            builder.append(s).append(",");
        }
        System.out.println(builder.toString());
    }

    private static void test2() {
        List<String> list = new ArrayList<>();
        list.add("appsystem");
        list.add("appprogramname");
        list.add("clustername");
        list.add("servicename");
        list.add("sourceappsystem");
        list.add("sourceservicename");
        list.add("serviceId");
        list.add("hostname");
        list.add("ip");
        list.sort(String::compareTo);
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            System.out.println(s + " varchar,");
//            builder.append(s).append(",");
        }
        System.out.println(builder.toString());
    }

    private static void test1() {
        List<String> list = new ArrayList<>();
        list.add("sourceappprogramname");
        list.add("sourcehostname");
        list.add("sourceservicename");
        list.add("endpoint");
        list.add("sourceserviceinstance");
        list.add("sourceip");
        list.add("sourceendpoint");
        list.add("ip");
        list.add("appsystem");
        list.add("hostname");
        list.add("appprogramname");
        list.add("servicename");
        list.add("serviceinstance");
        list.add("sourceappsystem");
        list.add("status");
        list.sort(String::compareTo);
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
//            System.out.println(s + " varchar,");
            builder.append(s).append(",");
        }
        System.out.println(builder.toString());
    }
}
