package com.github.xiesen.common.utils;

import cn.hutool.core.map.MapUtil;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataQualityNullAssessment {
    private static Map<String, Integer> columnCounts = new HashMap<>();

    static {
        columnCounts.put("logTypeName", 0);
        columnCounts.put("source", 0);
        columnCounts.put("normalFields.nullKey", 0);
    }

    public static void main(String[] args) {
        List<Map<String, Object>> list = MockDataUtil.mockLogDataList("hzy_log", 100);
        int totalRows = 0;

        for (Map<String, Object> record : list) {
            totalRows++;
            for (Map.Entry<String, Integer> entry : columnCounts.entrySet()) {
                String columnName = entry.getKey();
                if (columnName.contains(".")) {
                    String[] parts = columnName.split("\\.");
                    Map innerMap = MapUtils.getMap(record, parts[0]);
                    checkNullColumn(innerMap, columnName);
                } else {
                    checkNullColumn(record, columnName);
                }
            }
        }

        // 输出结果
        for (Map.Entry<String, Integer> entry : columnCounts.entrySet()) {
            String columnName = entry.getKey();
            int nullCount = entry.getValue();
            double nullPercentage = (double) nullCount / totalRows * 100;
            System.out.printf("Column '%s' has %d null values (%.2f%%)%n", columnName, nullCount, nullPercentage);
        }
    }

    private static void checkNullColumn(Map<String, Object> record, String columnName) {
        String key = columnName;
        if (columnName.contains(".")) {
            String[] parts = columnName.split("\\.");
            key = parts[1];
        }
        String value = MapUtil.getStr(record, key);
        if (value == null || value.trim().isEmpty()) {
            columnCounts.put(columnName, columnCounts.get(columnName) + 1);
        }
    }
}
