package com.github.xiesen.oracle;

import java.sql.*;

/**
 * @author xiesen
 * @title: selectPerson
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/4 16:00
 */
public class selectPerson {
    private Connection conn;

    public selectPerson(String dburl, String dbuser, String dbpassword) {
        this.conn = getConnection(dburl, dbuser, dbpassword);
    }

    private Connection getConnection(String dburl, String dbuser, String dbpassword) {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(dburl, dbuser, dbpassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("无效链接!!");
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) throws SQLException {
//        String dburl = "jdbc:oracle:thin:@218.11.11.11:1521:orcl";
        String dburl = "jdbc:oracle:thin:@//192.168.70.21:1521/helowin";
        String dbuser = "xiesen";
        String dbpassword = "xiesen";
        selectPerson s = new selectPerson(dburl, dbuser, dbpassword);
        String sql = "select * from SYS_USER";
        PreparedStatement preparedStatement = s.conn.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
        }

    }
}


