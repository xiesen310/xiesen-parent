package com.github.xiesen.flink.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.nio.charset.StandardCharsets;

/**
 * @author xiesen
 * @title: JsonStringSchema
 * @projectName xiesen-parent
 * @description: JsonStringSchema
 * @date 2022/8/21 13:49
 */
public class JsonStringSchema implements KafkaDeserializationSchema<String>, SerializationSchema<String> {
    @Override
    public boolean isEndOfStream(String nextElement) {
        return false;
    }

    @Override
    public String deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (record != null) {
            jsonObject.put("topic", record.topic());
            jsonObject.put("partition", record.partition());
            jsonObject.put("offset", record.offset());
            jsonObject.put("timestamp", record.timestamp());
            jsonObject.put("key", record.key());
            jsonObject.put("value", new String(record.value(), StandardCharsets.UTF_8));
        }
        return jsonObject.toJSONString();
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return BasicTypeInfo.STRING_TYPE_INFO;
    }

    @Override
    public byte[] serialize(String element) {
        return element.getBytes(StandardCharsets.UTF_8);
    }
}
