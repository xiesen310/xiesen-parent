package com.github.xiesen.redis.pojo;

import lombok.Data;

import java.util.Map;

/**
 * @author xiesen
 */
@Data
public class MetricModel {
    private String instanceId;
    private String metricSetName;
    private Map<String, String> metrics;
    private Map<String, String> dimensions;
}
