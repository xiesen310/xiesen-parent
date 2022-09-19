package com.github.xiesen.influxdb.datax;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiesen
 * @title: DataxReader
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/21 19:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataxWriter {
    private String name;
    private Object parameter;
}
