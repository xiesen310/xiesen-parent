package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.common.utils.PropertiesUtil;
import com.github.xiesen.common.utils.StringUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/6/28 10:03
 */
public class MockStreamLogAvro {

    /**
     * 获取 日志数据 size
     *
     * @param propertiesName 配置文件名称
     * @return
     * @throws Exception
     */
    private static long getSize(String propertiesName) throws Exception {
        Properties properties = PropertiesUtil.getProperties(propertiesName);
        long logSize = StringUtil.getLong(properties.getProperty("log.size", "5000").trim(), 1);
        return logSize;
    }

    /**
     * 打印日志数据
     *
     * @param logTypeName
     * @param timestamp
     * @param source
     * @param offset
     * @param dimensions
     * @param metrics
     * @param normalFields
     * @return
     */
    public static String printData(String logTypeName, String timestamp, String source, String offset,
                                   Map<String, String> dimensions, Map<String, Double> metrics, Map<String, String> normalFields) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logTypeName", logTypeName);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("source", source);
        jsonObject.put("offset", offset);
        jsonObject.put("dimensions", dimensions);
        jsonObject.put("measures", metrics);
        jsonObject.put("normalFields", normalFields);
        return jsonObject.toString();
    }

    private static String getRandomOffset() {
        Random random = new Random();
        long l = random.nextInt(10000);
        return String.valueOf(l);
    }

    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        int i = random.nextInt(10);
        Map<String, String> dimensions = new HashMap<>();

        dimensions.put("hostname", "zorkdata" + i);
        dimensions.put("ip", "192.168.1." + i);
        dimensions.put("appprogramname", "tc50");
        dimensions.put("appsystem", "tdx");

        return dimensions;
    }

    private static String[] codes = {
            "AO", "AF", "AL", "DZ", "AD", "AI", "AG", "AR", "AM", "AU",
            "AT", "AZ", "BS", "BH", "BD", "BB", "BY", "BE", "BZ", "BJ"
    };

    public static String getRandomCountryCode() {
        Random random = new Random();
        return codes[random.nextInt(codes.length)];
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>();
        normalFields.put("message", "data update success");
        normalFields.put("countryCode", getRandomCountryCode());
        return normalFields;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("请指定配置文件");
            System.exit(-1);
        }
        String propertiesName = args[0];
        long size = getSize(propertiesName);

        for (int i = 0; i < size; i++) {
            String logTypeName = "streamx_log_avro";
            String timestamp = DateUtil.getUTCTimeStr();
            String source = "/var/log/" + DateUtil.getDate() + ".log";
            String offset = getRandomOffset();
            Map<String, String> dimensions = getRandomDimensions();
            Map<String, Double> measures = new HashMap<>();


            Map<String, String> normalFields = getRandomNormalFields();

            System.out.println(printData(logTypeName, timestamp, source, offset, dimensions, measures, normalFields));
//            CustomerProducer producer = ProducerPool.getInstance(propertiesName).getProducer();
//            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);
            Thread.sleep(2000);
        }
        Thread.sleep(1000);
    }
}
