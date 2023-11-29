package com.github.xiesen.aviator;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.github.xiesen.aviator.func.DimensionFunction;
import com.github.xiesen.aviator.func.MapsFunction;
import com.googlecode.aviator.AviatorEvaluator;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiesen
 * @title: TestAviator
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/26 18:49
 */
public class TestAviator {
    public static Boolean metricCheck(String expression, Map<String, Object> data) {
        Boolean flag = (Boolean) AviatorEvaluator.execute(expression, data);
        return flag;
    }

    public static Map<String, Object> mockMetricData() {
        Map<String, Object> data = new HashMap<>();
        data.put("metricsetname", "streamx_metric_cpu1");
        Map<String, Double> metrics = new HashMap<>();

        metrics.put("cpu_usage_rate", 0.3);

        data.put("metrics", metrics);
        data.put("timestamp", System.currentTimeMillis() + "");

        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("appprogramname", "tomcat");
        dimensions.put("hostname", "zork_70.2.host.com");
        dimensions.put("ip", "192.168.70.2");
//        dimensions.put("appsystem", "streamx");
        data.put("dimensions", dimensions);
        return data;
    }

    public static Map<String, Object> mockPrometheusMetricData() {
        String s = "{\"metricsetname\":\"prometheus_prometheus_engine_query_duration_seconds_sum\",\"metrics\":{\"value\":5963.848968123006},\"timestamp\":\"1666860974000\",\"dimensions\":{\"instance\":\"192.168.1.92:9090\",\"slice\":\"inner_eval\",\"ip\":\"192.168.1.92\",\"appsystem\":\"prometheus\",\"assetsClassification\":\"source_data\",\"source\":\"prometheus\",\"job\":\"prometheus\"},\"tags\":{\"adapter_processing_time\":\"1666860981628\"}}";

        Map map = JSON.parseObject(s, Map.class);
        return map;
    }
    private static Long timestamp(String timestamp) {
        return new DateTime(timestamp).toDate().getTime();
    }

    public static void main(String[] args) {
        AviatorEvaluator.addFunction(new MapsFunction());
//        String expression = "masps('metricsetname')=='streamx_metric_cpu1' && dim('appsystem')=='streamx'";
//        String expression = "maps('metricsetname')=='streamx_metric_cpu1' && maps('dimensions.appsystem')=='streamx'";
        String expression = "metricsetname=='streamx_metric_cpu1' && dimensions.appsystem=='streamx'";
//        String expression = "maps('metrics.cpu_usage_rate')=='0.3'";
//        AviatorEvaluator.validate(expression);
        AviatorEvaluator.compile(expression);
        System.out.println(metricCheck(expression, mockMetricData()));

        System.out.println("===================");
//        String prometheus_anomaly_detection = "appsystem=='prometheus'&&metricSetName=='prometheus_prometheus_engine_query_duration_seconds'";
//        String prometheus_anomaly_detection = "metricsetname=='prometheus_prometheus_engine_query_duration_seconds_sum'";
//        String prometheus_anomaly_detection2 = "string.contains(str(metricsetname),'prometheus')";
//        System.out.println(metricCheck(prometheus_anomaly_detection2, mockPrometheusMetricData()));

//        private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");
//        String ts = "1700289870000";
        String ts = "2023-11-23T22:33:22.234";
        System.out.println(timestamp(ts));

        long timestamp = 1637250000234L; // 假设这是一个 13 位时间戳

        // 将 13 位时间戳格式化为指定格式的字符串
        String formattedString = DateUtil.format(new java.util.Date(timestamp), "yyyy-MM-dd'T'HH:mm:ss.SSS");

        System.out.println(formattedString);  // 输出格式化后的字符串

    }
}
