package com.github.xiesen.influxdb;

import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @since 2021/7/7
 */
public class InfluxDBTestQuery1 {
    public static void main(String[] args) {
        String username = "admin";
        String password = "admin";
        String openurl = "http://192.168.90.24:8086";
        String database = "datalake";
        // 连接到 InfluxDB
        InfluxDB influxDB = InfluxDBFactory.connect(openurl, username, password);

        String sql = "SELECT * FROM cpu limit 10";
        sql += " " + "PRECISION ms";
        // 执行查询
        Query query = new Query(sql, database);
        QueryResult queryResult = influxDB.query(query);
        System.out.println(queryResult);
    }
}
