package org.xiesen;

import cn.hutool.core.util.ReUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiesen
 * @title: Test4
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/9/15 10:30
 */
public class Test4 {
    private static final String regex = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
    private static final Pattern compile = Pattern.compile(regex);
    private static final String clientRegex = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
    private static final Pattern clientCompile = Pattern.compile(clientRegex);

    public static void main(String[] args) {
        String data = "asdfghjklsdfghjkl@192.168.1222.1";
        String data1 = "192.168.2.149:9090";
        String regex2 = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";

        System.out.println("============= remote ip match ===============");
        Matcher matcher = compile.matcher(data);
        if (matcher.find()) {
            System.out.println(matcher.group());
        } else {
            System.out.println("no match ip");
        }

        System.out.println("============= client ip match ===============");
        Matcher matcher1 = clientCompile.matcher(data1);
        if (matcher1.find()) {
            System.out.println(matcher1.group());
        } else {
            System.out.println("no match ip");
        }
    }
}
