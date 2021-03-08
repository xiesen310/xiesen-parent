package com.github.xiesen.asynchbase;

import com.alibaba.fastjson.JSONObject;
import com.stumbleupon.async.Deferred;
import com.stumbleupon.async.DeferredGroupException;
import org.hbase.async.HBaseClient;
import org.hbase.async.KeyValue;
import org.hbase.async.Scanner;
import org.hbase.async.auth.KerberosClientAuthProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author 谢森
 * @since 2021/3/2
 */
public class AsyncHbaseMain {
    public static final int DEFAULT_POOL_SIZE = 5;
    public static final String TABLE_NAME = "xiesen:country_hbase_side";
    public static final Logger LOG = LoggerFactory.getLogger(AsyncHbaseMain.class);
    public static final String quorum_spec = "cdh-3:2181,cdh-4:2181,cdh-5:2181";
    public static final String quorum_spec_kerberos = "data180:2181,data181:2181,data182:2181";
    public static final String base_path = "/hbase";
    public static Executor executor = null;

    static List<Deferred<Boolean>> doList(HBaseClient client)
            throws Throwable {
        final Scanner scanner = client.newScanner(TABLE_NAME);
//        scanner.setFamily(INFO_FAM);
//        scanner.setQualifier(PASSWORD_COL);

        ArrayList<ArrayList<KeyValue>> rows = null;
        ArrayList<Deferred<Boolean>> workers
                = new ArrayList<Deferred<Boolean>>();

        //线程阻塞直到返回所有查询结果
        while ((rows = scanner.nextRows(1).joinUninterruptibly()) != null) {
            LOG.info("received a page of users.");
            for (ArrayList<KeyValue> row : rows) {
                JSONObject jsonObject = new JSONObject();
                String rowKey = null;
                JSONObject jsonObject1 = new JSONObject();
                for (int i = 0; i < row.size(); i++) {
                    KeyValue kv = row.get(i);
                    byte[] expected = kv.value();
                    rowKey = new String(kv.key());
                    byte[] family = kv.family();
                    byte[] qualifier = kv.qualifier();
                    jsonObject1.put(new String(family) + ":" + new String(qualifier), new String(expected));
                }
                jsonObject.put(rowKey, jsonObject1.toJSONString());
                System.out.println("jsonObject 对象: " + jsonObject);
                // System.out.println("row对象: " + row);
            }
        }
        return workers;
    }

    public static void main(String[] args) throws Throwable {

//        HBaseClient hBaseClient = getHBaseClient();
        HBaseClient hBaseClient = getHBaseClientWithKerberos();
        Deferred<ArrayList<Boolean>> d = Deferred.group(doList(hBaseClient));
        try {
            d.join();
        } catch (DeferredGroupException e) {
            LOG.info(e.getCause().getMessage());
        }

        //线程阻塞直到shutdown完成
        hBaseClient.shutdown().joinUninterruptibly();

    }

    /**
     * 获取 hbase 客户端
     *
     * @return
     */
    private static HBaseClient getHBaseClient() {
        return new HBaseClient(quorum_spec);
    }


    private static HBaseClient getHBaseClientWithKerberos() {

        System.setProperty("java.security.auth.login.config", "D:\\tmp\\kerberos\\hbase-conf\\jaas.conf");
        System.setProperty("zookeeper.sasl.client", "false");
        System.setProperty("java.security.krb5.conf", "D:\\tmp\\kerberos\\krb5.conf");

        org.hbase.async.Config asyncConfig = new org.hbase.async.Config();
        asyncConfig.overrideConfig("hbase.zookeeper.quorum", quorum_spec_kerberos);
        asyncConfig.overrideConfig("hbase.security.auth.enable", "true");
        asyncConfig.overrideConfig("hbase.security.authentication", "kerberos");
        asyncConfig.overrideConfig("hbase.sasl.clientconfig", "Client");
        asyncConfig.overrideConfig("hbase.kerberos.regionserver.principal", "hbase/_HOST@ZORKDATA.COM");
        HBaseClient hbaseClient = new HBaseClient(asyncConfig);

        KerberosClientAuthProvider authProvider = new KerberosClientAuthProvider(hbaseClient);

        return hbaseClient;
    }
}


class CheckResult {

    private boolean connect;

    private String exceptionMsg;

    CheckResult(boolean connect, String msg) {
        this.connect = connect;
        this.exceptionMsg = msg;
    }

    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }
}

