package com.github.xiesen.trino;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

public class TrinoJdbcExample {
    public static void main(String[] args) throws Exception {
        Class.forName("io.trino.jdbc.TrinoDriver");
        TimeZone.setDefault(TimeZone.getTimeZone("+08:00"));
        String url = "jdbc:trino://192.168.70.192:8881";
        Properties properties = new Properties();
        properties.setProperty("user", "trino");
        properties.setProperty("password", "");
//        properties.setProperty("SSL", "true");
        Connection connection = DriverManager.getConnection(url, properties);
        Statement statement = connection.createStatement();
//        String sql = "select * from myclickhouse.yisa_oe.student as a left join hive.default.student2 as b on a.id=b.id";
//        String sql = "SHOW CATALOGS";
        String sql = "select name,task_comment from mysql.smartdata.bd_stream_task";
        ResultSet rs = statement.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i < columnCount + 1; i++) {
                row.add(rs.getObject(i));
            }
            System.out.println(row);

           /* System.out.println(rs.getString(1) + " " + rs.getString(2) + " ||| " +
                    rs.getString(3) + " " + rs.getString(4));*/
        }
    }
}
