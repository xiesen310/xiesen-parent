package com.github.xiesen.mock.data;

import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockHqLogAvro {

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");

    private static Map<String, String> getErrorDimensionsWithNoAppSystem() {
        Map<String, String> dimensions = new HashMap<>(4);
        dimensions.put("appsystem", "icubeserror");
        dimensions.put("originalSystem", "dev_test");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(4);
        return measures;
    }

    private static Map<String, String> getErrorNormalFieldsNoAppSystem() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("originalLog", "{\\\"agent\\\":{\\\"hostname\\\":\\\"yf120\\\"," +
                "\\\"id\\\":\\\"59ecdfa2-5be4-4021-9653-c1124aecb7d7\\\"," +
                "\\\"ephemeral_id\\\":\\\"e955e2aa-4627-400e-a51c-2abbb7367e41\\\",\\\"type\\\":\\\"filebeat\\\"," +
                "\\\"version\\\":\\\"7.4.0\\\"},\\\"clustername\\\":\\\"基础监控\\\"," +
                "\\\"log\\\":{\\\"file\\\":{\\\"path\\\":\\\"/var/log/monit.log\\\"},\\\"offset\\\":938284}," +
                "\\\"ip\\\":\\\"192.168.70.120\\\",\\\"topicname\\\":\\\"ods_default_log\\\",\\\"transip\\\":\\\"192" +
                ".168.70.120\\\",\\\"message\\\":\\\"[CST Dec 14 13:31:00] error    : Alert handler failed, retry " +
                "scheduled for next cycle\\\",\\\"tags\\\":[\\\"_dateparsefailure\\\"]," +
                "\\\"input\\\":{\\\"type\\\":\\\"log\\\"},\\\"transtime\\\":\\\"2021-03-23T14:09:45.135+08:00\\\"," +
                "\\\"appprogramname\\\":\\\"lmt模块\\\",\\\"collecttime\\\":\\\"2021-03-23T14:09:45.135+08:00\\\"," +
                "\\\"@timestamp\\\":\\\"2021-03-23 09:49:35,141\\\",\\\"servicecode\\\":\\\"lmt模块\\\"," +
                "\\\"ecs\\\":{\\\"version\\\":\\\"1.1.0\\\"},\\\"host\\\":{\\\"name\\\":\\\"yf120\\\"}," +
                "\\\"@version\\\":\\\"1\\\",\\\"appsystem\\\":\\\"dev_test\\\",\\\"servicename\\\":\\\"lmt模块\\\"," +
                "\\\"collectruleid\\\":2,\\\"offset\\\":0,\\\"source\\\":\\\"/rancher/docker_log\\\"," +
                "\\\"logTypeName\\\":\\\"zork_error_data\\\",\\\"timestamp\\\":\\\"2021-03-23T14:09:49.179+08:00\\\"}");
        normalFields.put("originalType", "rancher_default_fluent");
        normalFields.put("failReason", "kafka-parse-es7任务解析失败,维度为空: {\\\"agent\\\":{\\\"hostname\\\":\\\"yf120\\\"," +
                "\\\"id\\\":\\\"59ecdfa2-5be4-4021-9653-c1124aecb7d7\\\"," +
                "\\\"ephemeral_id\\\":\\\"e955e2aa-4627-400e-a51c-2abbb7367e41\\\",\\\"type\\\":\\\"filebeat\\\"," +
                "\\\"version\\\":\\\"7.4.0\\\"},\\\"clustername\\\":\\\"基础监控\\\"," +
                "\\\"log\\\":{\\\"file\\\":{\\\"path\\\":\\\"/var/log/monit.log\\\"},\\\"offset\\\":938284}," +
                "\\\"ip\\\":\\\"192.168.70.120\\\",\\\"topicname\\\":\\\"ods_default_log\\\",\\\"transip\\\":\\\"192" +
                ".168.70.120\\\",\\\"message\\\":\\\"[CST Dec 14 13:31:00] error    : Alert handler failed, retry " +
                "scheduled for next cycle\\\",\\\"tags\\\":[\\\"_dateparsefailure\\\"]," +
                "\\\"input\\\":{\\\"type\\\":\\\"log\\\"},\\\"transtime\\\":\\\"2021-03-23T14:09:45.135+08:00\\\"," +
                "\\\"appprogramname\\\":\\\"lmt模块\\\",\\\"collecttime\\\":\\\"2021-03-23T14:09:45.135+08:00\\\"," +
                "\\\"@timestamp\\\":\\\"2021-03-23 09:49:35,141\\\",\\\"servicecode\\\":\\\"lmt模块\\\"," +
                "\\\"ecs\\\":{\\\"version\\\":\\\"1.1.0\\\"},\\\"host\\\":{\\\"name\\\":\\\"yf120\\\"}," +
                "\\\"@version\\\":\\\"1\\\",\\\"appsystem\\\":\\\"dev_test\\\",\\\"servicename\\\":\\\"lmt模块\\\"," +
                "\\\"collectruleid\\\":2,\\\"offset\\\":0,\\\"source\\\":\\\"/rancher/docker_log\\\"," +
                "\\\"logTypeName\\\":\\\"rancher_default_fluent\\\"}");
        normalFields.put("failappsystem", "dev_test");
        return normalFields;
    }

    public static void main(String[] args) throws Exception {
        long size = 1000L * 1;
        for (int i = 0; i < size; i++) {
            String logTypeName = "zork_error_data";
            String timestamp = format.format(new Date()).toString();
            String source = "/var/log/nginx/access.log";
            String offset = String.valueOf(new Random().nextInt(100000));

            Map<String, String> dimensions = getErrorDimensionsWithNoAppSystem();

            Map<String, Double> measures = getRandomMeasures();
            Map<String, String> normalFields = getErrorNormalFieldsNoAppSystem();

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("logTypeName", logTypeName);
            map.put("timestamp", timestamp);
            map.put("source", source);
            map.put("offset", offset);
            map.put("dimensions", dimensions);
            map.put("measures", measures);
            map.put("normalFields", normalFields);
            CustomerProducer producer = ProducerPool.getInstance("/Users/xiesen/workspaces/xiesen-parent/xiesen-mock-data/src/main/resources/config.properties").getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);

            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
