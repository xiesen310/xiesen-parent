package com.github.xiesen.kudu;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.shaded.com.google.common.collect.ImmutableList;
import org.apache.kudu.shaded.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/31 14:41
 */
public class KuduTestMain {
    private static final String KUDU_MASTERS = "cdh-4:7051";
    private static final Logger logger = LoggerFactory.getLogger(KuduTestMain.class);

    public static void main(String[] args) throws KuduException {
        KuduClient kuduClient = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();
        createExampleTable(kuduClient, "kudu_test");
//        createExample2Table(kuduClient, "kudu_test2");
//        alterTableAddColumn(kuduClient, "kudu_test2", "domain_id_default", Type.INT32);
//        alterTableDeleteColumn(kuduClient, "kudu_test2", "domain_id_default");
        kuduClient.close();
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
        List<ColumnSchema> columnSchemas = Lists.newArrayList();
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("id", Type.STRING).key(true).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("app", Type.INT8).build());
        Schema schema = new Schema(columnSchemas);
        ImmutableList<String> hashKeys = ImmutableList.of("id");
        CreateTableOptions cto = new CreateTableOptions();
        cto.addHashPartitions(hashKeys, 2);
        cto.setNumReplicas(1);
        client.createTable(tableName, schema, cto);
        System.out.println("Create table " + tableName);
    }
}
