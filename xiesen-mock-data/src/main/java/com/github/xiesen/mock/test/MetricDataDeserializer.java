package com.github.xiesen.mock.test;

import com.github.xiesen.common.avro.AvroDeserializer;
import com.github.xiesen.common.avro.AvroDeserializerFactory;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.utils.Bytes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiesen
 * @title: MetricDataDeserializer
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/2/18 17:21
 */
public class MetricDataDeserializer implements Deserializer<String> {

    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    public String deserialize(String topic, byte[] data) {
        if (data == null)
            return null;
        AvroDeserializer metricDeserializer = AvroDeserializerFactory.getMetricDeserializer();
        GenericRecord deserializing = metricDeserializer.deserializing(data);
        String metricsetname = String.valueOf(deserializing.get("metricsetname"));
        String timestamp = String.valueOf(deserializing.get("timestamp"));
        Map<String, String> dimensions = new HashMap<>(16);
        Map<String, Object> tmp = (Map<String, Object>) deserializing.get("dimensions");
        for (Map.Entry entry : tmp.entrySet()) {
            dimensions.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return result(metricsetname, timestamp, dimensions);
    }

    public void close() {

    }

    private String result(String metricsetname, String timestamp, Map<String, String> dimensions) {
        StringBuilder builder = new StringBuilder();
        builder.append(metricsetname).append(timestamp);
        dimensions.forEach((k, v) ->
                builder.append(k).append(v)
        );
        return builder.toString();
    }
}