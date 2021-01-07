package com.github.xiesen.hbase.utils;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * @author 谢森
 * @Description Mysql 操作工具类
 * @Email xiesen310@163.com
 * @Date 2021/1/7 9:39
 */
public class JdbcUtils {
    private static Properties prop = new Properties();
    private static DruidDataSource dataSource = new DruidDataSource();

    static {
        try {
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            /**
             * 加载配置
             */
            dataSource.configFromPropety(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    private static JdbcUtils instance = null;

    public static JdbcUtils getInstance() {

        if (instance == null) {
            synchronized (JdbcUtils.class) {
                if (instance == null) {
                    instance = new JdbcUtils();
                }
            }
        }

        return instance;
    }

    public void executeQuery(String sql, QueryCallBack callBack) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            callBack.process(rs);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            release(conn, stmt, rs);
        }
    }

    public int executeUpdate(String sql, Object[] params) {
        int arl = 0;
        Connection conn = null;
        PreparedStatement pstat = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            pstat = conn.prepareStatement(sql);

            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    pstat.setObject(i + 1, params[i]);
                }
            }

            arl = pstat.executeUpdate();

            conn.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            release(conn, pstat);
        }

        return arl;
    }

    public int executeUpdate(String sql) {
        int arl = 0;
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            stmt = conn.createStatement();
            arl = stmt.executeUpdate(sql);

            conn.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            release(conn, stmt);
        }
        return arl;
    }

    public void executeQuery(String sql, Object[] params, QueryCallBack callBack) {
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {

            conn = dataSource.getConnection();
            pstat = conn.prepareStatement(sql);
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    pstat.setObject(i + 1, params[i]);
                }
            }
            rs = pstat.executeQuery();
            callBack.process(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            release(conn, pstat, rs);
        }
    }

    public int[] executeBatch(String sql, List<Object[]> paramsList) {
        int[] arls = null;
        Connection conn = null;
        PreparedStatement pstat = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            pstat = conn.prepareStatement(sql);

            if (paramsList != null && paramsList.size() > 0) {
                for (Object[] params : paramsList) {
                    for (int i = 0; i < params.length; i++) {
                        pstat.setObject(i + 1, params[i]);
                    }
                    pstat.addBatch();
                }
            }

            arls = pstat.executeBatch();
            conn.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            release(conn, pstat);
        }

        return arls;
    }

    public static interface QueryCallBack {
        // 处理查询结果
        void process(ResultSet rs) throws Exception;
    }

    // 释放资源
    public static void release(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }

        release(conn, stmt);
    }

    /**
     * 释放资源
     */
    public static void release(Connection conn, Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stmt = null;
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }
}
