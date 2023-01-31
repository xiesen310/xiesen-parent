package com.github.xiesen.redis.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import com.github.xiesen.redis.mysql.BaseDao;
import com.github.xiesen.redis.pojo.MetricModel;
import com.github.xiesen.redis.pojo.MetricSet;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xiesen
 */
public class MysqlHelper {
    public static final int batchSize = 20000;

    /**
     * 将模型数据写入到 mysql*
     *
     * @param data {@link Map<String, MetricModel>}
     */
    public static void writerMetricModel(Map<String, MetricModel> data) {
        long start = System.currentTimeMillis();
        Connection conn = BaseDao.getConn();
        String sql = "INSERT INTO `metric_model` (`id`, `instance_id`, `name`, `classification`, `assets_classification`, `source`, `statistical_period`, `status`, `producer`, `owner`, `maintainer`, `maintenance_time`, `comment`, `appsystem`, `appprogramname`, `cluster_name`, `hostname`, `ip`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES (NULL, ? , ?, ?, '1', '一体化平台', '10s', '1', 'admin', 'admin', NULL, NULL, ?, ?, ?, ?, ?, ?, 'admin', ? , 'admin', ?)";
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            if (data.size() <= batchSize) {
                modelProcess(data, ps);
                ps.executeBatch();
                conn.commit();
            }
            
            if (data.size() > batchSize) {
                int i = 0;
                for (Map.Entry<String, MetricModel> metricModelEntry : data.entrySet()) {
                    final String instanceId = metricModelEntry.getKey();
                    final MetricModel metricModel = metricModelEntry.getValue();
                    final String metricSetName = metricModel.getMetricSetName();
                    final Map<String, String> dimensions = metricModel.getDimensions();

                    String appSystem = MapUtil.getStr(dimensions, "appsystem");
                    String appProgramName = MapUtil.getStr(dimensions, "appprogramname");
                    String clustername = MapUtil.getStr(dimensions, "clustername");
                    String hostname = MapUtil.getStr(dimensions, "hostname");
                    String ip = MapUtil.getStr(dimensions, "ip");

                    ps.setString(1, instanceId);
                    ps.setString(2, metricSetName + "_" + ip);
                    ps.setString(3, metricSetName);
                    ps.setString(4, metricSetName);
                    ps.setString(5, appSystem);
                    ps.setString(6, appProgramName);
                    ps.setString(7, clustername);
                    ps.setString(8, hostname);
                    ps.setString(9, ip);
                    final DateTime now = DateTime.now();
                    ps.setString(10, now.toString());
                    ps.setString(11, now.toString());
                    ps.addBatch();
                    i++;
                    if (i != 0 && (i + 1) % batchSize == 0) {
                        ps.executeBatch();
                        conn.commit();
                        ps.clearBatch();
                    }
                }
                ps.executeBatch();
                conn.commit();
            }

        } catch (SQLException e) {
            if (1062 == e.getErrorCode()) {
//                System.out.println(e.getErrorCode() + " " + e.getMessage() + " ");
            } else {
                e.printStackTrace();
            }
        } finally {
            BaseDao.closeAll(conn, ps);
        }
        long end = System.currentTimeMillis();
//        System.out.println("所用时长:" + (end - start) + "毫秒");
    }

    private static void modelProcess(Map<String, MetricModel> data, PreparedStatement ps) throws SQLException {
        for (Map.Entry<String, MetricModel> metricModelEntry : data.entrySet()) {
            final String instanceId = metricModelEntry.getKey();
            final MetricModel metricModel = metricModelEntry.getValue();
            final String metricSetName = metricModel.getMetricSetName();
            final Map<String, String> dimensions = metricModel.getDimensions();

            String appSystem = MapUtil.getStr(dimensions, "appsystem");
            String appProgramName = MapUtil.getStr(dimensions, "appprogramname");
            String clustername = MapUtil.getStr(dimensions, "clustername");
            String hostname = MapUtil.getStr(dimensions, "hostname");
            String ip = MapUtil.getStr(dimensions, "ip");

            ps.setString(1, instanceId);
            ps.setString(2, metricSetName + "_" + ip);
            ps.setString(3, metricSetName);
            ps.setString(4, metricSetName);
            ps.setString(5, appSystem);
            ps.setString(6, appProgramName);
            ps.setString(7, clustername);
            ps.setString(8, hostname);
            ps.setString(9, ip);
            final DateTime now = DateTime.now();
            ps.setString(10, now.toString());
            ps.setString(11, now.toString());
            ps.addBatch();
        }
    }


    private static boolean existMetricSetName(String metricSetName) {
        Connection conn = BaseDao.getConn();
        String sql = "select count(1) from data_set where name = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, metricSetName);
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final long num = rs.getLong(1);
                if (num > 0) {
                    return true;
                }
            }
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(conn, ps);
        }
        return false;
    }

    private static boolean existMetricModelId(String metricModelId) {
        Connection conn = BaseDao.getConn();
        String sql = "select count(1) from metric_model where instance_id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, metricModelId);
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final long num = rs.getLong(1);
                if (num > 0) {
                    return true;
                }
            }
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(conn, ps);
        }
        return false;
    }


    private static void truncateTables(Set<String> tables) {
        for (String table : tables) {
            Connection conn = BaseDao.getConn();
            String sql = "TRUNCATE TABLE " + table;
            PreparedStatement ps = null;
            try {
                ps = conn.prepareStatement(sql);
                ps.execute();
            } catch (Exception e) {
                System.out.println("清空表 [{" + table + "}] 失败");
                e.printStackTrace();
            } finally {
                BaseDao.closeAll(conn, ps);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static void testTruncateTables() {
        Set<String> tables = new HashSet<>();
        tables.add("data_set");
        tables.add("data_set_detail");
        tables.add("metric_model");
        truncateTables(tables);
    }

    /**
     * 自动创建指标集*
     *
     * @param data {@link Map<String, MetricSet>}
     */
    public static void writerDataSet(Map<String, MetricSet> data) {
        Connection conn = BaseDao.getConn();
        int id = 0;
        String sql = "INSERT INTO `data_set` (`id`, `type`, `name`, `display_name`, `status`, `comment`, `create_user`, `create_time`,`update_user`, `update_time`) VALUES (NULL, '1', ? , ? , '1', ? , 'auto', ?, 'auto', ?)";

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (Map.Entry<String, MetricSet> metricModelEntry : data.entrySet()) {
                final String metricSetName = metricModelEntry.getKey();
                if (!existMetricSetName(metricSetName)) {
                    final MetricSet metricSet = metricModelEntry.getValue();
                    ps.setString(1, metricSetName);
                    ps.setString(2, metricSetName);
                    ps.setString(3, metricSetName);
                    final DateTime now = DateTime.now();
                    ps.setString(4, now.toString());
                    ps.setString(5, now.toString());

                    ps.executeUpdate();
                    final ResultSet resultSet = ps.getGeneratedKeys();
                    while (resultSet.next()) {
                        id = resultSet.getInt(1);
                    }
                    writerMetricDetail(id, metricSet);
                } else {
                    System.out.println(metricSetName + " 指标集已存在");
                }
            }
            int[] ints = ps.executeBatch();
            if (ints.length > 0) {
//                System.out.println("已成功添加 " + ints.length + " 条数据！！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(conn, ps);
        }
    }

    /**
     * 写入指标集详情数据*
     *
     * @param dataSetId 指标集id
     * @param metricSet 指标集
     */
    private static void writerMetricDetail(int dataSetId, MetricSet metricSet) {
        long start = System.currentTimeMillis();
        Connection conn = BaseDao.getConn();
        String sql = "INSERT INTO `data_set_detail` (`id`, `data_set_id`, `name`, `display_name`, `comment`, `column_type`, `data_type`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES (NULL, ?, ?, ?, ?, ?, '', 'admin', ?, 'admin', ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            final Map<String, String> dimensions = metricSet.getDimensions();
            final Map<String, String> metrics = metricSet.getMetrics();
            for (String key : dimensions.keySet()) {
                ps.setInt(1, dataSetId);
                ps.setString(2, key);
                ps.setString(3, key);
                ps.setString(4, key);
                ps.setString(5, "0");
                final DateTime now = DateTime.now();
                ps.setString(6, now.toString());
                ps.setString(7, now.toString());
                ps.addBatch();
            }
            for (String key : metrics.keySet()) {
                ps.setInt(1, dataSetId);
                ps.setString(2, key);
                ps.setString(3, key);
                ps.setString(4, key);
                ps.setString(5, "1");
                final DateTime now = DateTime.now();
                ps.setString(6, now.toString());
                ps.setString(7, now.toString());
                ps.addBatch();
            }

            int[] ints = ps.executeBatch();
            if (ints.length > 0) {
//                System.out.println("已成功添加 " + ints.length + " 条数据！！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeAll(conn, ps);
        }
        long end = System.currentTimeMillis();
//        System.out.println("所用时长:" + (end - start) + "毫秒");
    }


    public static void main(String[] args) {
//        final boolean flag = existMetricSetName("xiesen_metric671");
//        System.out.println(flag);
        testTruncateTables();
    }
}
