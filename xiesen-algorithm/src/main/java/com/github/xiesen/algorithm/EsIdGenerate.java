package com.github.xiesen.algorithm;


import com.github.xiesen.algorithm.constants.AlarmConstants;
import com.github.xiesen.algorithm.xxhash.AbstractLongHashFunction;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author xiese
 * @Description es id 生成器
 * @Email xiesen310@163.com
 * @Date 2020/8/26 16:33
 */
public class EsIdGenerate {


    /**
     * xxhash alarm push
     *
     * @param map
     * @return
     */
    public static String xxHashAlarmPush2es(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        if (map.containsKey(AlarmConstants.ID)) {
            builder.append(map.get(AlarmConstants.ID));
        }

        if (map.containsKey(AlarmConstants.APP_SYSTEM)) {
            builder.append(map.get(AlarmConstants.APP_SYSTEM));
        }

        if (map.containsKey(AlarmConstants.ALARM_CHANNEL_ID)) {
            builder.append(map.get(AlarmConstants.ALARM_CHANNEL_ID));
        }

        if (map.containsKey(AlarmConstants.STR_TIME)) {
            builder.append(map.get(AlarmConstants.STR_TIME));
        }
        long l = AbstractLongHashFunction.xx().hashChars(builder.toString());
        String id = Long.toString(l, 16);
        return id;
    }

    /**
     * xxhash alarm2es
     *
     * @param map
     * @return
     */
    public static String xxHashAlarm2es(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        if (map.containsKey(AlarmConstants.TIMESTAMP)) {
            builder.append(map.get(AlarmConstants.TIMESTAMP));
        }

        if (map.containsKey(AlarmConstants.ALARM_TYPE_NAME)) {
            builder.append(map.get(AlarmConstants.ALARM_TYPE_NAME));
        }

        if (map.containsKey(AlarmConstants.STATUS)) {
            builder.append(map.get(AlarmConstants.STATUS));
        }

        if (map.containsKey(AlarmConstants.EXT_FIELDS)) {
            Map<String, String> extFields = (Map<String, String>) map.get(AlarmConstants.EXT_FIELDS);
            if (extFields != null && extFields.containsKey(AlarmConstants.EXPRESSION_ID)) {
                builder.append(extFields.get(AlarmConstants.EXPRESSION_ID));
            }

            if (extFields != null && extFields.containsKey(AlarmConstants.ALARM_TIME)) {
                builder.append(extFields.get(AlarmConstants.ALARM_TIME));
            }
        }

        if (map.containsKey(AlarmConstants.SOURCES)) {
            Map<String, String> sources = (Map<String, String>) map.get(AlarmConstants.SOURCES);
            if (sources != null && sources.containsKey(AlarmConstants.HOSTNAME)) {
                builder.append(sources.get(AlarmConstants.HOSTNAME));
            }
            if (sources != null && sources.containsKey(AlarmConstants.APP_SYSTEM)) {
                builder.append(sources.get(AlarmConstants.APP_SYSTEM));
            }
            if (sources != null && sources.containsKey(AlarmConstants.IP)) {
                builder.append(sources.get(AlarmConstants.IP));
            }
        }
        long l = AbstractLongHashFunction.xx().hashChars(builder.toString());
        String id = Long.toString(l, 16);
        return id;
    }


    /**
     * xx hash log2es
     *
     * @param timestamp   时间戳
     * @param source      数据源目录
     * @param offset      offset
     * @param appSystem   系统简称
     * @param logTypeName 日志集名称
     * @return
     */
    public static String xxHashLog2es(String timestamp, String source, Long offset, String appSystem,
                                      String logTypeName) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotEmpty(timestamp)) {
            builder.append(timestamp);
        }
        if (StringUtils.isNotEmpty(source)) {
            builder.append(source);
        }

        if (offset != null) {
            builder.append(offset);
        }

        if (StringUtils.isNotEmpty(appSystem)) {
            builder.append(appSystem);
        }

        if (StringUtils.isNotEmpty(logTypeName)) {
            builder.append(logTypeName);
        }

        long l = AbstractLongHashFunction.xx().hashChars(builder.toString());
        String id = Long.toString(l, 16);
        return id;
    }


}
