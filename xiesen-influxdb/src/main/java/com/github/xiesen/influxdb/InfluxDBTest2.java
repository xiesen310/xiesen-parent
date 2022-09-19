package com.github.xiesen.influxdb;

import java.util.*;

/**
 * @author 谢森
 * @since 2021/7/7
 */
public class InfluxDBTest2 {
    public static void main(String[] args) {
        String username = "admin";
        String password = "admin";
        String openurl = "http://192.168.70.65:7076";
        String database = "xs";
        InfluxDBTest2 influxDBTest = new InfluxDBTest2();

        InfluxDBConnect connect = new InfluxDBConnect(username, password, openurl, database, 1, 2);
        connect.connection();
//        connect.createRetentionPolicy();
        Set<String> measurements = connect.getMeasurements();
        Set<String> tags = connect.getTagKeys("prometheus_znode_count");
        Set<String> fieldKeys = connect.getFieldKeys("prometheus_znode_count");
    }

    private List<InfluxDbRow> getRows() {
        List<InfluxDbRow> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            InfluxDbRow influxDbRow = new InfluxDbRow();
            String measurement = "kafkaConsumer";
            influxDbRow.setTimeSecond(System.currentTimeMillis());
            influxDbRow.setMeasurement(measurement);
            influxDbRow.setTags(getTags(i));
            influxDbRow.setFields(getFields());
            list.add(influxDbRow);
        }
        return list;
    }


    private Map<String, String> getTags(int num) {
        Map<String, String> tags = new HashMap<>(2);
        tags.put("job_id", UUID.randomUUID().toString().replaceAll("-", ""));
        tags.put("tm_id", "tm_" + 1);
        return tags;
    }

    private Map<String, Object> getFields() {
        Map<String, Object> fields = new HashMap<>(1);
        fields.put("value", 0.0);
        fields.put("oraginal_timestamp", 1.648550956867E12);
        return fields;
    }

}
