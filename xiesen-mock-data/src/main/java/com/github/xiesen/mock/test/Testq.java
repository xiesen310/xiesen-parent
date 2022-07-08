package com.github.xiesen.mock.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiesen
 * @title: Testq
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/3/23 16:50
 */
public class Testq {
    public static Map<String, String> getDynamicProperties(String dynamicPropertiesEncoded) {
        if (dynamicPropertiesEncoded != null && dynamicPropertiesEncoded.length() > 0) {
            Map<String, String> properties = new HashMap();
            String[] propertyLines = dynamicPropertiesEncoded.split("@@");
            String[] var3 = propertyLines;
            int var4 = propertyLines.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String propLine = var3[var5];
                if (propLine != null) {
                    int firstEquals = propLine.indexOf("=");
                    if (firstEquals >= 0) {
                        String key = propLine.substring(0, firstEquals).trim();
                        String value = propLine.substring(firstEquals + 1, propLine.length()).trim();
                        if (!key.isEmpty()) {
                            properties.put(key, value);
                        }
                    }
                }
            }

            return properties;
        } else {
            return Collections.emptyMap();
        }
    }

    public static void main(String[] args) {
        String dynamicPropertiesEncoded = "env.java.opts: -DjobName=metricstore_dwd_all_metric96 -DyarncontainerId=$CONTAINER_ID -DnodeId=$_FLINK_NODE_ID -DhdfsUser=$HADOOP_USER_NAME";
        Map<String, String> dynamicProperties = getDynamicProperties(dynamicPropertiesEncoded);
        dynamicProperties.forEach((k, v) -> System.out.println(k + "=" + v));
    }
}
