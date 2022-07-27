package com.github.xiesen.mock.data;

import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;
import org.mortbay.util.ajax.JSON;

import java.util.*;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockZcMetricAvroData {

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("hostname", "ostemplate");
        dimensions.put("appprogramname", "默认");
        dimensions.put("appsystem", "poctest");
        //dimensions.put("ip", "192.168.70.98");
        dimensions.put("ip", getRandomIp());
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(2);
        measures.put("count_zc", 1.0);
        return measures;
    }

    public static final List<String> METRIC_SETS = Arrays.asList("china_dimension_metric", "core_system_mb", "diskio_system_mb", "filesystem_system_mb", "load_system_mb");
    public static final List<String> IP_SETS = Arrays.asList("192.168.70.98", "192.168.70.99");

    private static String getRandomMetricSetName() {
        Random random = new Random();
        return METRIC_SETS.get(random.nextInt(METRIC_SETS.size()));
    }

    private static String getRandomIp() {
        Random random = new Random();
        return IP_SETS.get(random.nextInt(IP_SETS.size()));
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
            String metricSetName = "zhangmoumou";
            // String metricSetName = getRandomMetricSetName();
//            String timestamp = String.valueOf(System.currentTimeMillis());
            String timestamp = DateUtil.getUTCTimeStr();

            Map<String, String> dimensions = getRandomDimensions();
            Map<String, Double> metrics = getRandomMeasures();
            Map<String, Object> map = new HashMap<>();
            map.put("metricSetName", metricSetName);
            map.put("timestamp", timestamp);
            map.put("dimensions", dimensions);
            map.put("metrics", metrics);
            System.out.println(JSON.toString(map));
            Thread.sleep(new Random().nextInt(1000));
            CustomerProducer producer = ProducerPool.getInstance(confPath).getProducer();

            producer.sendMetric(metricSetName, timestamp, dimensions, metrics);

//            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
