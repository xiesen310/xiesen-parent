package com.github.xiesen.hive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * load yarn conf from specify dir
 *
 * @author 谢森
 */

public class YarnConfLoader {
    private static final String XML_SUFFIX = ".xml";

    /**
     * 根据 yarn 配置文件目录解析yarn 配置文件
     *
     * @param yarnConfDir yarn 配置文件目录
     * @return YarnConfiguration
     */
    public static YarnConfiguration getYarnConf(String yarnConfDir) {
        YarnConfiguration yarnConf = new YarnConfiguration();
        try {

            File dir = new File(yarnConfDir);
            if (dir.exists() && dir.isDirectory()) {

                File[] xmlFileList = new File(yarnConfDir).listFiles((dir1, name) -> {
                    return name.endsWith(XML_SUFFIX);
                });

                if (xmlFileList != null) {
                    for (File xmlFile : xmlFileList) {
                        yarnConf.addResource(xmlFile.toURI().toURL());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        haYarnConf(yarnConf);
        return yarnConf;
    }

    /**
     * deal yarn HA conf
     */
    private static Configuration haYarnConf(Configuration yarnConf) {
        Iterator<Map.Entry<String, String>> iterator = yarnConf.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.startsWith("yarn.resourcemanager.hostname.")) {
                String rm = key.substring("yarn.resourcemanager.hostname.".length());
                String addressKey = "yarn.resourcemanager.address." + rm;
                if (yarnConf.get(addressKey) == null) {
                    yarnConf.set(addressKey, value + ":" + YarnConfiguration.DEFAULT_RM_PORT);
                }
            }
        }
        return yarnConf;
    }
}
