package com.github.xiesen.ck;

import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiesen
 * @title: Test
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/13 15:13
 */
public class Test {
    public static void main(String[] args) throws SQLException {
//        search();

    }


    private static void insertData() throws SQLException {
        String driverName = "ru.yandex.clickhouse.ClickHouseDriver";
        String database = "xiesen";
        String url = "jdbc:clickhouse://192.168.90.25:8123/xiesen";
        String user = "default";
        String password = "admin";
        ClickHouseProperties prop = new ClickHouseProperties();
        prop.setUser(user);
        prop.setPassword(password);
        prop.setDatabase(database);
        ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(url, prop);
        ClickHouseConnection connection = clickHouseDataSource.getConnection();

        ClickHouseUtils clickHouseUtils = new ClickHouseUtils();
    }

    private static void search() throws SQLException {
        String driverName = "ru.yandex.clickhouse.ClickHouseDriver";
        String database = "xiesen";
        String url = "jdbc:clickhouse://192.168.90.25:8123/xiesen";
        String user = "default";
        String password = "admin";
        ClickHouseProperties prop = new ClickHouseProperties();
        prop.setUser(user);
        prop.setPassword(password);
        prop.setDatabase(database);
        ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(url, prop);
        ClickHouseConnection connection = clickHouseDataSource.getConnection();

        String sql = "select timestamp,ip from  dwd_default_metrics2 where timestamp > '2022-10-13 15:32:00' limit 10";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "  " + resultSet.getString(2));

        }
    }
}
