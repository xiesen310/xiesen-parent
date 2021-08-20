package com.github.xiesen.mysql;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.mysql.jdbc.Connection;

public class QueryDemo {
    /**
     * jdbc所属，暂不使用
     */
    private final static String url = "jdbc:mysql://192.168.1.222:3306/xiesen?useSSL=false&characterEncoding=utf8";
    private final static String name = "root";
    private final static String pwd = "Mysql@123";
    private static Connection conn;
    private static PreparedStatement ps;

    public static void main(String[] args) throws Exception {

        QueryDemo test = new QueryDemo();

        // psBatch 时间统计 - 开始
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf.format(new Date());
        System.out.println("psBatchUser 开始时间为：" + startTime);
        System.out.println("psBatchUser 开始执行...");

        // 使用PreparedStatement批量插入
        int idx = test.psBatchUser();

        // 统计时间 - 结束
        System.out.println("psBatchUser 执行完成，共插入" + idx + "条数据");
        String endTime = sdf.format(new Date());
        System.out.println("psBatchUser 结束时间为：" + endTime);

        System.out.println();

        // 时间统计 - 开始
        startTime = sdf.format(new Date());
        System.out.println("psBatchOrder 开始时间为：" + startTime);
        System.out.println("psBatchOrder 开始执行...");

        // 使用SQL语句批量插入
        idx = test.psBatchOrder();

        // 统计时间 - 结束
        System.out.println("psBatchOrder 执行完成，共插入" + idx + "条数据");
        endTime = sdf.format(new Date());
        System.out.println("psBatchOrder 结束时间为：" + endTime);

    }

    /**
     * 随机生成性别
     *
     * @return
     */
    private String getRandomSex() {
        boolean b = new Random().nextBoolean();
        return b ? "woman" : "man";
    }

    /**
     * 获取随机订单描述信息
     *
     * @return
     */
    private String getRandomOrderDesc() {
        return getRandomName() + " order desc";
    }

    /**
     * 随机生成名称
     *
     * @return
     */
    private String getRandomName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用PreparedStatement批量插入
     *
     * @return
     * @throws Exception
     */
    private int psBatchUser() throws Exception {

        int idx = 0;// 行数
        try {
            // 获取数据库连接
            conn = getConnection();
            // 设置不自动提交
            conn.setAutoCommit(false);
            String sql = "insert into sys_user_100w (id, name, sex) values (?, ?, ?)";
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < 1000000; i++) {
                idx++;
                ps.setInt(1, idx);
                ps.setString(2, getRandomName());
                ps.setString(3, getRandomSex());
                ps.addBatch();

                if (idx % 1000 == 0) {
                    ps.executeBatch();
                    conn.commit();
                    ps.clearBatch();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return idx;
    }

    private int psBatchOrder() throws Exception {

        int idx = 0;// 行数
        try {
            // 获取数据库连接
            conn = getConnection();
            // 设置不自动提交
            conn.setAutoCommit(false);
            String sql = "insert into tb_order_100w (id, order_name, order_desc,user_id) values (?, ?, ?,?)";
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < 1000000; i++) {
                idx++;
                ps.setInt(1, idx);
                ps.setString(2, getRandomName());
                ps.setString(3, getRandomOrderDesc());
                ps.setInt(4, idx);
                ps.addBatch();

                if (idx % 1000 == 0) {
                    ps.executeBatch();
                    conn.commit();
                    ps.clearBatch();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return idx;
    }


    /**
     * 获取数据库连接
     * <p>
     * SQL语句
     * </p>
     */
    private Connection getConnection() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection(url, name, pwd);
        return conn;

    }
}
