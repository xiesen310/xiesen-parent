package com.github.xiesen.mock.data;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Encoder;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.mock.util.BatchCommon;
import com.github.xiesen.mock.util.CkUtils;
import com.github.xiesen.mock.util.DateTimeUtils;
import com.github.xiesen.mock.util.MetricData;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseStatement;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author 谢森
 * @Description ReadMetricFile2Kafka
 * @Email xiesen310@163.com
 * @Date 2021/1/20 9:49
 */
public class ReadJsonFile2ClickhouseMetric {
    private static String url = "jdbc:clickhouse://192.168.90.25:8123";
    private static String database = "xiesen";
    private static String username = "default";
    private static String password = "admin";

    private static BatchCommon<BlockingQueue<MetricData>> blockingQueueBatchCommon = null;

    public static void main(String[] args) throws SQLException {
        String filePath = "D:\\tmp\\metricData\\m2logtest";
        ClickHouseConnection distributedConnect = CkUtils.getDistributedConnect(url, database, username, password);
        String sql = "insert into xiesen.metric values (?,?)";
        PreparedStatement preparedStatement = distributedConnect.prepareStatement(sql);
        ReadJsonFile2ClickhouseMetric readJsonFile2ClickhouseMetric = new ReadJsonFile2ClickhouseMetric();
        Consumer<List<BlockingQueue<MetricData>>> consumer = list -> {
            int size = list.size();
            if (size > 0) {
                for (BlockingQueue<MetricData> alarmSets : list) {
                    try {
                        readJsonFile2ClickhouseMetric.batchWrite(alarmSets, preparedStatement);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        blockingQueueBatchCommon = new BatchCommon<>(1000, 2000, consumer);

        try {
            File dir = new File(filePath);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                    readJsonFile2ClickhouseMetric.write2ck(bufferedReader);

                    bufferedReader.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void batchWrite(BlockingQueue<MetricData> alarmRecordQueue, PreparedStatement preparedStatement) throws InterruptedException, SQLException {
        int size = alarmRecordQueue.size();
        for (int i = 0; i < size; i++) {
            MetricData metricData = alarmRecordQueue.poll(3, TimeUnit.SECONDS);
//            preparedStatement.setString(1, metricData.getKey());
//            preparedStatement.setString(2, metricData.getMetricsetname());
            preparedStatement.addBatch();
        }
        execute(preparedStatement, 2);
    }

    private void execute(PreparedStatement statement, Integer retry) {
        try {
            int[] batch = statement.executeBatch();
            if (batch.length >= 0) {
                statement.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (retry > 0) {
                execute(statement, retry - 1);
            } else {
                if (null != statement) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private BlockingQueue<MetricData> write2ck(BufferedReader bufferedReader) throws IOException, InterruptedException {
        String strLine;
        BlockingQueue<MetricData> metricDataRecordQueue = new LinkedBlockingQueue<>();
        while (null != (strLine = bufferedReader.readLine())) {
            JSONObject jsonObject = JSONObject.parseObject(strLine);
            String metricsetname = jsonObject.getString("metricsetname");
            String timestamp = jsonObject.getString("timestamp");
            String dimensionStr = jsonObject.getString("dimensions");
            JSONObject jsonObject1 = JSONObject.parseObject(dimensionStr);
            Map<String, String> dimensions = jsonObject1.toJavaObject(Map.class);
            StringBuilder builder = new StringBuilder();
            dimensions.forEach((k, v) -> {
                builder.append(v).append("_");
            });
            String dimension = builder.toString();
            String key = metricsetname + "-" + timestamp + "-" + dimension;
//            MetricData metricData = new MetricData(key, metricsetname);
            if ("process_number_count_5".equals(metricsetname)) {
                System.out.println(strLine);
            }
//            metricDataRecordQueue.put(metricData);
        }
//        blockingQueueBatchCommon.add(metricDataRecordQueue);

        return metricDataRecordQueue;
    }


}
