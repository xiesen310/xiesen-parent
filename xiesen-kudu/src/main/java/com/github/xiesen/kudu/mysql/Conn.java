package com.github.xiesen.kudu.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author 谢森
 * @Description connection
 * @Email xiesen310@163.com
 * @Date 2021/1/4 9:30
 */
public class Conn {
    Connection connection;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://192.168.2.66:3306/test_flink_sql?useUnicode=true" +
                            "&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
                    "root", "a4#tDwX6.laA");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
