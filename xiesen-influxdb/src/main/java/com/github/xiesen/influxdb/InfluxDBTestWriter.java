package com.github.xiesen.influxdb;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @since 2021/7/7
 */
public class InfluxDBTestWriter {
    /**
     * 操作系统日志类型
     * linux_os_log
     * windows_winlog
     */
    public static final List<String> operationSystemLogTypeNames = Arrays.asList("linux_os_log", "windows_winlog");

    /**
     * 中间件日志类型
     * im_mid_ths,im_mid_redis,im_mid_kafka,im_mid_zk,im_mid_tomcat,im_mid_tongweb,im_mid_nginx,im_mid_tongrds
     */
    public static final List<String> middlewareLogTypeNames = Arrays.asList("im_mid_ths", "im_mid_redis", "im_mid_kafka", "im_mid_zk", "im_mid_tomcat", "im_mid_tongweb", "im_mid_nginx", "im_mid_tongrds");

    /**
     * 数据库日志类型
     * databaseproduct_log
     */
    public static final List<String> databaseLogTypeNames = Arrays.asList("databaseproduct_log");
    public static final List<String> logSourceList = Arrays.asList("/var/log/message", "/var/log/syslog", "/var/log/secure", "/var/log/auth.log", "/var/log/maillog", "/var/log/cron", "/var/log/messages", "/var/log/httpd/access_log", "/var/log/httpd/error_log", "/var/log/http");


    /**
     * 其他的都是业务日志
     */
    public static final List<String> otherLogTypeNames = Arrays.asList("other_mid_ths", "other_mid_redis", "other_mid_kafka", "other_mid_zk", "other_mid_tomcat", "other_mid_tongweb", "other_mid_nginx", "other_mid_tongrds");

    public static final Map<String, List<String>> appSystemMap = new HashMap<>();
    public static final int maxIpNum = 25;

    /**
     * 添加 cmdb 中不存在的 ip
     */
    private static void addIps(List<String> ipList, String ipPrefix) {
        for (int i = 2; i < maxIpNum; i++) {
            ipList.add(ipPrefix + i);
        }
    }

    static {
        // 海通证券
        List<String> homCmdbIps = Arrays.asList("19.21.23.225", "19.21.23.152", "19.21.23.236");
        List<String> homIps = new ArrayList<>();
        homIps.addAll(homCmdbIps);
        addIps(homIps, "19.21.22.");
        appSystemMap.put("hom", homIps);

        // poc测试
        List<String> poctestCmdbIps = Arrays.asList("192.168.70.97");
        List<String> poctestIps = new ArrayList<>();
        poctestIps.addAll(poctestCmdbIps);
        addIps(poctestIps, "192.168.80.");
        appSystemMap.put("poctest", poctestIps);

        // 数据中心
        List<String> JFGLCmdbIps = Arrays.asList("10.180.40.248", "10.180.40.250", "10.180.40.251");
        List<String> JFGLIps = new ArrayList<>();
        JFGLIps.addAll(JFGLCmdbIps);
        addIps(JFGLIps, "10.180.50.");
        appSystemMap.put("JFGL", JFGLIps);

        // 业务网kafka消息传输公共服务平台
        List<String> kafkaCmdbIps = Arrays.asList("10.112.21.126", "10.112.21.28", "10.112.21.42", "10.112.21.25", "10.112.21.57", "10.112.21.243", "10.112.21.127", "10.112.21.206", "10.112.21.101", "10.112.21.197");
        List<String> kafkaIps = new ArrayList<>();
        kafkaIps.addAll(kafkaCmdbIps);
        addIps(kafkaIps, "10.112.31.");
        appSystemMap.put("kafka", kafkaIps);

        // 公共组件业务
        List<String> commonCmdbIps = Arrays.asList("19.21.23.177", "19.21.23.7", "19.21.23.209", "19.21.23.116");
        List<String> commonIps = new ArrayList<>();
        commonIps.addAll(commonCmdbIps);
        addIps(commonIps, "10.112.33.");
        appSystemMap.put("common", commonIps);
    }


