package com.github.xiesen.influxdb.datax;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiesen
 * @title: DataxJob
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/21 18:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataxJob {
    private DataxSetting setting;
    private List<DataxContent> content;
}