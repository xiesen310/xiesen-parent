package com.github.xiesen.redis.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author xiesen
 * @title: BaseDao
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2023/1/13 13:34
 */
public class BaseDao {  //  静态工具类，用于创建数据库连接对象和释放资源，方便调用
    //    导入驱动jar包或添加Maven依赖（这里使用的是Maven，Maven依赖代码附在文末）
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //  获取数据库连接对象
    public static Connection getConn() {
        Connection conn = null;
        try {
            //  rewriteBatchedStatements=true,一次插入多条数据，只插入一次
            conn = DriverManager.getConnection("jdbc:mysql://192.168.1.222:3306/cmdb?rewriteBatchedStatements=true&useSSL=false", "root", "Mysql@123");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public static Connection getConn2() {
        Connection conn = null;
        try {
            //  rewriteBatchedStatements=true,一次插入多条数据，只插入一次
            conn = DriverManager.getConnection("jdbc:mysql://192.168.1.222:3306/xiesen?useSSL=false", "root", "Mysql@123");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    //  释放资源
    public static void closeAll(AutoCloseable... autoCloseables) {
        for (AutoCloseable autoCloseable : autoCloseables) {
            if (autoCloseable != null) {
                try {
                    autoCloseable.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /*  因为数据库的处理速度是非常惊人的 单次吞吐量很大 执行效率极高
        addBatch()把若干sql语句装载到一起，然后一次送到数据库执行，执行需要很短的时间
        而preparedStatement.executeUpdate() 是一条一条发往数据库执行的 时间都消耗在数据库连接的传输上面*/
    public static void main(String[] args) {
        long start = System.currentTimeMillis();    //  获取系统当前时间，方法开始执行前记录
        Connection conn = BaseDao.getConn();        //  调用刚刚写好的用于获取连接数据库对象的静态工具类
        String sql = "insert into mymilliontest values(null,?,?,?,NOW())";  //  要执行的sql语句
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);    //  获取PreparedStatement对象
            //  不断产生sql
            for (int i = 0; i < 1000000; i++) {
                ps.setString(1, Math.ceil(Math.random() * 1000000) + "");
                ps.setString(2, Math.ceil(Math.random() * 1000000) + "");
                ps.setString(3, UUID.randomUUID().toString());  //  UUID该类用于随机生成一串不会重复的字符串
                ps.addBatch();  //  将一组参数添加到此 PreparedStatement 对象的批处理命令中。
            }
            int[] ints = ps.executeBatch();//   将一批命令提交给数据库来执行，如果全部命令执行成功，则返回更新计数组成的数组。
            //  如果数组长度不为0，则说明sql语句成功执行，即百万条数据添加成功！
            if (ints.length > 0) {
                System.out.println("已成功添加一百万条数据！！");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeAll(conn, ps);  //  调用刚刚写好的静态工具类释放资源
        }
        long end = System.currentTimeMillis();  //  再次获取系统时间
        System.out.println("所用时长:" + (end - start) / 1000 + "秒");  //  两个时间相减即为方法执行所用时长
    }

}
