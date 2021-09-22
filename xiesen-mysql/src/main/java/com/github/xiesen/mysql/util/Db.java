package com.github.xiesen.mysql.util;

import com.github.xiesen.mysql.excel.BizData;
import com.github.xiesen.mysql.excel.BizDataService;
import com.github.xiesen.mysql.excel.Cmdb;
import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class Db {
    private final static String url = "jdbc:mysql://192.168.1.222:3306/xiesen?useSSL=false&characterEncoding=utf8";
    private final static String name = "root";
    private final static String pwd = "Mysql@123";
    private static Connection conn;
    private static PreparedStatement ps;

    /**
     * 获取链接信息
     *
     * @return {@link java.sql.Connection}
     * @throws Exception
     */
    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection(url, name, pwd);
        return conn;
    }

    public void psBatchBizData() throws Exception {
        try {
            // 获取数据库连接
            conn = getConnection();

            List<BizData> bizByExcel =
                    BizDataService.getBizByExcel("D:\\tmp\\excel\\topic.xls");
            for (BizData bizData : bizByExcel) {
                String sql = "insert into biz_data (hostname, logTypeName, appProgramName,appSystem,topic) values (?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, bizData.getHostname());
                ps.setString(2, bizData.getLogTypeName());
                ps.setString(3, bizData.getAppprogramname());
                ps.setString(4, bizData.getAppsystem());
                ps.setString(5, bizData.getTopic());
                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    public void psBatchCmdb() throws Exception {
        try {
            // 获取数据库连接
            conn = getConnection();

            List<Cmdb> cmdbByExcel = BizDataService.getCmdbByExcel("D:\\tmp\\excel\\cmdb.xls");
            for (Cmdb cmdb : cmdbByExcel) {
                String sql = "insert into cmdb (bkHostInnerIp, osName, bkBizName,bkHostName,bkOsType) values (?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, cmdb.getBkHostInnerIp());
                ps.setString(2, cmdb.getOsName());
                ps.setString(3, cmdb.getBkBizName());
                ps.setString(4, cmdb.getBkHostName());
                ps.setString(5, cmdb.getBkOsType());
                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
