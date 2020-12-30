package com.github.xiesen.algorithm;

/**
 * @author 谢森
 * @Description test
 * @Email xiesen310@163.com
 * @Date 2020/12/18 19:21
 */
public class TestLog2EsId {
    public static void main(String[] args) {
        mockData();
    }

    /**
     * 3ac52fb6232c3e0d
     */
    private static void mockData() {
        String timestamp = "2020-12-24T10:36:01.000+08:00";
        String source = "/var/log/a.log";
        Long offset = 111L;
        String appSystem = "icubeserror";
        String logTypeName = "zork_error_data";
        String hostname = "zorkdata2";
        System.out.println(EsIdGenerate.xxHashLog2es(timestamp, source, offset, appSystem, logTypeName, hostname));
    }
}
