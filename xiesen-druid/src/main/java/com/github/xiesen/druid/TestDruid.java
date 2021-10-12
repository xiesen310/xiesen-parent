package com.github.xiesen.druid;

import com.github.xiesen.druid.constant.DruidConstants;
import com.github.xiesen.druid.utils.PropertiesUtil;
import org.apache.calcite.avatica.AvaticaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TestDruid {
    public static final String LIMIT_SUFFIX = " LIMIT ? OFFSET ?";

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new RuntimeException("请指定配置文件路径");
        }
        Properties props = PropertiesUtil.getProperties(args[0]);
        System.out.println("param: " + props);
        String url = props.getProperty(DruidConstants.DRUID_URL);
        int pageSize = Integer.valueOf(props.getProperty(DruidConstants.DRUID_PAGE_SIZE));
        String sql = props.getProperty(DruidConstants.BIZ_SQL);
        String tableName = props.getProperty(DruidConstants.DRUID_TABLE_NAME);

        AvaticaConnection connection = connection(url);
        int pageCount = getPageCount(connection, tableName, pageSize);
        if (sql.toUpperCase().trim().contains("LIMIT")) {
            sql = sql.replaceAll("limit", "LIMIT");
            sql = sql.split("LIMIT")[0] + LIMIT_SUFFIX;
        } else {
            sql = sql + LIMIT_SUFFIX;
        }
        for (int i = 1; i <= pageCount; i++) {
            getPages(connection, sql, i, pageSize);
        }
    }

    public static void getPages(AvaticaConnection connection, String sql, int pageNo, int pageSize) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int startPage = (pageNo - 1) * pageSize;
        preparedStatement.setInt(1, pageSize);
        preparedStatement.setInt(2, startPage);
        String realExecuteSql = getRealExecuteSql(sql, pageSize, startPage);
        System.out.println(realExecuteSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        printResultSet(resultSet);
    }


    public static String getRealExecuteSql(String sql, Object... args) {
        String replace = sql.replace("?", "%s");
        return String.format(replace, args);
    }

    private String transSql(List<Object> parameters, String sql) {
        if (sql.indexOf("?") < 0) {
            return sql;
        }
        for (int i = 0; i < parameters.size(); i++) {
            sql = sql.replaceFirst("\\?", parameters.get(i) != null ? "\'"
                    + parameters.get(i).toString() + "\'" : "NULL");
        }
        return sql;
    }


    public static AvaticaConnection connection(String url) throws SQLException {
        Properties connectionProperties = new Properties();
        connectionProperties.put("Content-Type", "application/json; charset=utf-8");
        return (AvaticaConnection) DriverManager.getConnection(url, connectionProperties);
    }


    /**
     * 计算 count
     *
     * @param connection
     * @param pageSize
     * @return
     * @throws SQLException
     */
    public static int getPageCount(AvaticaConnection connection, String tableName, int pageSize) throws SQLException {
        String sql = "select count(*) from " + tableName;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int rowsCount = resultSet.getInt(1);
        int pageCount = (int) Math.ceil(1.0 * rowsCount / pageSize);//算出总共需要多少页
        return pageCount;
    }

    public static void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println("----------------- print result set start -----------------");
        /*while (resultSet.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i < columnCount + 1; i++) {
                row.add(resultSet.getObject(i));
            }
            System.out.println(row);
        }*/
        System.out.println("----------------- print result set end -----------------");
    }
}
