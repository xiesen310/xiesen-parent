package com.github.xiesen.mock.data;

import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;
import org.mortbay.util.ajax.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockChinaDimensionMetricAvroData {

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("hostname", "zorkdata_1");
        dimensions.put("appprogramname", "通达信交易");
        dimensions.put("appsystem", "tdx");
        dimensions.put("ip", "192.168.1.1");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(2);
        measures.put("cpu_use", 1.0);
        measures.put("memory_use", 2.0);
        return measures;
    }


    public static void main(String[] args) throws Exception {
        long size = 100000L * 1;

        for (int i = 0; i < size; i++) {
            String metricSetName = "china_dimension_metric";
            String timestamp = String.valueOf(System.currentTimeMillis());

            Map<String, String> dimensions = getRandomDimensions();
            Map<String, Double> metrics = getRandomMeasures();
            Map<String, Object> map = new HashMap<>();
            map.put("metricSetName", metricSetName);
            map.put("timestamp", timestamp);
            map.put("dimensions", dimensions);
            map.put("metrics", metrics);
            System.out.println(JSON.toString(map));
            CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen\\xiesen-parent" +
                    "\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();

            producer.sendMetric(metricSetName, timestamp, dimensions, metrics);

//            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
