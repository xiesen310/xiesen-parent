package com.github.xiesen.influxdb;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/7/7
 */
public class InfluxDBTest1 {
    public static void main(String[] args) {
        String username = "admin";
        String password = "admin";
//        String openurl = "http://192.168.1.95:8086";
        String openurl = "http://192.168.70.170:8086";
        String database = "xiesen";

        InfluxDBConnect connect = new InfluxDBConnect(username, password, openurl, database, 1, 2);
        connect.connection();

        connect.createRetentionPolicy();


        /**
         * 'taskmanager_job_task_operator_KafkaConsumer_sync-time-max,
         * host=dpombd-omhd063,
         * job_id=647e666036a69f34ebd5847661a823b2,
         * job_name=log2es_log_jty4_1_checked_02171220,
         * operator_id=b8537c9c45484f0f0bcb3f3105ae455a,
         * operator_name=Source:\ zorkdata_default_kafka_table,
         * subtask_index=1,
         * task_attempt_id=0207d1c3c39e5833964b74135d1b2a72,
         * task_attempt_num=0,
         * task_id=b8537c9c45484f0f0bcb3f3105ae455a,
         * task_name=Source:\ zorkdata_default_kafka_table,
         * tm_id=container_e21_1620721691038_1860_01_000002
         * value=-? 1625536453966000000': invalid number
         */
        String measurement = "taskmanager_job_task_operator_KafkaConsumer";
        Map<String, String> tags = new HashMap<>();
        tags.put("host", "dpombd-omhd063");
        tags.put("job_id", "647e666036a69f34ebd5847661a823b2");
        tags.put("job_name", "log2es_log_jty4_1_checked_02171220");
//        tags.put("operator_id", "b8537c9c45484f0f0bcb3f3105ae455a");
//        tags.put("operator_name", "Source:\\ zorkdata_default_kafka_table");

        Map<String, Object> fields = new HashMap<>();
        fields.put("value", 0.3);
        InfluxDbRow influxDbRow = new InfluxDbRow();
        influxDbRow.setTimeSecond(3600L);
        influxDbRow.setMeasurement(measurement);
        influxDbRow.setTags(tags);
        influxDbRow.setFields(fields);

//        connect.insert(influxDbRow);
        
    }

    /**
     *  partial write: unable to parse '
     *  taskmanager_job_task_operator_heartbeat-response-time-max,
     *  host=dpombd-omhd063,
     *  job_id=647e666036a69f34ebd5847661a823b2,
     *  job_name=log2es_log_jty4_1_checked_02171220,
     *  operator_id=b8537c9c45484f0f0bcb3f3105ae455a,
     *  operator_name=Source:\ zorkdata_default_kafka_table,
     *  subtask_index=5,
     *  task_attempt_id=0c89a3655265bd8f40820297d1fcaafc,
     *  task_attempt_num=0,
     *  task_id=b8537c9c45484f0f0bcb3f3105ae455a,
     *  task_name=Source:\ zorkdata_default_kafka_table,
     *  tm_id=container_e21_1620721691038_1860_01_000002
     *  value=-? 1625536463999000000': invalid number
     */
}
