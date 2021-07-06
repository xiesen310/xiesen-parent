package com.github.xiesen.hive;

import org.apache.hive.jdbc.HiveStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author 谢森
 * @since 2021/5/21
 */
public class HiveJDBCTest {
    // 驱动程序就是之前在classpath中配置的JDBC的驱动程序的JAR 包中
    public static final String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    // 连接地址是由各个数据库生产商单独提供的，所以需要单独记住
    public static final String HIVE_URL = "jdbc:hive2://cdh-3:10000/hzy?";

    public static void main(String[] args) throws Exception {
        Connection con = null; // 表示数据库的连接对象
        Statement stmt = null; // 表示数据库的更新操作
        ResultSet result = null; // 表示接收数据库的查询结果

        Class.forName(HIVE_DRIVER); // 1、使用CLASS 类加载驱动程序
        con = DriverManager.getConnection(HIVE_URL); // 2、连接数据库
        System.out.println(con);
        stmt = con.createStatement(); // 3、Statement 接口需要通过Connection 接口进行实例化操作
        long time = System.currentTimeMillis();

        //重点在这个里,启动一个线程，来不断轮询进度信息，进度信息封装在HiveStatement中，因为sql执行的时候executeQuery方法会阻塞。
        Thread logThread = new Thread(new LogRunnable((HiveStatement) stmt));
        logThread.setDaemon(true);
        logThread.start();

//        result = stmt.executeQuery("select logtypename, source, offset, dimensions['hostname'] as hostname from hzy" +
//                ".logtest");

        result = stmt.executeQuery("select count(1) as num from hzy.logtest");


        while (result.next()) {
//            String str = result.getString(1);
//            System.out.println(str);
//            System.out.println("use time:" + (System.currentTimeMillis() - time));
        }
        result.close();
        con.close(); // 4、关闭数据库

    }


    /**
     * 进度信息的轮询线程实现
     */
    static class LogRunnable implements Runnable {
        private final HiveStatement hiveStatement;

        LogRunnable(HiveStatement hiveStatement) {
            this.hiveStatement = hiveStatement;
        }

        private void updateQueryLog() {
            try {
                List<String> queryLogs = hiveStatement.getQueryLog();
                for (String log : queryLogs) {
                    System.out.println("进度信息-->" + log);
                }

            } catch (Exception e) {

            }
        }

        @Override
        public void run() {
            try {
                while (hiveStatement.hasMoreLogs()) {
                    updateQueryLog();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
        }
    }

}



