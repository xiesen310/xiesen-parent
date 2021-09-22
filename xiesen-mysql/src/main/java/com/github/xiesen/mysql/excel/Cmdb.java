package com.github.xiesen.mysql.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * bk_host_innerip: {}, os_name: {}, bk_biz_name: {}, bk_host_name: {}, bk_os_type: {}
 */
@AllArgsConstructor
@Data
public class Cmdb {
    private String bkHostInnerIp;
    private String osName;
    private String bkBizName;
    private String bkHostName;
    private String bkOsType;
}
