package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.util.*;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockChinaDimensionMetricAvroData {

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        int i = random.nextInt(225);
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("hostname", "zorkdata_" + i);
        dimensions.put("appprogramname", "通达信交易");
        dimensions.put("appsystem", "tdx");
        dimensions.put("ip", "192.168.1." + i);
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(2);
        measures.put("cpu_use", 1.0);
        measures.put("memory_use", 2.0);
        return measures;
    }

    public static final List<String> METRIC_SETS = Arrays.asList("china_dimension_metric", "core_system_mb", "diskio_system_mb", "filesystem_system_mb", "load_system_mb");

    private static String getRandomMetricSetName() {
        Random random = new Random();
        return METRIC_SETS.get(random.nextInt(METRIC_SETS.size()));
    }

    public static void main(String[] args) throws Exception {
        String confPath = null;
        if (args.length == 1) {
            confPath = args[0];
        } else {
            System.exit(-1);
        }

        long size = 100000L * 1;

        for (int i = 0; i < size; i++) {
            String metricSetName = getRandomMetricSetName();
            String timestamp = String.valueOf(System.currentTimeMillis());

            Map<String, String> dimensions = getRandomDimensions();
            Map<String, Double> metrics = getRandomMeasures();
            Map<String, Object> map = new HashMap<>();
            map.put("metricSetName", metricSetName);
            map.put("timestamp", timestamp);
            map.put("dimensions", dimensions);
            map.put("metrics", metrics);
            System.out.println(JSON.toJSONString(map));
            Thread.sleep(new Random().nextInt(1000));
            CustomerProducer producer = ProducerPool.getInstance(confPath).getProducer();

            producer.sendMetric(metricSetName, timestamp, dimensions, metrics);

//            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
