package com.github.xiesen.druid;

import org.apache.calcite.avatica.AvaticaConnection;
import org.apache.calcite.avatica.AvaticaStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class TestDruid {
    public static void main(String[] args) throws SQLException {
        AvaticaConnection connection = connection();
        AvaticaStatement statement = connection.createStatement();
        String sql = "SELECT __time,added,channel,cityName,comment,commentLength,countryIsoCode,countryName FROM wikipedia LIMIT 5";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("__time"));
        }
    }

    public static AvaticaConnection connection() throws SQLException {
        String urlStr = "jdbc:avatica:remote:url=http://192.168.1.95:8888/druid/v2/sql/avatica/";
        Properties connectionProperties = new Properties();
        connectionProperties.put("Content-Type", "application/json; charset=utf-8");
        return (AvaticaConnection) DriverManager.getConnection(urlStr, connectionProperties);

    }
}
