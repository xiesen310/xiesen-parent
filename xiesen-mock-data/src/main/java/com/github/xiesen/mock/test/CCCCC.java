package com.github.xiesen.mock.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CCCCC {
    public static final String REG_EX = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？·]";
    public static final String REPLACEMENT = "_";

    public static String filterString(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile(REG_EX);
        Matcher m = p.matcher(str);
        return m.replaceAll(REPLACEMENT).trim();
    }

    public static String filterString2(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile(REG_EX);
        Matcher m = p.matcher(str);
        return m.replaceAll("\\\\$0").trim();
    }

    public static void main(String[] args) {
        String str = "a:b@c$aa.xx+dd^cc[test]ddd(abc)|{xyz},海清，,@#$%^&*()!!`?./";
        try {
            System.out.println(filterString2(str));
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        // 输出结果 a\:b\@c\$aa.xx\+dd\^cc\[test\]ddd\(abc\)\|\{xyz\}
    }
}
