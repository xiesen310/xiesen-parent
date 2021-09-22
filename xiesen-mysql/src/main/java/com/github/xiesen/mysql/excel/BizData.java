package com.github.xiesen.mysql.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BizData {
    private String hostname;
    private String logTypeName;
    private String appprogramname;
    private String appsystem;
    private String topic;
}
