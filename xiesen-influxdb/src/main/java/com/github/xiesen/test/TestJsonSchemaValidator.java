package com.github.xiesen.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.core.report.ProcessingReport;

public class TestJsonSchemaValidator {
    public static void main(String[] args) throws Exception {
        // JSON Schema定义
//        String schemaJson = "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\",\"minimum\":18}},\"required\":[\"name\",\"age\"]}"; // 上面定义的JSON Schema字符串
        String schemaJson = "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"metricSetName\":{\"type\":\"string\"},\"timestamp\":{\"type\":\"string\"},\"metrics\":{\"type\":\"object\",\"delay\":{\"type\":\"number\"},\"used\":{\"type\":\"number\"}},\"dimensions\":{\"type\":\"object\",\"appsystem\":{\"type\":\"string\"},\"ip\":{\"type\":\"string\"}}},\"required\":[\"metricSetName\",\"timestamp\",\"metrics\",\"dimensions\"]}";
        // JSON数据
//        String jsonData = "{\"name\": \"Alice\", \"age\": 20}";
        String jsonData = "{\"metricSetName\":\"aa\",\"timestamp\":\"123\",\"metrics\":{\"delay\":1.0,\"used\":0.8},\"dimensions\":{\"appsystem\":\"jzjy\",\"ip\":\"192.168.1.1\"}}";

        // 使用Jackson将JSON Schema和数据转换为JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schemaNode = mapper.readTree(schemaJson);
        JsonNode dataNode = mapper.readTree(jsonData);

        // 创建JsonSchemaFactory并加载JSON Schema
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonSchema jsonSchema = factory.getJsonSchema(schemaNode);

        // 验证数据
        ProcessingReport report;
        try {
            report = jsonSchema.validate(dataNode);
            if (report.isSuccess()) {
                System.out.println("根据模式提供的JSON数据是有效的");
            } else {
                System.out.println("提供的JSON数据无效。错误:");
                for (ProcessingMessage message : report) {
                    System.out.println(message.getMessage());
                }
            }
        } catch (ProcessingException e) {
            e.printStackTrace();
        }
    }
}
