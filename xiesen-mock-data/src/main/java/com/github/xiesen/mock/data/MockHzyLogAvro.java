package com.github.xiesen.mock.data;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockHzyLogAvro {

    /**
     * "clustername": "集群",
     * "hostname": "zork-rd-dev-7092",
     * "appprogramname": "模块",
     * "appsystem": "poctest",
     * "servicename": "模块",
     * "ip": "192.168.70.92",
     * "servicecode": "模块"*
     *
     * @return
     */
    private static Map<String, String> getRandomDimensions() {
        Map<String, String> dimensions = new HashMap<>(8);
        dimensions.put("appprogramname", "组件");
        dimensions.put("clustername", "模块");
        dimensions.put("servicename", "组件");
        dimensions.put("servicecode", "组件");
        if (RandomUtil.randomBoolean()) {
            dimensions.put("appsystem", "dev");
            dimensions.put("hostname", "11");
            dimensions.put("ip", "192.168.2.68");
        } else {
            dimensions.put("appsystem", "dev");
            dimensions.put("hostname", "zork-rd-dev-7064");
            dimensions.put("ip", "192.168.70.64");
        }

        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        Random random = new Random();
        int i = random.nextInt(90) + 5;
        measures.put("lags", Double.valueOf(i));
//        measures.put("memory_used",0.9);
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("message", "183.95.248.189 - - [23/Jul/2020:08:26:32 +0800] \"GET " +
                "/gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfdHlwZSI6ImRhdGEifQ" +
                "%3D%3D HTTP/1.1\" 200 872 error ERROR Error");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        return normalFields;
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new RuntimeException("请填写配置文件路径");
        }

        long size = 100000000L * 1;
        for (int i = 0; i < size; i++) {
            String logTypeName = "default_analysis_template";
            String timestamp = DateUtil.getUTCTimeStr();
            String source = "/var/log/test/access.log";
            String offset = "351870827";

            Map<String, String> dimensions = getRandomDimensions();
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
            System.out.println(JSON.toJSONString(map));
            CustomerProducer producer = ProducerPool.getInstance(args[0]).getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);

            Thread.sleep(2000);
        }
        Thread.sleep(1000);
    }
}
