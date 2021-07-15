package com.github.xiesen.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @since 2021/7/7
 */
public class InfluxDBConnect {
    private String username;
    private String password;
    private String url;
    private String database;
    private int retentionDay;
    private int replicationCount;

    private InfluxDB influxDB;


    /**
     * @param username         用户名
     * @param password         密码
     * @param url              url
     * @param database         数据库名称
     * @param retentionDay     数据保存时限
     * @param replicationCount 副本数量
     */
    public InfluxDBConnect(String username, String password, String url, String database, int retentionDay,
                           int replicationCount) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.database = database;
        this.retentionDay = retentionDay;
        this.replicationCount = replicationCount;
    }

    /**
     * 连接时序数据库；获得InfluxDB
     **/
    void connection() {
        if (influxDB == null) {
            influxDB = InfluxDBFactory.connect(url, username, password);
        }
    }

    /**
     * 设置数据保存策略
     * defalut 策略名 /database 数据库名/ 30d 数据保存时限30天/ 1  副本个数为1/ 结尾DEFAULT 表示 设为默认的策略
     */
    void createRetentionPolicy() {
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                "default", database, retentionDay + "d", replicationCount);
        this.query(command);
    }

    /**
     * 查询
     *
     * @param command 查询语句
     * @return 查询结果
     */
    QueryResult query(String command) {
        return influxDB.query(new Query(command, database));
    }

    /**
     * 插入
     */
    public void insert(InfluxDbRow influxDbRow) {
        if (influxDbRow == null) {
            return;
        }
        Point.Builder builder = Point.measurement(influxDbRow.getMeasurement());
        builder.tag(influxDbRow.getTags());
        builder.fields(influxDbRow.getFields());
        if (influxDbRow.getTimeSecond() != null) {
            builder.time(influxDbRow.getTimeSecond(), TimeUnit.SECONDS);
        }
        Instant timestamp = Instant.now();

        // builder.time(timestamp.toEpochMilli(), TimeUnit.MILLISECONDS);
        builder.time(1626231525798L, TimeUnit.MILLISECONDS);
        influxDB.write(database, "default", builder.build());
    }

    public static Long getmicTime() {
        Long cutime = System.currentTimeMillis() * 1000; // 微秒
        Long nanoTime = System.nanoTime(); // 纳秒
        return cutime + (nanoTime - nanoTime / 1000000 * 1000000) / 1000;
    }

    /**
     * 删除
     *
     * @param command 删除语句
     * @return 返回错误信息
     */
    public String deleteMeasurementData(String command) {
        QueryResult result = influxDB.query(new Query(command, database));
        return result.getError();
    }

    /**
     * 创建数据库
     *
     * @param dbName 库名称
     */
    public void createDB(String dbName) {
        this.query("create database " + dbName);
    }

    /**
     * 删除数据库
     *
     * @param dbName
     */
    public void deleteDB(String dbName) {
        this.query("drop database " + dbName);
    }

    public void close() {
        this.influxDB.close();
    }

    /**
     * 指导导入
     *
     * @param influxDbRows 行记录
     */
    public void batchPointsImport(List<InfluxDbRow> influxDbRows) {
        if (influxDbRows == null || influxDbRows.size() == 0) {
            return;
        }
        BatchPoints batchPoints = BatchPoints.database(this.database).retentionPolicy("default").build();
        for (InfluxDbRow influxDbRow : influxDbRows) {
            if (influxDbRow.getTags().size() + influxDbRow.getFields().size() == 0) continue;
            Point.Builder builder = Point.measurement(influxDbRow.getMeasurement());
            builder.tag(influxDbRow.getTags());
            builder.fields(influxDbRow.getFields());
            if (influxDbRow.getTimeSecond() != null) {
                builder.time(influxDbRow.getTimeSecond(), TimeUnit.SECONDS);
            } else {
                builder.time(System.currentTimeMillis() / 1000, TimeUnit.SECONDS);
            }
            batchPoints.point(builder.build());
        }
        influxDB.write(batchPoints);
    }
}
