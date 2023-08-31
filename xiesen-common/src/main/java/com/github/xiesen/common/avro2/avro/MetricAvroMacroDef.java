package com.github.xiesen.common.avro2.avro;

/**
 * @author xiesen
 * @Description 指标集 schema 定义
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:33
 */
public class MetricAvroMacroDef {
    public static String metadata = "{\n" +
            "    \"namespace\":\"com.data.metrics\",\n" +
            "    \"type\":\"record\",\n" +
            "    \"name\":\"metric\",\n" +
            "    \"fields\":[\n" +
            "        {\n" +
            "            \"name\":\"name\",\n" +
            "            \"type\":[\n" +
            "                \"string\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"ts\",\n" +
            "            \"type\":[\n" +
            "                \"long\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"dimensions\",\n" +
            "            \"type\":[\n" +
            "                {\n" +
            "                    \"type\":\"map\",\n" +
            "                    \"values\":\"string\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"value\",\n" +
            "            \"type\":[\n" +
            "                \"double\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static void main(String[] args) {
        System.out.println(metadata);
    }
}
