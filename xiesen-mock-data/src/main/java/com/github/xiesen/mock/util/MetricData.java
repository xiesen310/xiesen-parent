package com.github.xiesen.mock.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xiesen
 * @title: MetricData
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/3/8 15:02
 */
@Data
@AllArgsConstructor
public class MetricData {
    private String key;
    private String metricsetname;
}
