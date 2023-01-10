package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.common.utils.PropertiesUtil;
import com.github.xiesen.common.utils.StringUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟指标 avro 数据
 * @Email xiesen310@163.com
 * @Date 2020/6/28 10:05
 */
public class MockStreamMetricAvro {
    /**
     * 获取 log size
     *
     * @param propertiesName 配置文件名称
     * @return
     * @throws Exception
     */
    private static long getSize(String propertiesName) throws Exception {
        Properties properties = PropertiesUtil.getProperties(propertiesName);
        long logSize = StringUtil.getLong(properties.getProperty("log.size", "5000").trim(), 1);
        return logSize;
    }

    /**
     * 打印数据
     *
     * @param metricSetName
     * @param timestamp
     * @param dimensions
     * @param metrics
     * @return
     */
    public static String printData(String metricSetName, String timestamp,
                                   Map<String, String> dimensions, Map<String, Double> metrics) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("metricsetname", metricSetName);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("dimensions", dimensions);
        jsonObject.put("metrics", metrics);
        return jsonObject.toString();
    }

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>();

        dimensions.put("hostname", "zorkdata" + i);
        dimensions.put("ip", "192.168.1." + i);
        dimensions.put("appprogramname", "tc50");
        dimensions.put("appsystem", "tdx");

        return dimensions;
    }

    /**
     * dimensions": {
     * *     "appsystem": "dev_test",
     * *     "hostname": "DESKTOP-4I4I8U3",
     * *     "ruler_id": "2",
     * *     "source": "d:\\tmp\\filebeat\\a.log",
     * *     "ip": "192.168.70.170",
     * *     "monitor_name": "filebeat"
     * *   },
     *
     * @return
     */
    private static Map<String, String> getRandomDimensionsWithYm() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>();

        dimensions.put("hostname", "DESKTOP-4I4I8U3");
//        dimensions.put("ruler_id", "2");
//        dimensions.put("source", "D:\\opt\\smartdata-streamx\\a.log");
        dimensions.put("ip", "192.168.70.170");
//        dimensions.put("monitor_name", "filebeat");
        dimensions.put("appsystem", "dev_test");

        return dimensions;
    }

    private static Map<String, Double> getRandomMetrics() {
        Map<String, Double> metrics = new HashMap<>();
        DecimalFormat df = new DecimalFormat("######0.00");
        String format = df.format(new Random().nextDouble());
        metrics.put("cpu_usage_rate", Double.valueOf(format));
        metrics.put("cpu_usage_rate", Double.valueOf(System.currentTimeMillis()));


        return metrics;
    }

    /**
     * "metrics": {
     * *     "delay": 2.5851163E7,
     * *     "collect_time": 1.614800718404E12
     * *   }
     *
     * @return
     */
    private static Map<String, Double> getRandomMetricsWithYm() {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("delay", Double.valueOf(1111111));
        metrics.put("collect_time", Double.valueOf(System.currentTimeMillis()));
        return metrics;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("请指定配置文件");
            System.exit(-1);
        }
        String propertiesName = args[0];
        long size = getSize(propertiesName);

        for (int i = 0; i < size; i++) {
            /**
             * {
             *   "metricsetname": "log_original_time_flink1",
             *   "timestamp": "1614826569567",
             *   "dimensions": {
             *     "appsystem": "dev_test",
             *     "hostname": "DESKTOP-4I4I8U3",
             *     "ruler_id": "2",
             *     "source": "d:\\tmp\\filebeat\\a.log",
             *     "ip": "192.168.70.170",
             *     "monitor_name": "filebeat"
             *   },
             *   "metrics": {
             *     "delay": 2.5851163E7,
             *     "collect_time": 1.614800718404E12
             *   }
             * }
             */
            String metricSetName = "ym7";
            String timestamp = DateUtil.getCurrentTimestamp();
//            Map<String, String> dimensions = getRandomDimensions();
            Map<String, String> dimensions = getRandomDimensionsWithYm();
//            Map<String, Double> metrics = getRandomMetrics();
            Map<String, Double> metrics = getRandomMetricsWithYm();

            Thread.sleep(1000);
             System.out.println(printData(metricSetName, timestamp, dimensions, metrics));

            CustomerProducer producer = ProducerPool.getInstance(propertiesName).getProducer();
            producer.sendMetric(metricSetName, timestamp, dimensions, metrics);
            Thread.sleep(new Random().nextInt(5));
//            System.exit(-1);
        }
        Thread.sleep(1000);
    }

}
