package com.github.xiesen.mock.test;

import com.github.xiesen.mock.util.CkUtils;
import ru.yandex.clickhouse.ClickHouseStatement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiesen
 * @title: CkDataStatistical
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/3/8 16:17
 */
public class CkDataStatistical {
    private static String url = "jdbc:clickhouse://192.168.90.25:8123";
    private static String database = "xiesen";
    private static String username = "default";
    private static String password = "admin";

    public static void main(String[] args) throws SQLException {
        // 获取所有指标集
        // 根据指标集进行查询统计
        // a) 统计当前指标集下的数据条数
        // b) 去重统计当前指标集下的数据条数
        // 比较 a 和 b 的值,如果不相同,输出 指标集名称

        ClickHouseStatement statement = CkUtils.getStatement(url, database, username, password);
        ResultSet resultSet = statement.executeQuery("select distinct metricsetname from xiesen.metric");
        Set<String> set = processResult(resultSet);
        for (String metricName : set) {
            String countSql = "select count() from xiesen.metric where metricsetname = '" + metricName + "'";
            String countDistinctSql = "select count(distinct name) from xiesen.metric where metricsetname = '" + metricName + "'";
            ResultSet countResultSet = statement.executeQuery(countSql);
            Long countNumber = processResult2(countResultSet);
            ResultSet countDistinctResultSet = statement.executeQuery(countDistinctSql);
            Long distinctCountNumber = processResult2(countDistinctResultSet);
            if (countNumber != distinctCountNumber) {
                System.out.println("metricsetname is " + metricName + ", countNumber = " + countNumber + " , distinctCountNumber = " + distinctCountNumber);
            }
        }

    }

    public static Long processResult2(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Long number = 0L;
        while (resultSet.next()) {
            for (int i = 1; i < columnCount + 1; i++) {
                number = resultSet.getLong(i);
            }
        }
        return number;
    }

    public static Set<String> processResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Set<String> row = new HashSet<>(250);
        while (resultSet.next()) {
            for (int i = 1; i < columnCount + 1; i++) {
                row.add(resultSet.getString(i));
            }
        }
        return row;
    }


    public static void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i < columnCount + 1; i++) {
                row.add(resultSet.getObject(i));
            }
            System.out.println(row);
        }
    }
}
