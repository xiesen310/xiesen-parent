package com.github.xiesen.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.*;

/**
 * @author 谢森
 * @since 2021/7/7
 */
public class InfluxDBTest {
    public static void main(String[] args) {
        String username = "admin";
        String password = "admin";
        String openurl = "http://192.168.90.24:8086";
        String database = "xiesen";
        InfluxDBTest influxDBTest = new InfluxDBTest();

        InfluxDBConnect connect = new InfluxDBConnect(username, password, openurl, database, 1, 2);
        connect.connection();
        connect.createRetentionPolicy();
        connect.createDatabase(database);

        final InfluxDB influxDB = InfluxDBFactory.connect(openurl, username, password);
//        influxDB.query("drop measurement cpu_used_pct")
        final QueryResult query = influxDB.query(new Query("show measurements", "xs"));
        List<String> list = new ArrayList<>();
        String prefix = "cpu_used_pct_";
        final List<QueryResult.Result> results = query.getResults();
        results.forEach((k -> {
            final List<QueryResult.Series> series = k.getSeries();
            series.forEach((s) -> {
                final List<List<Object>> values = s.getValues();
                values.forEach(v -> {
                    final String s1 = v.get(0).toString();
                    System.out.println(s1);
                    if (s1.startsWith(prefix)) {
                        list.add(s1);
                    }
                });

            });

        }));

        for (String s : list) {
            influxDB.query(new Query("drop measurement " + s, "xs"));
        }

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
