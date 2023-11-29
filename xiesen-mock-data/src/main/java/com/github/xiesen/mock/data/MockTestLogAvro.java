package com.github.xiesen.mock.data;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.common.utils.PropertiesUtil;
import com.github.xiesen.common.utils.StringUtil;
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
public class MockTestLogAvro {

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        Map<String, String> dimensions = new HashMap<>(4);
//        dimensions.put("hostname", "DVJTY4-WEB406-zzg");
//        dimensions.put("appprogramname", "DVJTY4-WEB406_80");
//        dimensions.put("servicecode", "WEB");
//        dimensions.put("clustername", "nginx");
        dimensions.put("appsystem", "tdx");
//        dimensions.put("servicename", "nginx");
        dimensions.put("ip", "192.168.1.1");

        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        final int num = new Random().nextInt(10);
        normalFields.put("message", "code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ[123]: IsActive " + num);
//        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
//        String kv = new Random().nextBoolean() ? "": "a";
//        normalFields.put("kv",kv);
        return normalFields;
    }

    private static long getSize(String propertiesName) throws Exception {
        Properties properties = PropertiesUtil.getProperties(propertiesName);
        long logSize = StringUtil.getLong(properties.getProperty("log.size", "5000").trim(), 1);
        return logSize;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("请指定配置文件");
            System.exit(-1);
        }
        String propertiesName = args[0];
        long size = getSize(propertiesName);
        System.out.println("配置文件：" + propertiesName);
        System.out.println("生成数据条数：" + size);
        String topicName = "xiesen_ods_default_log";
        StopWatch sw = new StopWatch();
        sw.start();
        Console.log("生成聚合数据任务开始: {}", cn.hutool.core.date.DateUtil.format(cn.hutool.core.date.DateUtil.date(), "yyyy-MM-dd HH:mm:ss"));
        for (int i = 0; i < size; i++) {
            String logTypeName = "default_analysis_template";
            String timestamp = DateUtil.getUTCTimeStr();
            String source = "/var/log/nginx/access.log";
            String offset = "351870827";

            Map<String, String> dimensions = getRandomDimensions();
            dimensions.put("uuid", UUID.randomUUID().toString());
            Map<String, Double> measures = getRandomMeasures();
            Map<String, String> normalFields = getRandomNormalFields();
            Map<String, Object> map = new HashMap<>();
            map.put("logTypeName", logTypeName);
            map.put("timestamp", timestamp);
            map.put("source", source);
            map.put("offset", offset);
            map.put("dimensions", dimensions);
            map.put("measures", measures);
            map.put("normalFields", normalFields);
//            System.out.println(JSON.toString(map));
            CustomerProducer producer = ProducerPool.getInstance(propertiesName).getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);
//            Thread.sleep(10);
        }
        sw.stop();
        Console.log("每秒产生 {} 条数据", 1000 * Math.ceil(size / sw.getTotalTimeMillis()));
        Console.log("生成聚合数据任务结束: {},总耗时: {}ms,一共生成了 {} 条数据.",
                cn.hutool.core.date.DateUtil.format(cn.hutool.core.date.DateUtil.date(), "yyyy-MM-dd HH:mm:ss"),
                sw.getTotalTimeMillis(), size);
        Thread.sleep(1000);
    }
}
