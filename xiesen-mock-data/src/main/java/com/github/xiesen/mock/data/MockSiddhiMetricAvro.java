package com.github.xiesen.mock.data;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author xiese
 * 模拟君弘指标 avro 数据
 */
public class MockSiddhiMetricAvro {
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

    private static Map<String, Object> mocCpuMetric() {
        Map<String, Object> data = new HashMap<>();
        String metricSetName = "cpu_system_mb";
        String timestamp = DateUtil.getCurrentTimestamp();
        data.put("metricsetname", metricSetName);
        data.put("timestamp", timestamp);

        Map<String, String> dimensions = new HashMap<>();

        dimensions.put("hostname", "zorkdata");
        dimensions.put("ip", "192.168.1.1");
        dimensions.put("appsystem", "dev_test");
        dimensions.put("servicename", "servicename");
        dimensions.put("clustername", "clustername");
        dimensions.put("appprogramname", "appprogramname");

        data.put("dimensions", dimensions);
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("cores", 32.0);
        metrics.put("user_pct", 18.0);
        metrics.put("system_pct", 1.8);
        metrics.put("nice_pct", 0.8);
        metrics.put("idle_pct", 2.8);
        metrics.put("iowait_pct", 3.8);
        metrics.put("irq_pct", 4.8);
        metrics.put("softirq_pct", 5.8);
        metrics.put("steal_pct", 6.8);
        metrics.put("total_pct", 7.8);
        metrics.put("used_pct", 8.8);

        data.put("metrics", metrics);
        return data;
    }

    private static List<Map<String, String>> cacheListDim = new ArrayList<>();

    static {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("hostname", "JECGMAS23087");
        map1.put("ip", "10.180.230.87");
        map1.put("appsystem", "JHSystem");
        map1.put("servicename", "v2_kuorong");
        map1.put("clustername", "gmas");
        map1.put("appprogramname", "v2_kuorong");

        HashMap<String, String> map2 = new HashMap<>();
        map2.put("hostname", "es119");
        map2.put("ip", "10.180.226.119");
        map2.put("appsystem", "JHSystem");
        map2.put("servicename", "es");
        map2.put("clustername", "gmas");
        map2.put("appprogramname", "es");

        HashMap<String, String> map3 = new HashMap<>();
        map3.put("hostname", "GMASV1-150");
        map3.put("ip", "10.180.231.150");
        map3.put("appsystem", "JHSystem");
        map3.put("servicename", "v1_kuorong");
        map3.put("clustername", "gmas");
        map3.put("appprogramname", "v1_kuorong");

        HashMap<String, String> map4 = new HashMap<>();
        map4.put("hostname", "jh-bf-437");
        map4.put("ip", "10.164.19.42");
        map4.put("appsystem", "JHSystem");
        map4.put("servicename", "北方");
        map4.put("clustername", "交易中间件");
        map4.put("appprogramname", "北方");

        HashMap<String, String> map5 = new HashMap<>();
        map5.put("hostname", "*yyz_jy_226_9");
        map5.put("ip", "10.180.226.9");
        map5.put("appsystem", "JHSystem");
        map5.put("servicename", "卡园");
        map5.put("clustername", "交易中间件");
        map5.put("appprogramname", "卡园");

        HashMap<String, String> map6 = new HashMap<>();
        map6.put("hostname", "*yyz_jy_224_116");
        map6.put("ip", "10.180.224.116");
        map6.put("appsystem", "JHSystem");
        map6.put("servicename", "卡园");
        map6.put("clustername", "交易中间件");
        map6.put("appprogramname", "卡园");


        HashMap<String, String> map7 = new HashMap<>();
        map7.put("hostname", "jzc1-kcbp6");
        map7.put("ip", "10.180.1.21");
        map7.put("appsystem", "jzjy");
        map7.put("servicename", "BP");
        map7.put("clustername", "核心1");
        map7.put("appprogramname", "BP");

        HashMap<String, String> map8 = new HashMap<>();
        map8.put("hostname", "jzc1-kcbp4");
        map8.put("ip", "10.180.1.14");
        map8.put("appsystem", "jzjy");
        map8.put("servicename", "BP");
        map8.put("clustername", "核心1");
        map8.put("appprogramname", "BP");

        HashMap<String, String> map9 = new HashMap<>();
        map9.put("hostname", "jzc2-shof");
        map9.put("ip", "10.180.2.31");
        map9.put("appsystem", "jzjy");
        map9.put("servicename", "OF");
        map9.put("clustername", "核心2");
        map9.put("appprogramname", "OF");

        HashMap<String, String> map10 = new HashMap<>();
        map10.put("hostname", "jzc2-szof");
        map10.put("ip", "10.180.2.32");
        map10.put("appsystem", "jzjy");
        map10.put("servicename", "OF");
        map10.put("clustername", "核心2");
        map10.put("appprogramname", "OF");

        cacheListDim.add(map1);
        cacheListDim.add(map2);
        cacheListDim.add(map3);
        cacheListDim.add(map4);
        cacheListDim.add(map5);
        cacheListDim.add(map6);
        cacheListDim.add(map7);
        cacheListDim.add(map8);
        cacheListDim.add(map9);
        cacheListDim.add(map10);

    }

