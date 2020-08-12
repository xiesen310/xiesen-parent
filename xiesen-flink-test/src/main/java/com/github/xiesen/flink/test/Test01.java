package com.github.xiesen.flink.test;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/7/27 13:26
 */
public class Test01 {


    public static void main(String[] args) throws Exception {
        String sql = "create table jty_gfoc_err_filebeat ( WATERMARK FOR timestamp AS withOffset('timestamp', 60000), timestamp bigint, source varchar, offset varchar, appprogramname varchar, appsystem varchar, hostname varchar, collecttime varchar, logstash_deal_ip varchar, logstash_deal_name varchar, message varchar ) with ( type = 'kafka11', zookeeperQuorum = 'kafka-1:2181/kafka110,kafka-2:2181/kafka110,kafka-3:2181/kafka110', kafka.key.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer', sourceDataType = 'logavro', kafka.value.deserializer = 'org.apache.kafka.common.serialization.ByteArrayDeserializer', bootstrapServers = 'kafka-1:19092,kafka-2:19092,kafka-3:19092', groupId = 'default_group', parallelism = '1', topic = 'dwd_default_log', schemaString = 'hostname,appprogramname,appsystem||collecttime,logstash_deal_name,logstash_deal_ip,message', offsetReset = 'latest' ); create table jty_gfoc_err_connectpool_1min_fromlog ( timestamp TIMESTAMP, appprogramname varchar, appsystem varchar, hostname varchar, gfoc_err_count double ) with ( type = 'kafka11', zookeeperQuorum = 'kafka-1:2181/kafka110,kafka-2:2181/kafka110,kafka-3:2181/kafka110', kafka.key.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer', kafka.value.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer', bootstrapServers = 'kafka-1:19092,kafka-2:19092,kafka-3:19092', sinkDataType = 'metricavro', parallelism = '2', topic = 'dwd_all_metric', schemaString = 'hostname,appprogramname,appsystem|gfoc_err_count|' ); insert into jty_gfoc_err_connectpool_1min_fromlog select hostname as hostname, appsystem as appsystem, appprogramname as appprogramname, cast(count(1) as double) AS gfoc_err_count, TUMBLE_START(`timestamp`, INTERVAL '1' MINUTE) AS `timestamp` from jty_gfoc_err_filebeat where message like '%创建连接池失败%' group by hostname, appsystem, appprogramname, TUMBLE(`timestamp`, INTERVAL '1' MINUTE);";
        System.out.println(sql);
        System.out.println("sql 中是否包含 % : " + sql.contains("%"));

        sql = sql.replaceAll("%", "#");
        System.out.println("-----------------------------------------------");
        System.out.println(sql);
        System.out.println("sql 中是否包含 % : " + sql.contains("%"));
    }
}
