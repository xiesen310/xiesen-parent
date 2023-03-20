package com.github.xiesen.mock.test;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.io.File;
import java.util.List;

/**
 * @author xiesen
 */
public class TestExcel {
    public static void main(String[] args) {
        final ExcelReader reader = ExcelUtil.getReader(new File("D:\\tmp\\funcid.xlsx"));
        final List<List<Object>> lists = reader.read();
        for (List<Object> list : lists) {
            System.out.println(list);
        }
    }
}
