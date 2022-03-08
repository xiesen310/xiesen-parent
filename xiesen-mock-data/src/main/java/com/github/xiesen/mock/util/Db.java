package com.github.xiesen.mock.util;


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

    public void psBatchBizData(String name) throws Exception {
        try {
            // 获取数据库连接
            conn = getConnection();
            String sql = "insert into metric_test (name) values (?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.execute();
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
