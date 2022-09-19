package com.github.xiesen.influxdb.datax;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.influxdb.InfluxDBConnect;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiesen
 * @title: DataxTest
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/21 19:15
 */
public class DataxTest {
    public static final String INFLUXDB_ENDPOINT = "http://192.168.70.65:7076";
    public static final String INFLUXDB_USERNAME = "admin";
    public static final String INFLUXDB_PASSWORD = "admin";
    public static final String INFLUXDB_DATABASE = "xs";
    public static final Long INFLUXDB_SPLITINTERVALS = 86400000L;
    public static final String INFLUXDB_BEGINDATETIME = "2022-08-21 00:00:00";
    public static final String INFLUXDB_ENDDATETIME = "2022-08-21 17:20:00";

    public static final String CK_USERNAME = "default";
    public static final String CK_PASSWORD = "admin";
    public static final String CK_URL = "jdbc:clickhouse://192.168.90.25:8123/xiesen";
    public static final String CK_DATABASE = "xiesen";

    public static void main(String[] args) {
        InfluxDBConnect connect = new InfluxDBConnect(INFLUXDB_USERNAME, INFLUXDB_PASSWORD,
                INFLUXDB_ENDPOINT, INFLUXDB_DATABASE, 1, 2);
        connect.connection();
        Set<String> measurements = connect.getMeasurements();
        for (String measurement : measurements) {
            JSONObject speed = new JSONObject();
            speed.put("channel", 1);
            JSONObject errorLimit = new JSONObject();
            errorLimit.put("record", 0);
            errorLimit.put("percentage", 0.02);
            DataxSetting setting = DataxSetting.builder().speed(speed).errorLimit(errorLimit).build();

            Set<String> tagKeys = connect.getAllKeys(measurement);

            String readerName = "influxdbreader";
            InfluxdbReaderParameter influxdbReaderParameter = InfluxdbReaderParameter.builder().endpoint(INFLUXDB_ENDPOINT).username(INFLUXDB_USERNAME).password(INFLUXDB_PASSWORD)
                    .database(INFLUXDB_DATABASE).splitIntervalS(INFLUXDB_SPLITINTERVALS).beginDateTime(INFLUXDB_BEGINDATETIME)
                    .endDateTime(INFLUXDB_ENDDATETIME).measurement(measurement).column(tagKeys).build();
            DataxReader reader = DataxReader.builder().name(readerName).parameter(influxdbReaderParameter).build();

            String writerName = "clickhousewriter";
            JSONObject connection = new JSONObject();
            connection.put("jdbcUrl", CK_URL);
            connection.put("table", Arrays.asList(measurement));
            String preSql = "truncate table " + CK_DATABASE + "." + measurement;
            Set<String> ckKeys = getCkKeys(tagKeys);
            ClickhouseWriterParameter writerParameter = ClickhouseWriterParameter.builder().username(CK_USERNAME)
                    .password(CK_PASSWORD).column(ckKeys).connection(Arrays.asList(connection))
                    .preSql(Arrays.asList(preSql)).build();
            DataxWriter writer = DataxWriter.builder().name(writerName).parameter(writerParameter).build();
            DataxContent dataxContent = DataxContent.builder().reader(reader).writer(writer).build();
            List<DataxContent> content = Arrays.asList(dataxContent);
            DataxJob dataxJob = DataxJob.builder()
                    .setting(setting)
                    .content(content).build();

            DataxConfig dataxConfig = DataxConfig.builder().job(dataxJob).build();

            System.out.println("datax 配置文件: " + JSONObject.toJSONString(dataxConfig));

            StringBuilder builder = new StringBuilder();
            builder.append("create table if not exists ").append(CK_DATABASE).append(".").append(measurement);
            builder.append("(");

            ckKeys.forEach(k -> {
                if ("value".equalsIgnoreCase(k)) {
                    builder.append("`").append(k).append("`").append("  ").append("Float64").append(",");
                } else {
                    builder.append("`").append(k).append("`").append("  ").append("String").append(",");
                }
            });

            builder.append("`").append("ck_time").append("`").append("  ").append("DateTime64(3, 'Asia/Shanghai')  ").append("DEFAULT now64()");
            builder.append(")");
            builder.append("ENGINE = MergeTree ORDER BY uid PARTITION BY toYYYYMMDD(ck_time) SETTINGS index_granularity = 8192;");
            System.out.println("create ck table sql : " + builder.toString());


            System.out.println();
        }


    }

    public static Set<String> getCkKeys(Set<String> tagKeys) {
        Set<String> set = new LinkedHashSet<>(tagKeys.size());
        Set<String> clone = ObjectUtil.clone(tagKeys);
        if (null != tagKeys && tagKeys.size() > 0) {
            clone.removeIf(Constant.UID::equalsIgnoreCase);
            clone.removeIf(Constant.TIME::equalsIgnoreCase);
            set.add(Constant.UID.replaceAll("_", ""));
            set.add(Constant.TIME.replaceAll("_", ""));
            set.addAll(clone);
        }
        return set;
    }
}
