package com.github.xiesen.influxdb;

import org.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 谢森
 * @since 2021/7/7
 */
public class InfluxDBTestDelete {
    public static void main(String[] args) {
        String username = "admin";
        String password = "Admin@123";
        String openurl = "http://10.144.98.226:8086";
        String database = "big-screen-prod";
//        String database = "test";
        List<String> allMeasurements = new ArrayList<>();
        List<String> realMeasurements = new ArrayList<>();
        List<String> offLineMeasurements = new ArrayList<>();
        List<String> manMeasurements = new ArrayList<>();

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
                        if (o.toString().startsWith("real_")) {
                            realMeasurements.add(o.toString());
                        } else {
                            offLineMeasurements.add(o.toString());
                        }

                        if (o.toString().startsWith("人员组织")) {
                            manMeasurements.add(o.toString());
                        }
                    }
                }
            }
        }


        /*offLineMeasurements.forEach(m -> {
            StringBuilder builder = new StringBuilder();
            builder.append("drop measurement \"" + m + "\"");
            String sql1 = builder.toString();
            System.out.println(sql1);
            connect.query(builder.toString());
        });*/
//        final int size = offLineMeasurements.size();
//        System.out.println(size);
        System.out.println("人员组织: " + manMeasurements.size());
        manMeasurements.forEach(m -> {
            StringBuilder builder = new StringBuilder();
            builder.append("drop measurement \"" + m + "\"");
            String sql1 = builder.toString();
            System.out.println(sql1);
            connect.query(builder.toString());
        });
        System.out.println("=============");

    }
}
