package com.github.xiesen.flink.test;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

import java.util.Properties;

/**
 * @author xiese
 * @Description Flink kafka demo
 * @Email xiesen310@163.com
 * @Date 2020/6/30 14:50
 */
public class FlinkKafkaDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "kafka01:9092,kafka02:9092,kafka03:9092");
        FlinkKafkaConsumer011<String> kafkaConsumer011 = new FlinkKafkaConsumer011<>("log2metric1y", new SimpleStringSchema(), properties);
        DataStreamSource<String> source = env.addSource(kafkaConsumer011).setParallelism(9);
        kafkaConsumer011.setStartFromEarliest();

        FlinkKafkaProducer011<String> kafkaProducer011 = new FlinkKafkaProducer011<>("xiesen", new SimpleStringSchema(), properties);
        source.addSink(kafkaProducer011).setParallelism(2);

        env.execute();
    }
}
