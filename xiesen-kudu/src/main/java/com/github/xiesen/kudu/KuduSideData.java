package com.github.xiesen.kudu;

import com.github.xiesen.kudu.mysql.Conn;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.apache.kudu.shaded.com.google.common.collect.ImmutableList;
import org.apache.kudu.shaded.com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 谢森
 * @Description KuduSideData
 * @Email xiesen310@163.com
 * @Date 2021/1/4 9:29
 */
public class KuduSideData {
    private static final String KUDU_MASTERS = "cdh-4:7051";
    private static final String KUDU_TABLE_NAME = "side_country_code";

    public static void main(String[] args) throws Exception {
        KuduClient kuduClient = null;
        KuduTable table = null;
        KuduSession session = null;
        SessionConfiguration.FlushMode mode = SessionConfiguration.FlushMode.AUTO_FLUSH_BACKGROUND;
        try {
            kuduClient = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();
            createSideTable(kuduClient, KUDU_TABLE_NAME);

        } catch (KuduException e) {
            System.out.println("创建 kudu 表失败");
        }

        try {
            session = kuduClient.newSession();
            table = kuduClient.openTable(KUDU_TABLE_NAME);
            mode = SessionConfiguration.FlushMode.AUTO_FLUSH_BACKGROUND;

        } catch (KuduException e) {
            System.out.println("打开 kudu 表异常");
        }


        try {

            Conn conn = new Conn();
            Connection connection = conn.getConnection();
            Statement statement = connection.createStatement();
            String sql = "select * from country_code";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String enName = rs.getString("en_name");
                String chName = rs.getString("ch_name");
                String code = rs.getString("code");
                int telCode = rs.getInt("tel_code");
                int timeDiff = rs.getInt("time_diff");

                /**
                 * 插入数据到 kudu 表
                 */
                Insert insert = table.newInsert();
                PartialRow row = insert.getRow();
                row.addString("code", code);
                row.addString("en_name", enName);
                row.addString("ch_name", chName);
                row.addInt("tel_code", telCode);
                row.addInt("time_diff", timeDiff);
                session.apply(insert);

                System.out.println("enName: " + enName + " ,chName: "
                        + chName + " ,code: " + code + " ,telCode: "
                        + telCode + " ,timeDiff: " + timeDiff);
            }

            if (SessionConfiguration.FlushMode.AUTO_FLUSH_BACKGROUND == mode) {
                session.flush();
                RowErrorsAndOverflowStatus error = session.getPendingErrors();
                if (error.isOverflowed() || error.getRowErrors().length > 0) {
                    if (error.isOverflowed()) {
                        throw new Exception("Kudu overflow exception occurred.");
                    }
                    StringBuilder errorMessage = new StringBuilder();
                    if (error.getRowErrors().length > 0) {
                        for (RowError errorObj : error.getRowErrors()) {
                            errorMessage.append(errorObj.toString());
                            errorMessage.append(";");
                        }
                    }
                    throw new Exception(errorMessage.toString());
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("ResultSet关闭时出现错误");
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Statement关闭时出现错误");
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Connection关闭时出现错误！");
                    e.printStackTrace();
                }
            }
        } catch (SQLException | KuduException e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }

    }


    /**
     * 创建维表
     *
     * @param client
     * @param tableName
     * @throws KuduException
     */
    static void createSideTable(KuduClient client, String tableName) throws KuduException {
        boolean done = client.isCreateTableDone(tableName);
        if (done) {
            client.deleteTable(tableName);
        }

        List<ColumnSchema> columnSchemas = Lists.newArrayList();
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("code", Type.STRING).key(true).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("en_name", Type.STRING).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("ch_name", Type.STRING).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("tel_code", Type.INT32).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("time_diff", Type.INT32).build());
        Schema schema = new Schema(columnSchemas);
        ImmutableList<String> hashKeys = ImmutableList.of("code");
        CreateTableOptions cto = new CreateTableOptions();
        cto.addHashPartitions(hashKeys, 2);
        cto.setNumReplicas(1);
        client.createTable(tableName, schema, cto);
        System.out.println("Create table " + tableName);
    }


}
