package com.github.xiesen.mock.test;

import java.util.Date;

/**
 * @author xiesen
 */
public class AAAA {
    public static void main(String[] args) {
        String str = "network_name=Slot10_ML2__PCIefunction12,network_name_cn=Slot10(ML2)\\,PCIefunction12,workernodeid=999999,workernodeid_cn=999999";
//        String str = "network_system_mb,appprogramname=clear_rts2018bk,appprogramname_cn=clear_rts2018bk,appsystem=fySystem,appsystem_cn=fySystem,clustername=clear_rts2018lanjing,clustername_cn=clear_rts2018蓝鲸,host=datalake-connector-02,hostname=10a180a195a106,hostname_cn=10a180a195a106,ip=10.180.195.106,ip_cn=10.180.195.106,name=LoopbackPseudo-Interface1,name_cn=Loopback\\ Pseudo-Interface\\ 1,servicename=clear_rts2018bk,servicename_cn=clear_rts2018bk _delay=10480,in_bytes=0,in_dropped=0,in_errors=0,in_packets=0,out_bytes=0,out_dropped=0,out_errors=0,out_packets=0 1699343102300000000";

        final String s = str.replaceAll("\\\\,", "");
        System.out.println("原始: " + str);
        System.out.println("过滤: " + s);


        Date date = new Date(1725292800000L);
        System.out.println(date);

    }
}
