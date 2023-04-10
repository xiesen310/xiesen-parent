package com.github.xiesen.mock.ops;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 创建标准指标*
 *
 * @author xiesen
 */
public class CreateStandardMetric {
    public static final String CREATE_DEFINE_URL = "http://localhost:8888/metricDefinition/create";
    public static final String CREATE_DEFINE_PARAM = "{\n" +
            "  \"alarmInfo\": {\n" +
            "    \"recommendAlarmStrategy\": \"5分钟3次\",\n" +
            "    \"recommendAlarmThreshold\": {\n" +
            "      \"ceiling\": 90,\n" +
            "      \"risk\": 80,\n" +
            "      \"safety\": 60\n" +
            "    }\n" +
            "  },\n" +
            "  \"baseInfo\": {\n" +
            "    \"comment\": \"cpu_used_pct_aaa\",\n" +
            "    \"dataFormat\": \"数值\",\n" +
            "    \"definitionAndAlgorithm\": \"程序运行使用的CPU时间/总CPU时间*100%\",\n" +
            "    \"displayName\": \"cpu使用率\",\n" +
            "    \"name\": \"cpu_used_pct_aaa\",\n" +
            "    \"unit\": \"%\"\n" +
            "  },\n" +
            "  \"dimensions\": [\n" +
            "    {\n" +
            "      \"comment\": \"系统名称\",\n" +
            "      \"name\": \"appsystem\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"comment\": \"主机名称\",\n" +
            "      \"name\": \"hostname\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"comment\": \"ip地址\",\n" +
            "      \"name\": \"ip\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"labels\": [\n" +
            "    {\n" +
            "      \"labelName\": \"一级分类\",\n" +
            "      \"labelValue\": \"主机\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"labelName\": \"指标对象\",\n" +
            "      \"labelValue\": \"CPU\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"labelName\": \"负责团队\",\n" +
            "      \"labelValue\": \"国泰运维\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static void main(String[] args) {
        for (int i = 500; i < 1000; i++) {
            final JSONObject jsonObject = JSON.parseObject(CREATE_DEFINE_PARAM);
            final JSONObject baseInfo = jsonObject.getJSONObject("baseInfo");
            baseInfo.put("comment", "cpu_used_pct_" + i);
            baseInfo.put("name", "cpu_used_pct_" + i);
            jsonObject.put("baseInfo", baseInfo);
            final HttpResponse response = HttpUtil.createPost(CREATE_DEFINE_URL)
                    .header("Authorization", "Bearer e3df8d92-d02b-4b32-8c31-51ac14ff7eb4")
                    .body(jsonObject.toJSONString()).execute();
            System.out.println(response);
        }

    }
}
