package com.github.xiesen.cdc.schema;

import com.alibaba.fastjson.JSONObject;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

/**
 * @author xiesen
 * @title: JsonStringDeserializationSchema
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/11/26 15:10
 */

@SuppressWarnings("all")
public class JsonStringDeserializationSchema implements DebeziumDeserializationSchema<String> {

    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<String> collector) throws Exception {
        String topic = sourceRecord.topic();
        final String[] arr = topic.split("\\.");
        String source = arr[0];
        String database = arr[1];
        String tableName = arr[2];
        final Envelope.Operation operation = Envelope.operationFor(sourceRecord);
        final Struct value = (Struct) sourceRecord.value();

        final Struct after = value.getStruct("after");

        final JSONObject afterData = new JSONObject();
        final JSONObject afterSchema = new JSONObject();
        if (null != after) {
            final Schema schema = after.schema();
            for (Field field : schema.fields()) {
                final String type = schema.field(field.name()).schema().type().name().toLowerCase();
                afterSchema.put(field.name(), type);
                final Object o = after.get(field);
                afterData.put(field.name(), o);
            }
        }

        final Struct before = value.getStruct("before");
        final JSONObject beforeData = new JSONObject();
        final JSONObject beforeSchema = new JSONObject();
        if (null != before) {
            final Schema schema = before.schema();
            for (Field field : schema.fields()) {
                final String type = schema.field(field.name()).schema().type().name().toLowerCase();
                beforeSchema.put(field.name(), type);
                final Object o = before.get(field);
                beforeData.put(field.name(), o);
            }
        }


        final JSONObject result = new JSONObject();
        result.put("operation", operation.toString().toLowerCase());
        result.put("after", afterData);
        result.put("before", beforeData);
        result.put("database", database);
        result.put("table", tableName);
        result.put("source", source);

        if (beforeSchema.size() > 0) {
            result.put("schema", beforeSchema);
        } else {
            result.put("schema", afterSchema);
        }

        collector.collect(result.toJSONString());
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return BasicTypeInfo.STRING_TYPE_INFO;
    }
}
