package com.github.xiesen.influxdb;

import lombok.Data;

import java.util.Map;

/**
 * @author 谢森
 * @since 2021/7/7
 */

@Data
public class InfluxDbRow {
    private String measurement;
    private Map<String, String> tags;
    Map<String, Object> fields;
    private Long timeSecond;
}
