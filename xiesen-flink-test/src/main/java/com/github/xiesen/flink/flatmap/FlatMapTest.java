package com.github.xiesen.flink.flatmap;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/3/4
 */
public class FlatMapTest {
    public static void main(String[] args) throws Exception {
        // Checking input parameters
        final ParameterTool params = ParameterTool.fromArgs(args);

        // set up the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);
        DataStream<String> text;
        text = env.fromElements("hadoop", "spark");

        DataStream<ZorkDataMap> map = text.map(new MapFunction<String, ZorkDataMap>() {
            @Override
            public ZorkDataMap map(String value) throws Exception {
                ZorkDataMap zorkDataMap = new ZorkDataMap();
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("msg", value);
                zorkDataMap.setMap(hashMap);
                return zorkDataMap;
            }
        });
        map.flatMap(new FlatMapFunction<ZorkDataMap, ZorkDataMap>() {
            @Override
            public void flatMap(ZorkDataMap value, Collector<ZorkDataMap> out) throws Exception {

                out.collect(null);
            }
        }).addSink(new SinkFunction<ZorkDataMap>() {
            @Override
            public void invoke(ZorkDataMap value, Context context) throws Exception {
                System.out.println(value);
            }
        });

        // execute program
        env.execute("Streaming WordCount");
    }
}
