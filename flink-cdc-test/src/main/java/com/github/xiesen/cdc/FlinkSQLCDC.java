package com.github.xiesen.cdc;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author xiesen
 * @title: FlinkSQLCDC
 * @projectName xiesen-parent
 * @description: FlinkSQLCDC
 * @date 2022/11/26 16:06
 */
public class FlinkSQLCDC {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.executeSql("CREATE TABLE test_user (" + " id INT," +
                " name STRING," +
                " sex STRING" +
                ") WITH (" +
                " 'connector' = 'mysql-cdc'," +
                " 'scan.startup.mode' = 'latest-offset'," +
                " 'scan.incremental.snapshot.enabled' = 'false'," +
                " 'hostname' = '192.168.1.222'," +
                " 'port' = '3306'," +
                " 'username' = 'root'," +
                " 'password' = 'Mysql@123'," +
                " 'database-name' = 'xiesen'," +
                " 'table-name' = 'test_user'" +
                ")");


        tableEnv.executeSql("CREATE TABLE test_user2 (" + " id INT," +
                " name STRING," +
                " sex STRING," +
                " PRIMARY KEY (id) NOT ENFORCED" +
                ") WITH (" +
                " 'connector' = 'jdbc'," +
                " 'url' = 'jdbc:mysql://192.168.1.222:3306/xiesen?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai'," +
                " 'driver' = 'com.mysql.jdbc.Driver'," +
                " 'username' = 'root'," +
                " 'password' = 'Mysql@123'," +
                " 'table-name' = 'test_user2'" +
                ")");

//        tableEnv.executeSql("select * from test_user").print();
        tableEnv.executeSql("INSERT INTO test_user2 SELECT id, name, sex FROM test_user");
    }
}
