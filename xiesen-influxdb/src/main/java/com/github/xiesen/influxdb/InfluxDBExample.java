package com.github.xiesen.influxdb;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxDBExample {
    private static final long KILO = 1024;
    private static final String[] UNITS = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};

    /**
     * 字节大小格式化
     *
     * @param size 字节数
     * @return
     */
    public static String formatBytes(long size) {
        int unitIndex = 0;
        double formattedSize = size;

        while (formattedSize >= KILO && unitIndex < UNITS.length - 1) {
            formattedSize /= KILO;
            unitIndex++;
        }

        return String.format("%.2f %s", formattedSize, UNITS[unitIndex]);
    }

    private final static String databaseName = "datalake";
    private final static String username = "admin";
    private final static String password = "admin";
    private final static String influxDbUrl = "http://192.168.90.24:8086";
    private final static String dataDir = "/data/influxdb/data";

    public static void main(String[] args) {


        InfluxDB influxDB = InfluxDBFactory.connect(influxDbUrl, username, password);
//        queryMetricNum(influxDB, databaseName);
        queryMetricCapacity(influxDbUrl, databaseName);

        System.out.println(formatBytes(1024)); // 输出: 1.00 KB
        System.out.println(formatBytes(2048)); // 输出: 2.00 KB
        System.out.println(formatBytes(1500000)); // 输出: 1.43 MB
        System.out.println(formatBytes(5000000000L)); // 输出: 4.66 GB

        influxDB.close();
    }

    /**
     * 查询指定数据库的容量大小
     *
     * @param influxUrl
     * @param databaseName
     */
    private static void queryMetricCapacity(String influxUrl, String databaseName) {
        databaseName = "xs";
        String url = influxUrl + "/debug/vars";
        HttpResponse execute = new HttpRequest(url).execute();
        Long diskBytesTotal = 0L;
        if (execute.isOk()) {
            String body = execute.body();
            JSONObject jsonObject = JSON.parseObject(body);
            for (String s : jsonObject.keySet()) {
                if (s.startsWith("database:")) {
                    String key = "database:" + databaseName;
                    if (key.equalsIgnoreCase(s)) {
                        JSONObject datasourceJsonObject = jsonObject.getJSONObject(s);
                        String database = datasourceJsonObject.getJSONObject("tags").getString("database");
                        if (databaseName.equalsIgnoreCase(database)) {
                            JSONObject values = datasourceJsonObject.getJSONObject("values");
                            Integer numSeries = values.getInteger("numSeries");
                            Integer numMeasurements = values.getInteger("numMeasurements");
                            Console.log("数据库({})有 {} 个序列,{} 张表", databaseName, numSeries, numMeasurements);
                        }
                    }
                }

                if (s.startsWith("tsm1_filestore:")) {
                    String key = "tsm1_filestore:" + dataDir + "/" + databaseName;
                    if (s.startsWith(key)) {
                        JSONObject fileStoreJsonObject = jsonObject.getJSONObject(s);
                        String path = fileStoreJsonObject.getJSONObject("tags").getString("path");
                        String walPath = fileStoreJsonObject.getJSONObject("tags").getString("walPath");
                        Long diskBytes = fileStoreJsonObject.getJSONObject("values").getLong("diskBytes");
                        diskBytesTotal += diskBytes;
//                        Console.log("path = {}; walPath = {}; diskBytes = {}", path, walPath, diskBytes);
                    }
                }
            }
            System.out.println(jsonObject);
            Console.log("统计数据库({})占用磁盘空间大小为 {} 字节,格式化后大小为 {}", databaseName, diskBytesTotal, formatBytes(diskBytesTotal));
        }

    }

    /**
     * 查询指标数量
     *
     * @param influxDB
     * @param databaseName
     */
    private static void queryMetricNum(InfluxDB influxDB, String databaseName) {
        QueryResult queryResult = influxDB.query(new Query("SHOW MEASUREMENTS", databaseName));
        int measurementCount = queryResult.getResults().get(0).getSeries().get(0).getValues().size();
        System.out.println("指标数量: " + measurementCount);
    }
}