    public static void main(String[] args) {
        String username = "admin";
        String password = "admin";
        String openurl = "http://192.168.70.97:8086";
        String database = "xs";

        List<String> dateRangeList = dateRangeFrom(LocalDate.of(2024, 8, 14),LocalDate.now());
        dateRangeList.forEach(date -> {
            Console.log("开始写入 {} 时间数据...", date);
            InfluxDB influxDB = InfluxDBFactory.connect(openurl, username, password);
            try {
                writerMetric(influxDB, database, buildOperationSystemPoints(date));
                writerMetric(influxDB, database, buildMiddlewarePoints(date));
                writerMetric(influxDB, database, buildDatabasePoints(date));
                writerMetric(influxDB, database, buildOtherPoints(date));
            } catch (Exception e) {
                Console.error("写入指标数据失败.");
                e.printStackTrace();
            } finally {
                influxDB.close();
                Console.log("写入完成 {} 时间数据...", date);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Sleep was interrupted.");
                }
            }

        });
    }


    /**
     * 生成时间范围
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return List<String>
     * @author 谢森
     * </String>
     */
    public static List<String> dateRangeFrom(LocalDate startDate, LocalDate endDate) {
        List<String> dateRangeList = new ArrayList<>();
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        if (daysBetween < 0) {
            System.out.println("起始日期不能晚于当前日期.");
            return dateRangeList;
        }

        for (long i = 0; i <= daysBetween; i++) {
            LocalDate date = startDate.plusDays(i);
            String yyyyMMdd = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            dateRangeList.add(yyyyMMdd);
        }
        return dateRangeList;
    }

    /**
     * 写入指标
     *
     * @param influxDB influx connect
     * @param database database name
     * @param points   points
     */
    private static void writerMetric(InfluxDB influxDB, String database, List<Point> points) {
        if (points == null || points.size() == 0) {
            return;
        }
        BatchPoints batchPoints = BatchPoints.database(database).consistency(InfluxDB.ConsistencyLevel.ALL).build();
        points.forEach(point -> {
            batchPoints.point(point);
        });
        influxDB.write(batchPoints);
    }


    /**
     * 构建操作系统日志
     *
     * @param datetime eg: 20240816
     * @return List<Point>
     */
    private static List<Point> buildOperationSystemPoints(String datetime) {
        return buildBasePoints(operationSystemLogTypeNames, datetime);
    }

    /**
     * 构建中间件日志
     *
     * @param datetime
     * @return List<Point>
     */
    private static List<Point> buildMiddlewarePoints(String datetime) {
        return buildBasePoints(middlewareLogTypeNames, datetime);
    }

    /**
     * 构建数据库日志
     *
     * @param datetime
     * @return
     */
    private static List<Point> buildDatabasePoints(String datetime) {
        return buildBasePoints(databaseLogTypeNames, datetime);
    }

    /**
     * 构建其他类型的日志点
     * <p>
     * 本方法通过调用构建基础日志点的方法，来根据指定的时间生成其他类型的日志点
     *
     * @param datetime 时间字符串，用于生成日志点的时间依据
     * @return 返回一个包含其他类型日志点的列表
     */
    private static List<Point> buildOtherPoints(String datetime) {
        return buildBasePoints(otherLogTypeNames, datetime);
    }

    /**
     * 构建基础 point
     *
     * @param logTypeNames
     * @param datetime
     * @return
     */
    private static List<Point> buildBasePoints(List<String> logTypeNames, String datetime) {
        List<Point> points = new ArrayList<>();
        if (logTypeNames == null) {
            return points;
        }
        if (appSystemMap.size() > 0) {
            appSystemMap.forEach((appSystem, ips) -> {
                logTypeNames.forEach(logTypeName -> {
                    ips.forEach(ip -> {
                        logSourceList.forEach(logSource -> {
                            Point point = buildPoint(appSystem, logTypeName, ip, logSource, datetime);
                            points.add(point);
                        });
                    });
                });
            });
        }
        return points;
    }


    /**
     * 构建 point
     *
     * @param appSystem   系统
     * @param logTypeName 日志集名称
     * @param ip          ip 地址
     * @param logSource   日志路径
     * @param datetime    日期 eg: 20240816
     * @return
     */
    private static Point buildPoint(String appSystem, String logTypeName, String ip, String logSource, String datetime) {
        if (datetime == null) {
            datetime = DateUtil.format(new Date(), "yyyyMMdd");
        }
        DateTime yyyyMMdd = DateUtil.parse(datetime);
        long currentMs = yyyyMMdd.toTimestamp().getTime();

        Point point = Point.measurement("log_source_count").tag("appsystem", appSystem).tag("data_source", "hdfs").tag("datetime", datetime).tag("ip", ip).tag("source", logSource).tag("log_type_name", logTypeName).field("count", Double.valueOf(RandomUtil.randomInt(100, 100000)))
                // 固定的值
                .time(currentMs, TimeUnit.MILLISECONDS).build();
        return point;
    }
}
