package com.github.xiesen.mock.data;

import cn.hutool.core.util.StrUtil;
import com.github.xiesen.common.utils.DateUtil;
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
public class MockSplitLogAvro {

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("hostname", "DVJTY4-WEB406-zzg");
        dimensions.put("appprogramname", "DVJTY4-WEB406_80");
        dimensions.put("clustername", "nginx");
        dimensions.put("appsystem", "tdx");
        dimensions.put("ip", "192.168.1.1");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("message", "17:11\t青云-北京4-139.198.9.231\t139.198.9.231\t8114\t110\t401\t5000\t21897\t\t3.03 build 2020.1126 Linux64");
        return normalFields;
    }


    public static void main(String[] args) throws Exception {
        String msg = "17:11\t青云-北京4-139.198.9.231\t139.198.9.231\t8114\t110\t401\t5000\t21897\t\t3.03 build 2020.1126 Linux64";
        final String[] strings = StrUtil.splitToArray(msg, "\t");
        System.out.println("name: " + strings[1]);
        System.out.println("data_ip: " + strings[2]);
        System.out.println("port: " + strings[3]);
        System.out.println("currconnect: " + strings[4]);
        System.out.println("peakconnect: " + strings[5]);
        System.out.println("maxconnect: " + strings[6]);
        System.out.println("totalconnect: " + strings[7]);
        long size = 1000000L * 1;
        String topicName = "dwd_test_split";
        for (int i = 0; i < size; i++) {
            String logTypeName = "tdx_onlineuser_num";
            String timestamp = DateUtil.getUTCTimeStr();
            String source = "/var/log/access.log";
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
            System.out.println(JSON.toString(map));
            CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen-parent\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);

            Thread.sleep(2000);
        }
        Thread.sleep(1000);
    }
}
