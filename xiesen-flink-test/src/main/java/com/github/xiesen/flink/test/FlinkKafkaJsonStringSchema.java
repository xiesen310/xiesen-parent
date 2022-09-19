package com.github.xiesen.flink.test;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.io.IOException;
import java.util.Properties;

/**
 * @author xiese
 * @Description Flink kafka demo
 * @Email xiesen310@163.com
 * @Date 2020/6/30 14:50
 */
public class FlinkKafkaJsonStringSchema {
    private static String topic = "test";
    private static String groupId = "xiesen9";
    private static String brokers = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        /// setCheckpoint(env);
        final ParameterTool params = ParameterTool.fromArgs(args);

        if (params.has("topic")) {
            topic = params.get("topic");
        } else if (params.has("groupId")) {
            groupId = params.get("groupId");
        } else if (params.has("brokers")) {
            brokers = params.get("brokers");
        } else {
            throw new RuntimeException("Executing FlinkKafkaDemo param exception");
        }

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", brokers);
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        FlinkKafkaConsumer<String> kafkaConsumer011 = new FlinkKafkaConsumer<>(topic, new JsonStringSchema(),
                properties);
        DataStreamSource<String> source = env.addSource(kafkaConsumer011);
        kafkaConsumer011.setStartFromGroupOffsets();

        source.print().setParallelism(1);

        env.execute();
    }

    /**
     * 设置 checkpoint
     *
     * @param env
     * @throws IOException
     */
    private static void setCheckpoint(StreamExecutionEnvironment env) throws IOException {
        env.setParallelism(3);
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        // 开启检查点
        env.enableCheckpointing(60 * 1000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        env.setRestartStrategy(RestartStrategies.noRestart());
        // 确保检查点之间有至少500 ms的间隔【checkpoint最小间隔】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        // 同一时间只允许进行一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

        env.setStateBackend(new RocksDBStateBackend("hdfs:///tmp/xiesen"));
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
    }
}
