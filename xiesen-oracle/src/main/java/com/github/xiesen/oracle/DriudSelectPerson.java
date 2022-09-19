package com.github.xiesen.oracle;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author xiesen
 * @title: selectPerson
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/4 16:00
 */
public class DriudSelectPerson {
    private Connection conn;
    private DataSource dataSource;

    public DriudSelectPerson(String dburl, String dbuser, String dbpassword) throws Exception {
        this.conn = getConnection(dburl, dbuser, dbpassword);
//        getDataSource();
    }

    public DriudSelectPerson() throws Exception {
        getDataSource();
    }

    private void getDataSource() throws Exception {
        Map map = new HashMap();
        //设置URL
        map.put(DruidDataSourceFactory.PROP_URL, "jdbc:oracle:thin:@//192.168.70.21:1521/helowin");
        //设置驱动Driver
        map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, "oracle.jdbc.driver.OracleDriver");
        //设置用户名
        map.put(DruidDataSourceFactory.PROP_USERNAME, "xiesen");
        //设置密码
        map.put(DruidDataSourceFactory.PROP_PASSWORD, "xiesen");
        dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(map);
        this.conn = dataSource.getConnection();
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

    public static void main(String[] args) throws Exception {
//        String dburl = "jdbc:oracle:thin:@218.11.11.11:1521:orcl";
        String dburl = "jdbc:oracle:thin:@//192.168.70.21:1521/helowin";
        String dbuser = "xiesen";
        String dbpassword = "xiesen";
        DriudSelectPerson s = new DriudSelectPerson();
        String sql = "select * from SYS_USER";
        PreparedStatement preparedStatement = s.conn.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
        }

    }
}


