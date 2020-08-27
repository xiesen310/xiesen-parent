package com.github.xiesen.flink.test;

import java.sql.*;


/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/8/19 11:32
 */
public class MysqlInsertTest {
    public static void add(Integer id, Integer status) {
        Connection con;//声明一个连接对象
        //遍历查询结果集
        try {
            con = MySQLconnection.getConnection();//1.调用方法返回连接
            if (!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            Statement statement = con.createStatement(); //2.创建statement类对象，用来执行SQL语句！！

            String sql = "INSERT INTO xiesen_test(id,status) values('%s','%s')";//要执行的SQL语句
            if (statement.executeUpdate(String.format(sql, id, status)) != 0)
                System.out.println("插入成功");
            else
                System.out.println("插入失败");
            MySQLconnection.close(statement);
            MySQLconnection.close(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void add2(Integer id, Integer status) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver loaded");

        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://192.168.1.69:3306/test", "root", "springmvc");
        System.out.println("Databases connected");

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO xiesen_test(id,status) values(?,?)");

        preparedStatement.setObject(1, id);
        preparedStatement.setObject(2, null);

        int i = preparedStatement.executeUpdate();
        System.out.println(i);

        connection.close();
    }

    public static void main(String[] args) throws Exception {
        int id = 3;
        Integer status = 1;
        add2(id, status);
    }

}
