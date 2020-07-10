package com.github.xiesen.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author xiese
 * @Description 日志集
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:44
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogZorkData {
    private String logTypeName;
    private String source;
    private String timestamp;
    private String offset;
    private Map<String, Double> measures;
    private Map<String, String> normalFields;
    private Map<String, String> dimensions;
}