    /**
     * String metricSetName = "memory_system_mb";*
     *
     * @param metricSetName
     * @return
     */
    private static Map<String, Object> mockMemoryMetric(String metricSetName, Map<String, String> dimensions) {
        Map<String, Object> data = new HashMap<>();
        String timestamp = DateUtil.getCurrentTimestamp();
        data.put("metricsetname", metricSetName);
        data.put("timestamp", timestamp);

        /*dimensions.put("hostname", "zorkdata_" + i);
        dimensions.put("ip", "192.168.1." + i);
        dimensions.put("appsystem", "dev_test");
        dimensions.put("servicename", "servicename");
        dimensions.put("clustername", "clustername");
        dimensions.put("appprogramname", "appprogramname");*/

        data.put("dimensions", dimensions);
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("total", randomDouble(1, 10));
        metrics.put("used_bytes", randomDouble(10, 20));
        metrics.put("free", randomDouble(3, 5));
        metrics.put("used_pct", randomDouble(1, 5));
        metrics.put("actual_used_bytes", randomDouble(1, 10));
        metrics.put("actual_free", randomDouble(1, 10));
        metrics.put("actual_used_pct", randomDouble(1, 10));
        metrics.put("swap_total", randomDouble(1, 10));
        metrics.put("swap_used_bytes", randomDouble(1, 10));
        metrics.put("swap_free", randomDouble(1, 10));
        metrics.put("swap_used_pct", randomDouble(1, 10));

        data.put("metrics", metrics);
        return data;
    }

    private static Double randomDouble(int start, int end) {
        final int[] values = NumberUtil.generateRandomNumber(start, end, 1);
        return (double) values[0];
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

    //    public static final List<String> METRIC_SET_NAMES = Arrays.asList("cpu", "tomcat_raw_zabbix", "nginx_raw_zabbix",
//            "redis_raw_zabbix", "zookeeper_raw_zabbix", "mysql_raw_zabbix", "kafka_beat", "router_beat", "memory", "filesystem", "status_apache", "process_num");
    public static final List<String> METRIC_SET_NAMES = Arrays.asList("cpu", "cpu_system_mb", "memory_system_mb", "filesystem", "process_num");

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
        long size = 50000000;
        String prefix = "memory_system_mb";
        for (int i = 0; i < size; i++) {
            CustomerProducer producer = ProducerPool.getInstance(confPath).getProducer();
            for (int j = 0; j < cacheListDim.size(); j++) {
                producer.sendMetric(mockMemoryMetric(prefix, cacheListDim.get(j)));
            }
            Thread.sleep(50);
        }
        Thread.sleep(1000);
    }


    private static void initDim() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection com = DriverManager.getConnection("jdbc:mysql://192.168.1.222:3306/cmdb?serverTimezone=PRC&useUnicode=true&characterEncoding=utf8", "root", "Mysql@123");
        // DriverManager 注册驱动
        // Connection 数据库连接对象  url（指定连接的路径 语法：“jdbc:mysql://ip地址:端口号/数据库名称”）
        Statement stat = com.createStatement();
        //执行 sql 语句的对象
        String sql = "SELECT YWJC,MKMC,ZUJIANMC,ZJMC,IP FROM Topo WHERE YWJC = 'JHsystem' AND MKMC != '' AND ZUJIANMC != ''";
        ResultSet rs = stat.executeQuery(sql);
        // 执行 增删改查 （DML）语句用 int executeUpdate(Sting sql);
        // 执行 DQL 语句 ResultSet executeQuery(String sql);
        // 对象释放 void close();
        while (rs.next()) {
            Console.log("业务: {}, 集群: {}, 模块: {}, 主机名: {}, ip: {}", rs.getString("YWJC"), rs.getString("MKMC"),
                    rs.getString("ZUJIANMC"), rs.getString("ZJMC"), rs.getString("IP"));
        }

        com.close();
        stat.close();
        com.close();


    }

}
