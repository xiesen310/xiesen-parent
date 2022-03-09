package com.github.xiesen.mock.util;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * @author xiesen
 * @title: CustomerCallBack
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/3/9 16:31
 */
public class CustomerMetricCallBack implements Callback {
    public static final Logger logger = LoggerFactory.getLogger(CustomerMetricCallBack.class);
    private String topic;
    private String metricSetName;
    private String timestamp;
    private Map<String, String> dimensions;
    private Map<String, Double> metrics;

    public CustomerMetricCallBack(String topic, String metricSetName, String timestamp,
                                  Map<String, String> dimensions, Map<String, Double> metrics) {
        this.topic = topic;
        this.metricSetName = metricSetName;
        this.timestamp = timestamp;
        this.dimensions = dimensions;
        this.metrics = metrics;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (metadata == null) {
            logger.error("向 kafka topic({}) 中发送指标消息失败,失败数据: metricSetName={},timestamp={},dimensions={},metrics={}",
                    topic, metricSetName, timestamp, dimensions, metrics);
            exception.printStackTrace();
        }
    }
}
