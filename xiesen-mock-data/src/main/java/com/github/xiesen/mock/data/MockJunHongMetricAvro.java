package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.text.NumberFormat;
import java.util.*;

/**
 * @author xiese
 * 模拟君弘指标 avro 数据
 */
public class MockJunHongMetricAvro {
    /**
     * 集中交易系统: jzjy*
     * 君弘: JHSystem*
     */
    public static final List<String> APP_SYSTEM_LIST = Arrays.asList("jzjy", "JHSystem");

    /**
     * 随机生成系统*
     *
     * @return String 系统简称
     */
    private static String getRandomAppSystem() {
        return APP_SYSTEM_LIST.get(new Random().nextInt(APP_SYSTEM_LIST.size()));
    }

    /**
     * 打印数据
     *
     * @param metricSetName metricSetName
     * @param timestamp     timestamp
     * @param dimensions    dimensions
     * @param metrics       metrics
     * @return String
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


    /**
     * 获取集中交易维度数据*
     *
     * @return Map<String, String>
     */
    private static Map<String, String> getJZJYDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>();

        dimensions.put("hostname", "zorkdata" + i);
        dimensions.put("ip", "192.168.1." + i);
        dimensions.put("clustername", "各核心kcbp群集");
        if (new Random().nextBoolean()) {
            dimensions.put("appprogramname", "核心1");
        } else {
            dimensions.put("appprogramname", "核心2");
        }

        dimensions.put("appsystem", "jzjy");
        return dimensions;
    }

    /**
     * 获取君弘系统维度数据*
     *
     * @return Map<String, String>
     */
    private static Map<String, String> getJHSystemDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>();

        dimensions.put("hostname", "zorkdata" + i);
        dimensions.put("ip", "192.168.1." + i);
        if (new Random().nextBoolean()) {
            dimensions.put("appprogramname", "交易中间件");
            dimensions.put("clustername", "博睿测速");
        } else {
            dimensions.put("appprogramname", "南方中心");
            dimensions.put("clustername", "交易中间件");
        }

        dimensions.put("appsystem", "JHSystem");
        dimensions.put("functionId", "00000-00001");
        dimensions.put("networkArea", "卡园1区");
        dimensions.put("operator", "张三");
        dimensions.put("operatorPhone", "13838383838");

        return dimensions;
    }


    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>();

        dimensions.put("hostname", "zorkdata" + i);
        dimensions.put("ip", "192.168.1." + i);
        dimensions.put("clustername", "192.168.1." + i);
        dimensions.put("appprogramname", "tc50");
        dimensions.put("appsystem", "tdx");

        return dimensions;
    }


    /**
     * 随机生成指标数据*
     *
     * @return Map<String, Double>
     */
    private static Map<String, Double> getRandomMetrics() {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("delay", getRandomDouble());
        metrics.put("Fra_used", getRandomDouble());
        metrics.put("Pool_free_mem", getRandomDouble());
        return metrics;
    }

    /**
     * 随机生成 double 数字,并保留2位小数*
     *
     * @return Double
     */
    private static Double getRandomDouble() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return Double.valueOf(nf.format(new Random().nextDouble()));
    }

    public static final List<String> METRIC_SET_NAMES = Arrays.asList("cpu", "tomcat_raw_zabbix", "nginx_raw_zabbix",
            "redis_raw_zabbix", "zookeeper_raw_zabbix", "mysql_raw_zabbix", "kafka_beat", "router_beat", "memory", "filesystem", "status_apache", "process_num");

    public static final List<String> JHSYSTEM_METRIC_SET_NAMES = Arrays.asList("jhapp_hq_zx", "jhapp_hq_init", "jhapp_hq_online",
            "jhapp_hq_refresh", "JhMsSql2005");

    private static String getJHRandomMetricSetName() {
        return JHSYSTEM_METRIC_SET_NAMES.get(new Random().nextInt(JHSYSTEM_METRIC_SET_NAMES.size()));
    }

    private static String getJZJYRandomMetricSetName() {
        return METRIC_SET_NAMES.get(new Random().nextInt(METRIC_SET_NAMES.size()));
    }

    public static void main(String[] args) throws Exception {
        String confPath = null;
        if (args.length == 1) {
            confPath = args[0];
        } else {
            System.exit(-1);
        }
        /// String propertiesName = "D:\\develop\\workspace\\xiesen-parent\\xiesen-mock-data\\src\\main\\resources\\config.properties";
        long size = 500000;
        for (int i = 0; i < size; i++) {
            String timestamp = DateUtil.getCurrentTimestamp();
            Map<String, Double> metrics = getRandomMetrics();

            CustomerProducer producer = ProducerPool.getInstance(confPath).getProducer();
            producer.sendMetric(getJZJYRandomMetricSetName(), timestamp, getJZJYDimensions(), metrics);
            producer.sendMetric(getJHRandomMetricSetName(), timestamp, getJHSystemDimensions(), metrics);
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }

}
