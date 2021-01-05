package com.github.xiesen.kudu;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.shaded.com.google.common.collect.ImmutableList;
import org.apache.kudu.shaded.com.google.common.collect.Lists;

import java.util.List;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/31 14:41
 */
public class KuduTestMain {
    private static final String KUDU_MASTERS = "cdh-4:7051";
    public static final String KUDU_TABLE_NAME = "streamx_kudu_sink";

    public static void main(String[] args) throws KuduException {
        KuduClient kuduClient = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();
        createExampleTable(kuduClient, KUDU_TABLE_NAME);
//        deleteTable(kuduClient, KUDU_TABLE_NAME);
        kuduClient.close();
    }

    static boolean deleteTable(KuduClient client, String tableName) {
        boolean flag = false;
        try {
            client.deleteTable(tableName);
            System.out.println("删除表 " + tableName + " 成功");
            flag = true;
        } catch (KuduException e) {
            System.out.println("删除表 " + tableName + " 失败,原因为: " + e.getMessage());
        }
        return flag;
    }

    /**
     * 背景：
     * 单column主键设置，与app一一对应的关系
     * 根据Id hash分了两个区。
     *
     * @param client
     * @param tableName
     * @throws KuduException
     */
    static void createExampleTable(KuduClient client, String tableName) throws KuduException {
        deleteTable(client, tableName);
        List<ColumnSchema> columnSchemas = Lists.newArrayList();
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("uuid", Type.STRING).key(true).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("id", Type.STRING).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("name", Type.STRING).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("address", Type.STRING).build());
        Schema schema = new Schema(columnSchemas);
        ImmutableList<String> hashKeys = ImmutableList.of("uuid");
        CreateTableOptions cto = new CreateTableOptions();
        cto.addHashPartitions(hashKeys, 2);
        cto.setNumReplicas(1);
        client.createTable(tableName, schema, cto);
        System.out.println("Create table " + tableName + " success");
    }
}
