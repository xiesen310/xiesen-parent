package com.github.xiesen.common.avro2.avro;

/**
 * @author xiese
 * @Description 日志集 schema 定义
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:33
 */
public class LogAvroMacroDef {
    public static String metadata = "{\n" +
            "\t\"namespace\": \"com.data.logs\",\n" +
            "\t\"type\": \"record\",\n" +
            "\t\"name\": \"logs\",\n" +
            "\t\"fields\": [{\n" +
            "\t\t\t\"name\": \"name\",\n" +
            "\t\t\t\"type\": [\n" +
            "\t\t\t\t\"string\"\n" +
            "\t\t\t]\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"ts\",\n" +
            "\t\t\t\"type\": [\n" +
            "\t\t\t\t\"long\"\n" +
            "\t\t\t]\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"source\",\n" +
            "\t\t\t\"type\": [\n" +
            "\t\t\t\t\"string\"\n" +
            "\t\t\t]\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"offset\",\n" +
            "\t\t\t\"type\": [\n" +
            "\t\t\t\t\"long\"\n" +
            "\t\t\t]\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"dimensions\",\n" +
            "\t\t\t\"type\": [{\n" +
            "\t\t\t\t\"type\": \"map\",\n" +
            "\t\t\t\t\"values\": \"string\"\n" +
            "\t\t\t}]\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"normalFields\",\n" +
            "\t\t\t\"type\": [\n" +
            "\t\t\t\t\"null\",\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"type\": \"map\",\n" +
            "\t\t\t\t\t\"values\": \"string\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t]\n" +
            "\t\t}\n" +
            "\t]\n" +
            "}";

    public static void main(String[] args) {
        System.out.println(metadata);
    }
}
