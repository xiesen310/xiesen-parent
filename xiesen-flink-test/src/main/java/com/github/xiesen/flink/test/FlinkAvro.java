package com.github.xiesen.flink.test;

import com.github.xiesen.common.avro.AvroDeserializer;
import com.github.xiesen.common.avro.AvroDeserializerFactory;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.util.Collector;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/9/29 10:06
 */
public class FlinkAvro {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        String filePath = "hdfs://cdh-2:8020/tmp/datawarehouse4/jzjy/kcbp_biz_log/20200929/part-0-0.avro";
        DataSource<String> source = env.readTextFile(filePath);
        source.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                AvroDeserializer logsDeserializer = AvroDeserializerFactory.getLogsDeserializer();
                GenericRecord record = logsDeserializer.deserializing(value.getBytes());
                out.collect(record.toString());
            }
        }).print();
    }
}
