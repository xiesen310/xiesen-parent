package com.github.xiesen.junit.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author 谢森
 * @Description TestMetricData
 * @Email xiesen310@163.com
 * @Date 2021/1/20 9:34
 */
@Slf4j
public class TestMetricData {
    public static void main(String[] args) {
        readFileByLine("E:\\data\\metricbeat.log");
    }

    /**
     * 按行读取文件
     *
     * @param strFile
     */
    public static void readFileByLine(String strFile) {
        try {
            File file = new File(strFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine = null;
            int lineCount = 1;
            while (null != (strLine = bufferedReader.readLine())) {
                JSONObject jsonObject = JSONObject.parseObject(strLine);
                String ip = jsonObject.getString("ip");
                JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.getString("system"));

                boolean flag = jsonObject1.containsKey("filesystem");
                if (flag) {
                    JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1.getString("filesystem"));
                    String device_name = jsonObject2.getString("device_name");
                    String total = jsonObject2.getString("total");
                    if ("10.180.212.165".equalsIgnoreCase(ip) && "/dev/sda4".equalsIgnoreCase(device_name)) {
                        System.out.println("第[" + lineCount + "]行 total 数据:" + total);
                    }
                    // System.out.println("第[" + lineCount + "]行数据:" + strLine);
                }
//                log.info("第[" + lineCount + "]行数据:[" + strLine + "]");
                lineCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
