package com.github.xiesen.influxdb.datax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiesen
 * @title: DataxContent
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/21 19:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataxContent {
    private DataxReader reader;
    private DataxWriter writer;
}
