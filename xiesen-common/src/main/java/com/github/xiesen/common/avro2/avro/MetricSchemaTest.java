package com.github.xiesen.common.avro2.avro;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.avro.generic.GenericRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiesen
 */
public class MetricSchemaTest {
    private static Map<String, String> getRandomDimensions() {
        Map<String, String> dimensions = new HashMap<>(7);
        dimensions.put("hostname", "DVJTY4-WEB406-zzg");
        dimensions.put("app", "tdx");
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
        String name = "cpu_used";
        Long ts = System.currentTimeMillis();
        final double value = new Random().nextDouble();
        Map<String, String> dimensions = getRandomDimensions();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("ts", ts);
        map.put("dimensions", dimensions);
        map.put("value", value);
        System.out.println("指标原始数据: " + JSON.toJSONString(map));

        byte[] bytesMap = AvroSerializerFactory.getMetricAvroSerializer().serializingMetric(map);
        System.out.println("指标序列化 Map 类型数据: " + new String(bytesMap));

        GenericRecord recordMap = AvroDeserializerFactory.getMetricDeserializer().deserializing(bytesMap);
        System.out.println("指标反序列化 Map 类型数据: " + recordMap);

        System.out.println("=========================");

        byte[] bytes = AvroSerializerFactory.getMetricAvroSerializer().serializingMetric(name, ts, dimensions, value);
        System.out.println("指标序列化数据: " + new String(bytes));

//        GenericRecord record = AvroDeserializerFactory.getMetricDeserializer().deserializing(bytesMap);
//        System.out.println("指标反序列化数据: " + record);


    }
}
