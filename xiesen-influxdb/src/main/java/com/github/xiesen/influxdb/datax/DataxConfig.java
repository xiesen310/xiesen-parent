package com.github.xiesen.influxdb.datax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiesen
 * @title: DataxConfig
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/21 22:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataxConfig {
    private DataxJob job;
}
