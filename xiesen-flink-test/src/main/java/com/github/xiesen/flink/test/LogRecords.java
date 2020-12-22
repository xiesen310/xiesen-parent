package com.github.xiesen.flink.test;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author 谢森
 * @Description 日志记录
 * @Email xiesen310@163.com
 * @Date 2020/12/21 21:49
 */
@Data
public class LogRecords {
    private String message;
    private Long offset;
    private String source;

    @JSONField(name = "@timestamp")
    private String collectTime;
    private Map<String, Object> fields;
    private Map<String, Object> host;
}
