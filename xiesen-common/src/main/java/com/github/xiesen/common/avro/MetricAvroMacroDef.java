package com.github.xiesen.common.avro;

/**
 * @author xiesen
 * @Description 指标集 schema 定义
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:33
 */
public class MetricAvroMacroDef {
    public static String metadata = "{\n" +
            "    \"namespace\": \"com.zork.metrics\",\n" +
            "    \"type\": \"record\",\n" +
            "    \"name\": \"metrics\",\n" +
            "    \"fields\": [\n" +
            "        {\n" +
            "            \"name\": \"metricsetname\",\n" +
            "            \"type\": [\n" +
            "                \"string\",\n" +
            "                \"null\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"timestamp\",\n" +
            "            \"type\": [\n" +
            "                \"string\",\n" +
            "                \"null\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"dimensions\",\n" +
            "            \"type\": [\n" +
            "                \"null\",\n" +
            "                {\n" +
            "                    \"type\": \"map\",\n" +
            "                    \"values\": \"string\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"metrics\",\n" +
            "            \"type\": [\n" +
            "                \"null\",\n" +
            "                {\n" +
            "                    \"type\": \"map\",\n" +
            "                    \"values\": \"double\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static void main(String[] args) {
        System.out.println(metadata);
    }
}
