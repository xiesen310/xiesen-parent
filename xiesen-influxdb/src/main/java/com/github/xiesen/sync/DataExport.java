package com.github.xiesen.sync;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataExport {
    private static String username = "admin";
    private static String password = "admin";
    private static String openurl = "http://192.168.90.24:8086";
    private static String database = "datalake";
    private static String outputDir = "/Users/xiesen/Desktop/";

    public static void main(String[] args) throws Exception {
        if (args.length == 5) {
            openurl = args[0];
            username = args[1];
            password = args[2];
            database = args[3];
            outputDir = args[4];
        } else {
            System.out.println("请指定参数,eg(url username password database outputDir): http://192.168.90.24:8086 admin admin datalake /Users/xiesen/Desktop/");
            System.exit(0);
        }

        final InfluxDB influxDB = InfluxDBFactory.connect(openurl, username, password);
        QueryResult measurements = influxDB.query(new Query("show measurements", database));
        Set<String> measurementSet = new HashSet<>();
        final List<QueryResult.Result> results = measurements.getResults();
        results.forEach((k -> {
            final List<QueryResult.Series> series = k.getSeries();
            series.forEach((s) -> {
                final List<List<Object>> values = s.getValues();
                values.forEach(v -> {
                    final String s1 = v.get(0).toString();
                    measurementSet.add(s1);
                });
            });
        }));
        Console.log("计划导出 {} 个指标", measurementSet.size());
        System.out.println("======================");
        measurementSet.forEach(measurement -> {
            List<String> mapList = transformInfluxdbLineProtocol(influxDB, database, measurement);
            if (null != mapList && mapList.size() > 0) {
                String filePath = outputDir + measurement + ".txt";
                writeFile(mapList, filePath);
                Console.log("导出 [{}] 指标成功，数据写入到 {}", measurement, filePath);
            }
        });

        if (influxDB != null) {
            influxDB.close();
        }
        Console.log("指标导出完成.");
    }

    private static List<String> transformInfluxdbLineProtocol(InfluxDB influxDB, String database, String measurement) {
        try {
            QueryResult queryData = influxDB.query(new Query("select * from " + measurement + " limit 10;", database));
            final List<QueryResult.Result> resultData = queryData.getResults();
            List<String> mapList = new ArrayList<>();
            resultData.forEach((k -> {
                final List<QueryResult.Series> series = k.getSeries();
                List<List<Object>> values = series.get(0).getValues();
                List<String> columns = series.get(0).getColumns();
                String name = series.get(0).getName();

                values.forEach(list -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append(name).append(",");
                    String time = "";
                    StringBuilder tagsBuilder = new StringBuilder();
                    StringBuilder fieldsBuilder = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        Object o = list.get(i);
                        if (o instanceof String) {
                            String key = columns.get(i);
                            if ("time".equalsIgnoreCase(key)) {
                                time = o.toString();
                            } else {
                                tagsBuilder.append(key).append("=").append(o.toString().replaceAll(" ", "")).append(",");
                            }
                        } else {
                            fieldsBuilder.append(columns.get(i)).append("=").append(Double.valueOf(o.toString())).append(",");
                        }
                    }
                    String tagStr = tagsBuilder.toString().substring(0, tagsBuilder.toString().length() - 1);
                    String fieldStr = fieldsBuilder.toString().substring(0, fieldsBuilder.toString().length() - 1);
                    long timestamp = System.currentTimeMillis();
                    try {
                        timestamp = DateUtil.parse(time).getTime();
                    } catch (Exception e) {
                        System.out.println("解析时间失败，设置为当前系统时间. time = " + time);
                        e.printStackTrace();
                    }
                    builder.append(tagStr).append(" ").append(fieldStr).append(" ").append(timestamp * 1000000);
                    mapList.add(builder.toString());
                });
            }));
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeFile(List<String> mapList, String filePath) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(FileUtil.file(filePath));
            for (String data : mapList) {
                writer.write(data + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
