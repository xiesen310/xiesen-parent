package com.github.xiesen.influxdb;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.influxdb.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiesen
 * {
 * "人力编码": "71101908",
 * "姓名": "李世宗",
 * "人效岗位名称": "BD客户代表",
 * "单位": "信息网络部/互联网大客户部",
 * "部门": "互联网BD",
 * "指标名称": "政企全业务新增销售额",
 * "月份": "202307",
 * "指标值": "0",
 * "公司累计排名": "254",
 * "公司分月中位值": "150.00",
 * "区局（/BD）分月中位值": "0.00"
 * }
 */
public class ReadFile2Influx {
    public static final String prefix = "人效分析清单_";
    public static final String username = "admin";
    public static final String password = "Admin@123";
    public static final String openurl = "http://10.144.98.226:8086";
    public static final String database = "big-screen-prod";
//    public static final String database = "test";

    private static final AtomicLong metricSize = new AtomicLong(0L);

    public static void main(String[] args) {
        String filePath = "E:\\tmp\\人效分析7月清单2.json";
        FileReader fileReader = new FileReader(filePath);
        final String s = fileReader.readString();
        final JSONObject jsonObject = JSON.parseObject(s);
        final JSONArray records = jsonObject.getJSONArray("RECORDS");
        BatchPoints batchPoints = BatchPoints.database(database).build();

        for (Object record : records) {
            final JSONObject innerJsonObject = JSON.parseObject(record.toString());
            Map<String, String> tags = new HashMap<>();
            final String 人力编码 = innerJsonObject.getString("人力编码");
            final String 姓名 = innerJsonObject.getString("姓名");
            final String 人效岗位名称 = innerJsonObject.getString("人效岗位名称");
            final String 单位 = innerJsonObject.getString("单位");
            final String 部门 = innerJsonObject.getString("部门");
            final String 指标名称 = innerJsonObject.getString("指标名称");
            final String 月份 = innerJsonObject.getString("月份");
            String 指标值 = innerJsonObject.getString("指标值").replaceAll(",", "");
            Double metricValue = 0.0;
            if (!"".equalsIgnoreCase(指标值)) {
                try {
                    metricValue = Double.valueOf(指标值);
                } catch (Exception e) {
                    Console.error("指标值转换 Double 失败: {}", innerJsonObject.toJSONString());
                    e.printStackTrace();
                }
            }
            String 公司累计排名 = innerJsonObject.getString("公司累计排名").replaceAll(",", "");
            Double rankValue = 0.0;
            if (!"".equalsIgnoreCase(公司累计排名)) {
                try {
                    rankValue = Double.valueOf(公司累计排名);
                } catch (Exception e) {
                    Console.error("公司累计排名转换 Double 失败: {}", innerJsonObject.toJSONString());
                }
            }

            String 公司分月中位值 = innerJsonObject.getString("公司分月中位值").replaceAll(",", "");

            Double companyMedian = 0.0;
            if (!"".equalsIgnoreCase(公司分月中位值)) {
                try {
                    companyMedian = Double.valueOf(公司分月中位值);
                } catch (Exception e) {
                    Console.error("公司分月中位值转换 Double 失败: {}", innerJsonObject.toJSONString());
                }
            }
            String 区局分月中位值 = innerJsonObject.getString("区局（/BD）分月中位值").replaceAll(",", "");

            Double medianValueOfDistrictBranch = 0.0;
            if (!"".equalsIgnoreCase(区局分月中位值)) {
                try {
                    medianValueOfDistrictBranch = Double.valueOf(区局分月中位值);
                } catch (Exception e) {
                    Console.error("区局分月中位值转换 Double 失败: {}", innerJsonObject.toJSONString());
                }
            }
            String unit = 单位.trim();
            if ("政企全业务新增销售额".equalsIgnoreCase(指标名称) && 单位.endsWith("电信局")) {
                unit = 单位.trim().substring(0, 单位.trim().length() - 3);
            }
            String measurement = prefix + 指标名称;
            tags.put("人力编码", 人力编码.trim());
            tags.put("姓名", 姓名.trim());
            tags.put("人效岗位名称", 人效岗位名称.trim());
            tags.put("单位", unit);
            tags.put("部门", 部门.trim());
            tags.put("月份", 月份.trim());
            Map<String, Object> fields = new HashMap<>();
            fields.put("指标值", metricValue);
            fields.put("公司累计排名", rankValue);
            fields.put("公司分月中位值", companyMedian);
            fields.put("区局BD分月中位值", medianValueOfDistrictBranch);
            if (!"".equalsIgnoreCase(月份)) {
                metricSize.getAndIncrement();
                Long time = null;
                try {
                    time = DateUtils.formatTime(月份.trim()).getTime();
                } catch (Exception e) {
                    Console.error(innerJsonObject.toJSONString());
                    e.printStackTrace();
                    throw e;
                }
                Point point = Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS).tag(tags).fields(fields).build();
                batchPoints.point(point);
            } else {
                Console.error("错误数据,月份字段为空. {}", innerJsonObject.toJSONString());
            }


//            if ("".equalsIgnoreCase(指标值)) {
//                Console.log("人力编码 = {} ; 姓名 = {} ; 人效岗位名称 = {} ; 单位 = {} ; 部门 = {} ; 指标名称 = {} ; 月份 = {} ; 指标值 = {} ;",
//                        人力编码, 姓名, 人效岗位名称, 单位, 部门, 指标名称, 月份, 指标值);
//            }

//            Console.log("人力编码 = {} ; 姓名 = {} ; 人效岗位名称 = {} ; 单位 = {} ; 部门 = {} ; 指标名称 = {} ; 月份 = {} ; 指标值 = {} ; 公司累计排名 = {} ; 公司分月中位值 = {} ; 区局分月中位值 = {};",
//                    人力编码, 姓名, 人效岗位名称, 单位, 部门, 指标名称, 月份, 指标值, 公司累计排名, 公司分月中位值, 区局分月中位值);

//            System.out.println(innerJsonObject.toJSONString());
        }

//        System.out.println(fileReader.readString());

        batchWriteData(defaultInfluxDB(), batchPoints);
        Console.log("一共同步了 {} 条数据.", metricSize);
    }


    public static InfluxDB defaultInfluxDB() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        InfluxDB influxDb = InfluxDBFactory.connect(openurl, username, password, okHttpBuilder);
        influxDb.setDatabase(database);
        influxDb.setLogLevel(InfluxDB.LogLevel.BASIC);
        return influxDb;
    }


    /**
     * 批量写入数据到 influxdb
     *
     * @param batchPoints batchPoints
     */
    private static void batchWriteData(InfluxDB influxDB, BatchPoints batchPoints) {

        final boolean flag = influxDB.databaseExists(database);
        if (!flag) {
            Console.log("influxdb 实例中[{}], 数据库[{}]不存在.自动创建数据库...", openurl, database);
            influxDB.createDatabase(database);
        }
        if (batchPoints.getPoints().size() > 0) {
            influxDB.write(batchPoints);
        }
    }
}
