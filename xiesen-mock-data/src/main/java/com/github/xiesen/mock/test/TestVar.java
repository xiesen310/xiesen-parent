package com.github.xiesen.mock.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiesen
 * @title: TestVar
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/12/1 14:12
 */
public class TestVar {
    public static String getKey(String g) {
        return g.substring(2, g.length() - 1);
    }

    public static void main(String[] args) {
//        String content = "test_${dimensions.appsystem}_${dimensions.hostname}";
        String content = "test";

        if (isContainsVariable(content)) {
            final List<String> list = parseVariable(content);
            for (String s : list) {
                System.out.println(s);
            }
        } else {
            System.out.println("不包含变量");
        }

    }


    public static boolean isContainsVariable(String content) {
        String pattern = "\\$\\{[^}]+}";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        return m.find();
    }

    public static List<String> parseVariable(String content) {
        List<String> result = new ArrayList<>();
        String pattern = "\\$\\{[^}]+}";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String var = getKey(m.group(0));
            result.add(var);
        }
        return result;
    }
}
