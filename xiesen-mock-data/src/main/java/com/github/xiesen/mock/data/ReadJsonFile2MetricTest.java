package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.mock.util.MetricData;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 谢森
 * @Description ReadMetricFile2Kafka
 * @Email xiesen310@163.com
 * @Date 2021/1/20 9:49
 */
public class ReadJsonFile2MetricTest {
    private static String url = "jdbc:clickhouse://192.168.90.25:8123";
    private static String database = "xiesen";
    private static String username = "default";
    private static String password = "admin";


    public static void main(String[] args) throws SQLException {
        String filePath = "D:\\tmp\\metricData\\m2logtest";
        ReadJsonFile2MetricTest readJsonFile2ClickhouseMetric = new ReadJsonFile2MetricTest();
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

            if ("lv2Net".equals(metricsetname)) {
                System.out.println(strLine);
            }
        }

        return metricDataRecordQueue;
    }


}
