package com.github.xiesen.druid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author xiese
 * @Description Properties 文件读取工具类
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:41
 */
public class PropertiesUtil {
    /**
     * 根据文件名获取该properties对象
     *
     * @param propertieFileName
     * @return
     */
    public static Properties getProperties(String propertieFileName) throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
//            inputStream = PropertiesUtil.class.getResourceAsStream(propertieFileName);
            inputStream = new FileInputStream(new File(propertieFileName));
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            properties.load(inputStreamReader);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ex) {
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (Exception ex) {
                }
            }
        }
        return properties;
    }
}
