package com.github.xiesen.influxdb.datax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author xiesen
 * @title: InfluxdbReaderParameter
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/21 19:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfluxdbReaderParameter {
    private String endpoint;
    private String username;
    private String password;
    private String database;
    private String measurement;
    private Set<String> column;
    private Long splitIntervalS;
    private String beginDateTime;
    private String endDateTime;
}
