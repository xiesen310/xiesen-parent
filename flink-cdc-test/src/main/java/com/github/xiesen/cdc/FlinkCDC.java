package com.github.xiesen.cdc;

import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import com.ververica.cdc.debezium.StringDebeziumDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xiesen
 * @title: FlinkCDC
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/11/26 15:00
 */
public class FlinkCDC {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        final DebeziumSourceFunction<String> sourceFunction = MySqlSource.<String>builder()
                .hostname("192.168.1.222")
                .port(3306)
                .username("root")
                .password("Mysql@123")
                .databaseList("xiesen")
                .tableList("xiesen.test_user")
                .startupOptions(StartupOptions.initial())
                .deserializer(new StringDebeziumDeserializationSchema())
                .build();


        DataStreamSource<String> dataStreamSource = env.addSource(sourceFunction);
        dataStreamSource.print();
        env.execute(FlinkCDC.class.getSimpleName());
    }

}
