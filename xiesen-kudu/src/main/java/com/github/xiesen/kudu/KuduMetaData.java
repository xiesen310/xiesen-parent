package com.github.xiesen.kudu;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.ListTablesResponse;

import java.util.List;

/**
 * @author 谢森
 * @Description KuduSideData
 * @Email xiesen310@163.com
 * @Date 2021/1/4 9:29
 */
public class KuduMetaData {
    private static final String KUDU_MASTERS = "cdh-4:7051";
    private static final String KUDU_TABLE_NAME = "side_country_code";

    public static void main(String[] args) {
        KuduClient kuduClient = null;
        try {
            kuduClient = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();
            ListTablesResponse listTablesResponse = kuduClient.getTablesList();
            List<ListTablesResponse.TableInfo> tableInfosList = listTablesResponse.getTableInfosList();
            for (ListTablesResponse.TableInfo tableInfo : tableInfosList) {
                String tableId = tableInfo.getTableId();
                String tableName = tableInfo.getTableName();
                System.err.println("Table Name: " + tableName + " , Table Id: " + tableId);

                try {
                    KuduTable kuduTable = kuduClient.openTable(tableName);
                    printSchema(kuduTable);
                } catch (KuduException e) {
                    System.out.println("打开 kudu 表失败");
                }
            }
        } catch (KuduException e) {
            System.out.println("获取 kudu 表失败");
        } finally {
            try {
                kuduClient.close();
            } catch (KuduException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("all")
    private static void printSchema(KuduTable kuduTable) {
        if (kuduTable != null) {
            Schema schema = kuduTable.getSchema();
            for (int i = 0; i < schema.getColumnCount(); i++) {
                ColumnSchema columnSchema = schema.getColumnByIndex(i);
                String name = columnSchema.getName();
                Type type = columnSchema.getType();
                ColumnSchema.Encoding encoding = columnSchema.getEncoding();
                ColumnSchema.CompressionAlgorithm compressionAlgorithm = columnSchema.getCompressionAlgorithm();
                System.out.println("Column = " + name + " ,ID = " + i + " ,Type = " + type + " ,Encoding = " + encoding + " ,Compression = " + compressionAlgorithm);
            }
        } else {
            System.out.println("kuduTable 为 null");
        }
    }

}
