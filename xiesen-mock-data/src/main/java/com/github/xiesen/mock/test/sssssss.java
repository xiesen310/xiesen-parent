package com.github.xiesen.mock.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sssssss {

    /**
     * 提取变量名（支持 ${var} 和 $var 格式）
     *
     * @param input 输入字符串（如 "${servicename}" 或 "$servicename"）
     * @return 提取的变量名（如 "servicename"），未匹配时返回 null
     */
    public static String extractVariableName(String input) {
        // 正则匹配两种模式：${var} 或 $var
        Pattern pattern = Pattern.compile("\\$\\{(\\w+)}|\\$(\\w+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // 返回第一个非空的分组（优先 ${var} 格式）
            return matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
        }
        return null;
    }

    public static void main(String[] args) {

        // 测试用例
        System.out.println(extractVariableName("${servicename}")); // 输出 "servicename"
        System.out.println(extractVariableName("$servicename"));   // 输出 "servicename"
        System.out.println(extractVariableName("invalid_format")); // 输出 null
        System.out.println(extractVariableName("$var}")); // 输出 var
    }
}
