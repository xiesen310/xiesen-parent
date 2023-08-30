package com.github.xiesen.influxdb;

import org.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 谢森
 * @since 2021/7/7
 */
public class InfluxDBTestQuery {
    /**
     * 计算总计指标 ; 缺少后端费用;
     */
    public static final List<String> allMetric = Arrays.asList("成本_人工成本_人工成本", "成本_前端费用_前端费用",
            "成本_折旧摊销_折旧摊销", "成本_综合费用_综合费用", "成本_综合费用_其他综合费用");

    public static void main(String[] args) {
        String username = "admin";
        String password = "Admin@123";
        String openurl = "http://10.144.98.226:8086";
        String database = "big-screen-prod";
        List<String> allMeasurements = new ArrayList<>();
        List<String> costMeasurements = new ArrayList<>();
        InfluxDBConnect connect = new InfluxDBConnect(username, password, openurl, database, 1, 2);
        connect.connection();
        String sql = "show measurements";
        final QueryResult query = connect.query(sql);
        final List<QueryResult.Result> results = query.getResults();
        for (QueryResult.Result result : results) {
            final List<QueryResult.Series> series = result.getSeries();
            for (QueryResult.Series series1 : series) {
                final List<List<Object>> values = series1.getValues();
                for (List<Object> value : values) {
                    for (Object o : value) {
                        allMeasurements.add(o.toString());
                        if (o.toString().startsWith("成本")) {
                            costMeasurements.add(o.toString());
                        }
                    }
                }
            }
        }


//        allMeasurements.forEach(System.out::println);
        System.out.println("=============");
        costMeasurements.forEach(System.out::println);
        System.out.println("=============");
        for (String s : allMetric) {
            String innerSql = "select * from \"" + s + "\"";
            System.out.println(innerSql);
            final QueryResult query1 = connect.query(innerSql);
            final List<QueryResult.Result> results1 = query1.getResults();
            for (QueryResult.Result result : results1) {
                final List<QueryResult.Series> series = result.getSeries();
                for (QueryResult.Series series1 : series) {
                    final List<List<Object>> values = series1.getValues();
                    for (List<Object> value : values) {
                        System.out.println(value);
                    }
                }
            }
        }
    }
}
