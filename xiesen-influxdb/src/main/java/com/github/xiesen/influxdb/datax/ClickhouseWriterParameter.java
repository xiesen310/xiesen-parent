package com.github.xiesen.influxdb.datax;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author xiesen
 * @title: ClickhouseWriterParameter
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/21 19:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClickhouseWriterParameter {
    private String username;
    private String password;
    private Set<String> column;
    private List<JSONObject> connection;
    private List<String> preSql;
}
