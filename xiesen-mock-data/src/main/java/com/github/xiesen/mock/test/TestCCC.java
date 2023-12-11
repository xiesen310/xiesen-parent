package com.github.xiesen.mock.test;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TestCCC {
    private static final Pattern HEADER_VAL_COMPILE = Pattern.compile("(?<=\\[).*?(?=\\])");
    private static final String HEADER_PREFIX = "headers_";
    private static final String HTTP_HEADERS_STR = "http.headers";

    public static void main(String[] args) {
        String actionKey = "http.headers";
        String actionValue = "fileName=[WHT101_101111111111_11123323232.xml]";
        if (actionKey.equals(HTTP_HEADERS_STR)) {
            String headerVal = actionValue;
            String[] headerArr = headerVal.split("\n");
            for (String header : headerArr) {
                /// 此处不能使用=分隔字段，防止请求头值中包含=导致解析内容错误，通过截取首个=下标字符串的方式进行值获取
                int equalIndex = StrUtil.indexOf(header, '=');
                String headerName = StrUtil.sub(header, 0, equalIndex);
                String headerValue = StrUtil.sub(header, equalIndex + 1, header.length());
                String actualHeaderVal = getActualHeaderVal(headerValue);
//                normalFields.put(HEADER_PREFIX + headerName, actualHeaderVal);
                System.out.println("name=" + (HEADER_PREFIX + headerName) + "; value = " + actualHeaderVal);
            }
        }
    }

    /**
     * 获取实际请求头信息
     *
     * @param headerData
     * @return
     */
    private static String getActualHeaderVal(String headerData) {
        String actualValue = null;
        if (StrUtil.isNotBlank(headerData)) {
            try {
                Matcher matcher = HEADER_VAL_COMPILE.matcher(headerData);
                if (matcher.find()) {
                    actualValue = matcher.group();
                } else {
                    actualValue = headerData;
                }
            } catch (Exception e) {
                log.error("解析请求头值异常:{}", headerData, e.getMessage());
            }
        }
        return actualValue;
    }
}
