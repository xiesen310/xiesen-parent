package com.github.xiesen.ck;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @author xiesen
 * @title: Utils
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/13 15:06
 */
public interface Utils {
    public Connection connection(ConnEntiy connEntiy);

    public void close(AutoCloseable... closes);

    public boolean insert(Connection connection, String sql, String... params);

    public boolean delete(Connection connection, String sql, String... params);

    public ResultSet QueryResultSet(Connection connection, String sql, String... params);

}
