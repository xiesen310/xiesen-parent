package com.github.xiesen.excel;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.reader.SheetReader;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author xiesen
 */
public class MetricExcel {
    public static void main(String[] args) {
        final ExcelReader reader = ExcelUtil.getReader(new File("D:\\tmp\\国泰君安运维指标体系_20230221.xlsx"), 15);
        final List<String> sheetNames = reader.getSheetNames();
        for (int i = 0; i < sheetNames.size(); i++) {
            System.out.println("index: " + i + " ; sheetName: " + sheetNames.get(i));
        }


        final List<List<Object>> read = reader.read(3, reader.getRowCount());
        final List<Map<String, Object>> maps = reader.readAll();
        for (Map<String, Object> map : maps) {
            map.forEach((key, value) -> {
                System.out.println(key + " = " + value);
            });
        }
    }
}
