package com.github.xiesen.phoenix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestPhoenix {
    private static final String JDBC_DRIVER_CLASS = "org.apache.phoenix.jdbc.PhoenixDriver";
    // jdbc:phoenix:192.168.1.19:2181:/hbase中的/hbase为HBase注册到zooKeeper的根目录, 如使用HBase自带的zooKeeper,默认为"hbase"
    private static final String JDBC_URL = "jdbc:phoenix:cdh-3,cdh-4,cdh-5:2181:/hbase";

    public static void main(String[] args) throws SQLException {
        String sql = "select * from hbase_test";
        Connection conn = getConn();
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.execute();
        closeConn(conn);
    }


    public static Connection getConn() {
        try {
            // 注册Driver
            Class.forName(JDBC_DRIVER_CLASS);
            // 返回Connection对象
            return DriverManager.getConnection(JDBC_URL);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.printf("获得连接失败!");
            return null;
        }
    }

    public static void closeConn(Connection conn) {
        try {
            if (!conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.printf("关闭连接失败!");
        }
    }
}
