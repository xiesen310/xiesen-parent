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
public class StreamSQLIdentifier {
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

        Table tableA = tEnv.fromDataStream(orderA, $("user"), $("product"), $("amount"));

        // register the view named 'exampleView' in the catalog named 'custom_catalog'
        // in the database named 'custom_database'
        tEnv.createTemporaryView("exampleView", tableA);

        // register the view named 'exampleView' in the catalog named 'custom_catalog'
        // in the database named 'other_database'
        tEnv.createTemporaryView("other_database.exampleView", tableA);

        // register the view named 'example.View' in the catalog named 'custom_catalog'
        // in the database named 'custom_database'
//        tEnv.createTemporaryView("example.View", tableA);

        // register the view named 'exampleView' in the catalog named 'other_catalog'
        // in the database named 'other_database'
//        tEnv.createTemporaryView("other_catalog.other_database.exampleView", tableA);

        for (String catalog : tEnv.listCatalogs()) {
            System.out.println("catalogName = " + catalog);
        }

        for (String database : tEnv.listDatabases()) {
            System.out.println("databaseName = " + database);
        }

        for (String table : tEnv.listTables()) {
            System.out.println("tableName = " + table);
        }


    }
}
