package com.github.xiesen.mock.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.clickhouse.*;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author 谢森
 * @since 2021/7/26
 */
public class CkUtils {
    public static final Logger log = LoggerFactory.getLogger(CkUtils.class);

    private static ClickHouseConnectionImpl connection = null;
    private static ClickHousePreparedStatement preparedStatement = null;
    private static ClickHouseStatement statement = null;

    /**
     * @param url      连接信息
     * @param database 数据库
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static ClickHouseStatement getStatement(String url, String database, String username, String password) {
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setUser(username);
        properties.setPassword(password);
        properties.setDatabase(database);
        try {
            ClickHouseDataSource dataSource = new ClickHouseDataSource(url, properties);
            connection = (ClickHouseConnectionImpl) dataSource.getConnection();
            if (connection != null) {
                if (log.isDebugEnabled()) {
                    log.debug("connection success.");
                }
            }
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 分布式获取 ck statement
     *
     * @param url      连接信息
     * @param database 数据库
     * @param username 用户名
     * @param password 密码
     * @return ClickHouseStatement
     */
    public static ClickHouseStatement getDistributedStatement(String url, String database, String username,
                                                              String password) {
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setUser(username);
        properties.setPassword(password);
        properties.setDatabase(database);

        try {
            BalancedClickhouseDataSource dataSource = new BalancedClickhouseDataSource(url, properties);
            connection = (ClickHouseConnectionImpl) dataSource.getConnection();
            if (connection != null) {
                if (log.isDebugEnabled()) {
                    log.debug("connection success.");
                }
            }
            statement = connection.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statement;
    }


    public static ClickHouseConnection getDistributedConnect(String url, String database, String username,
                                                             String password) {
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setUser(username);
        properties.setPassword(password);
        properties.setDatabase(database);
        ClickHouseConnectionImpl connection = null;
        try {
            BalancedClickhouseDataSource dataSource = new BalancedClickhouseDataSource(url, properties);
            connection = (ClickHouseConnectionImpl) dataSource.getConnection();
            if (connection != null) {
                if (log.isDebugEnabled()) {
                    log.debug("connection success.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * 关闭连接
     */
    public static void close() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


}
