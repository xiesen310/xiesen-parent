package com.github.xiesen.basics;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Arrays;
import java.util.Objects;

import static org.apache.flink.table.api.Expressions.$;

/**
 * 扩展表标识符
 *
 * @author 谢森
 * @since 2021/3/4
 */
public class StreamSQLSearch {
    public static void main(String[] args) throws Exception {
        final ParameterTool params = ParameterTool.fromArgs(args);
        String planner = params.has("planner") ? params.get("planner") : "blink";

        // 设置执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        StreamTableEnvironment tEnv;
        if (Objects.equals(planner, "blink")) {
            EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().useBlinkPlanner()
                    .build();
            tEnv = StreamTableEnvironment.create(env, settings);
        } else if (Objects.equals(planner, "flink")) {
            EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().useOldPlanner()
                    .build();
            tEnv = StreamTableEnvironment.create(env, settings);
        } else {
            System.err.println("The planner is incorrect. Please run 'StreamSQLExample --planner <planner>', " +
                    "where planner (it is either flink or blink, and the default is blink) indicates whether the " +
                    "example uses flink planner or blink planner.");
            return;
        }
        DataStream<Order> orderA = env.fromCollection(Arrays.asList(
                new Order(1L, "beer", 3),
                new Order(1L, "diaper", 4),
                new Order(3L, "rubber", 2)));

        Table orders = tEnv.fromDataStream(orderA, $("user"), $("product"), $("amount"));

        orders.printSchema();
        Table revenue = orders.filter($("product").isNotEqual("beer"))
                .select($("user"), $("product"), $("amount"));

        tEnv.toAppendStream(revenue, Order.class).print();
        env.execute();
    }
}
