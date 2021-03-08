package com.github.xiesen.hbase;

import com.github.xiesen.hbase.utils.CountryCode;
import com.github.xiesen.hbase.utils.JdbcUtils;
import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令:
 * <p>
 * disable 'xiesen:t1' // 禁用表
 * <p>
 * drop 'xiesen:t1' // 删除表
 * <p>
 * scan 'xiesen:country_hbase_side' // 扫描表
 * list // 查看表以及 namespace
 * create_namespace 'xiesen' // 创建 namespace
 * user_permission // 查看用户权限
 *
 * @author 谢森
 * @Description InsertSideData2Hbase
 * @Email xiesen310@163.com
 * @Date 2021/1/7 9:34
 */
public class InsertSideData2Hbase {
    public static void main(String[] args) throws IOException, ServiceException {
        List<CountryCode> models = queryAllRecord();
        insertHbaseWithKerberos(models);
    }


    /**
     * kerberos 认证 写入
     *
     * @param models
     * @throws IOException
     */
    public static void insertHbaseWithKerberos(List<CountryCode> models) throws IOException, ServiceException {

        String KERB_CONF = "D:\\tmp\\kerberos\\krb5.conf";
        String KEYTAB_FILE = "D:\\tmp\\kerberos\\admin.keytab";
        String principal = "admin@ZORKDATA.COM";

        System.setProperty("java.security.krb5.conf", KERB_CONF);
        // System.setProperty("sun.security.krb5.debug", "true");

        Configuration conf = HBaseConfiguration.create();
        conf.addResource(new Path("D:\\tmp\\kerberos\\hbase-conf\\core-site.xml"));
        conf.addResource(new Path("D:\\tmp\\kerberos\\hbase-conf\\hbase-site.xml"));

        // Kerberos认证
        // 设置java安全krb5.conf
        System.setProperty("java.security.krb5.conf", KERB_CONF);
        // 设置用户主体(Principal)
        conf.set("kerberos.principal", principal);
        // 使用用户keytab文件认证
        conf.set("keytab.file", KEYTAB_FILE);

        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(principal, KEYTAB_FILE);
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();

        String tabName = "xiesen:country_hbase_side";
        //String[] families = new String[] { "enName", "chName", "code", "telCode", "timeDiff" };
        String[] families = new String[]{"cf"};
        if (admin.tableExists(TableName.valueOf(tabName))) {
            System.out.println("表存在,删除");
            admin.disableTable(TableName.valueOf(tabName));
            admin.deleteTable(TableName.valueOf(tabName));
        }

        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tabName));
        for (String family : families) {
            tableDesc.addFamily(new HColumnDescriptor(family));
        }
        admin.createTable(tableDesc);

        for (CountryCode model : models) {
            HTable htable = (HTable) conn.getTable(TableName.valueOf(tabName));
            Put p = new Put(Bytes.toBytes(model.getCode()));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("enName"),
                    Bytes.toBytes(String.valueOf(model.getEn_name().trim())));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("chName"),
                    Bytes.toBytes(String.valueOf(model.getCh_name().trim())));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("code"), Bytes.toBytes(String.valueOf(model.getCode())));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("telCode"),
                    Bytes.toBytes(String.valueOf(model.getTel_code())));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("timeDiff"),
                    Bytes.toBytes(String.valueOf(model.getTime_diff())));
            htable.put(p);
        }

        conn.close();

    }

    /**
     * 无 kerberos 认证 写入
     *
     * @param models
     * @throws IOException
     */
    public static void insertHbase(List<CountryCode> models) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "cdh-3,cdh-4,cdh-5");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("zookeeper.znode.parent", "/hbase");

        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();

        String tabName = "xiesen:country_hbase_side";
        //String[] families = new String[] { "en_name", "ch_name", "code", "tel_code", "time_diff" };
        String[] families = new String[]{"cf"};
        if (admin.tableExists(TableName.valueOf(tabName))) {
            System.out.println("表存在,删除");
            admin.disableTable(TableName.valueOf(tabName));
            admin.deleteTable(TableName.valueOf(tabName));
        }

        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tabName));
        for (String family : families) {
            tableDesc.addFamily(new HColumnDescriptor(family));
        }
        admin.createTable(tableDesc);

        for (CountryCode model : models) {
            HTable htable = (HTable) conn.getTable(TableName.valueOf(tabName));
            Put p = new Put(Bytes.toBytes(model.getCode()));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("enName"),
                    Bytes.toBytes(String.valueOf(model.getEn_name().trim())));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("chName"),
                    Bytes.toBytes(String.valueOf(model.getCh_name().trim())));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("code"), Bytes.toBytes(String.valueOf(model.getCode())));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("telCode"),
                    Bytes.toBytes(String.valueOf(model.getTel_code())));
            p.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("timeDiff"),
                    Bytes.toBytes(String.valueOf(model.getTime_diff())));
            htable.put(p);
        }

        conn.close();

    }


    public static List<CountryCode> queryAllRecord() {
        String selectSQL = "select * from country_code";
        List<CountryCode> lists = new ArrayList<>();

        JdbcUtils.getInstance().executeQuery(selectSQL, new JdbcUtils.QueryCallBack() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    String en_name = rs.getString(1);
                    String ch_name = rs.getString(2);
                    String code = rs.getString(3);
                    int tel_code = rs.getInt(4);
                    int time_diff = rs.getInt(5);

                    CountryCode model = new CountryCode(en_name, ch_name, code, tel_code, time_diff);
                    lists.add(model);
                }
            }
        });

        return lists;
    }
}
