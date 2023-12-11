package com.github.xiesen.mock.test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestCCCD {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        HashMap<String, String> normalFields = new HashMap<>();
        normalFields.put("message", "normalFields");
        HashMap<String, String> normalfields = new HashMap<>();
        normalfields.put("message", "normalfields");
        map.put("normalFields", normalFields);
        map.put("normalfields", normalfields);
        Console.log("size = {}",map.size());

        boolean notBlank = StrUtil.isBlank("");
        boolean notBlank2 = StrUtil.isBlank(null);
        boolean empty = StrUtil.isEmpty("");
        boolean empty2 = StrUtil.isEmpty(null);
        System.out.println(notBlank);
        System.out.println(notBlank2);
        System.out.println("========");
        System.out.println(empty);
        System.out.println(empty2);

    }
}
