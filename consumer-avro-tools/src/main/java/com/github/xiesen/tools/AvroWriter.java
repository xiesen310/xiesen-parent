package com.github.xiesen.tools;

import com.github.xiesen.common.avro.AvroDeserializerFactory;
import com.github.xiesen.common.avro.AvroSerializerFactory;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.util.hash.Hash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/9/29 10:35
 */
public class AvroWriter {
    public static void main(String[] args) {

        String logTypeName = "test";
        String timestamp = DateUtil.getUTCTimeStr();
        String source = "/var/log/a.log";
        String offset = "1111";
        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("ip", "192.168.1.1");
        Map<String, Double> metrics = new HashMap<>();
        Map<String, String> normalFields = new HashMap<>();
        normalFields.put("message", "aaaaaaaaa");
        byte[] bytes = AvroSerializerFactory.getLogAvroSerializer().serializingLog(logTypeName, timestamp, source,
                offset, dimensions, metrics, normalFields);

        GenericRecord deserializing = AvroDeserializerFactory.getLogsDeserializer().deserializing(bytes);
        System.out.println(deserializing);

    }
}
