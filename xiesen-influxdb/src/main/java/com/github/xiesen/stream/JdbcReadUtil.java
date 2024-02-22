package com.github.xiesen.stream;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcReadUtil {
    public static final String MYSQL_URL = "jdbc:mysql://192.168.70.64:3306/smartdata_20231213";
    public static final String MYSQL_USERNAME = "root";
    public static final String MYSQL_PASSWORD = "zorkdata.8888";

    public static void main(String[] args) {
        List<String> list = searchStreamConf();
        if (list.size() > 0) {
            for (String conf : list) {
//                System.out.println(conf);
            }
        }
    }

    public static List<String> searchStreamConf() {
        List<String> result = new ArrayList<>();
        // JDBC连接对象
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 连接到MySQL数据库
            conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);

            // 创建Statement对象
            stmt = conn.createStatement();

            // 执行查询语句
            String query = "select * from bd_stream_task where id not in(198, 251, 258, 267, 299, 300, 308, 360, 383, 413, 437, 453, 487, 521,  525, 528, 529, 535, 620, 710, 746, 747,787, 826, 836);";
            rs = stmt.executeQuery(query);

            // 遍历结果集并输出数据
            while (rs.next()) {
                // 获取每一行数据的具体字段值
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String execParam = rs.getString("exec_param");
                // 可根据实际情况获取更多字段值
                result.add(execParam);

//                Boolean check = StreamConfigExample.check(execParam);
//                if (!check) {
//                    Console.log("ID: {}, Name: {}", id, name);
//                }
                // 输出数据
//                Console.log("ID: {}, Name: {}, execParam: {}", id, name, execParam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭ResultSet、Statement和Connection对象
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
