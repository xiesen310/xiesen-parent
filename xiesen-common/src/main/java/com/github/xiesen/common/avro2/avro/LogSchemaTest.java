package com.github.xiesen.common.avro2.avro;

import com.github.xiesen.common.utils.DateUtil;
import org.apache.avro.generic.GenericRecord;
import org.mortbay.util.ajax.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiesen
 */
public class LogSchemaTest {
    private static Map<String, String> getRandomDimensions() {
        Map<String, String> dimensions = new HashMap<>(7);
        dimensions.put("hostname", "DVJTY4-WEB406-zzg");
        dimensions.put("appprogramname", "DVJTY4-WEB406_80");
        dimensions.put("servicecode", "WEB");
        dimensions.put("clustername", "nginx");
        dimensions.put("servicename", "nginx");
        dimensions.put("ip", "192.168.1.1");
        return dimensions;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("message", "183.95.248.189 - - [23/Jul/2020:08:26:32 +0800] \"GET " +
                "/gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ" +
                "%3D%3D HTTP/1.1\" 200 872 ");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        return normalFields;
    }

    public static void main(String[] args) {
        String name = "default_analysis_template";
        Long ts = System.currentTimeMillis();
        String source = "/var/log/nginx/access.log";
        Long offset = new Random().nextLong();

        Map<String, String> dimensions = getRandomDimensions();
        Map<String, String> normalFields = getRandomNormalFields();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("ts", ts);
        map.put("source", source);
        map.put("offset", offset);
        map.put("dimensions", dimensions);
        map.put("normalFields", normalFields);
        System.out.println("日志原始数据: " + JSON.toString(map));

        byte[] bytesMap = AvroSerializerFactory.getLogAvroSerializer()
                .serializingLog(map);

        System.out.println("日志序列化Map类型数据: " + new String(bytesMap));

        GenericRecord recordMap = AvroDeserializerFactory.getLogsDeserializer().deserializing(bytesMap);
        System.out.println("日志反序列化Map类型数据: " + recordMap);

        System.out.println("========================================");
        
        byte[] bytes = AvroSerializerFactory.getLogAvroSerializer()
                .serializingLog(name, ts, source,
                        offset, dimensions, normalFields);
        System.out.println("日志序列化数据: " + new String(bytes));

        GenericRecord record = AvroDeserializerFactory.getLogsDeserializer().deserializing(bytes);
        System.out.println("日志反序列化数据: " + record);
    }
}
