package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.util.*;

/**
 * {"metricsetname":"prometheus_node_forks_total","metrics":{"value":4.4182622E7},"timestamp":"1666013436000","dimensions":{"appsystem":"prometheus","assetsClassification":"source_data","instance":"192.168.90.25:9100","source":"prometheus","job":"node_exporter","ip":"192.168.90.25"},"tags":{"adapter_processing_time":"1666013421699"}}
 *
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockPrometheusAvroData {

    private static Map<String, String> getRandomDimensions() {
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("appsystem", "prometheus");
        dimensions.put("assetsClassification", "source_data");
        dimensions.put("instance", "192.168.90.25:9100");
        dimensions.put("source", "prometheus");
        dimensions.put("job", "node_exporter");
        dimensions.put("ip", "192.168.90.25");
        return dimensions;
    }

    private static Map<String, String> getTags() {
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("adapter_processing_time", System.currentTimeMillis() + "");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(2);
        measures.put("value", 1.0);
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
            String metricSetName = "prometheus_node_forks_total";
            String timestamp = String.valueOf(System.currentTimeMillis());

            Map<String, String> dimensions = getRandomDimensions();
            Map<String, Double> metrics = getRandomMeasures();
            Map<String, Object> map = new HashMap<>();
            map.put("metricSetName", metricSetName);
            map.put("timestamp", timestamp);
            map.put("dimensions", dimensions);
            map.put("metrics", metrics);
            map.put("tags", getTags());

//            Thread.sleep(new Random().nextInt(1000));
            CustomerProducer producer = ProducerPool.getInstance(confPath).getProducer();

            producer.sendMetric(metricSetName, timestamp, dimensions, metrics);
            System.out.println(JSON.toJSONString(map));

            Thread.sleep(10000);
        }
        Thread.sleep(1000);
    }
}
