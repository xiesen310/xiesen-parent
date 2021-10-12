package com.github.xiesen.mysql.excel;


import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BizDataService {
    /**
     * 查询主机,topic信息
     *
     * @param file 文件完整路径
     * @return {@link List}
     */
    public static List<BizData> getBizByExcel(String file) {
        List<BizData> list = new ArrayList<BizData>();
        try {
            Workbook rwb = Workbook.getWorkbook(new File(file));
            //表
            Sheet rs = rwb.getSheet(0);
            //得到所有的列
            int clos = rs.getColumns();
            //得到所有的行
            int rows = rs.getRows();

            System.out.println("表的列数：" + clos + " 表的行数:" + rows);
            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < clos; j++) {
                    //第一个是列数，第二个是行数
                    //默认最左边编号也算一列 所以这里得j++
                    String hostname = rs.getCell(j++, i).getContents();

                    String logTypeName = rs.getCell(j++, i).getContents();
                    String appProgramName = rs.getCell(j++, i).getContents();
                    String appSystem = rs.getCell(j++, i).getContents();
                    String topic = rs.getCell(j++, i).getContents();


                    log.info("hostname: {}, logTypeName: {}, appProgramName: {}, appSystem: {}, topic: {}", hostname, logTypeName, appProgramName, appSystem, topic);
                    list.add(new BizData(hostname, logTypeName, appProgramName, appSystem, topic));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过 excel 获取蓝鲸 cmdb 中的信息
     *
     * @param file cmdb excel 信息
     * @return {@link List}
     */
    public static List<Cmdb> getCmdbByExcel(String file) {
        List<Cmdb> list = new ArrayList<Cmdb>();
        try {
            Workbook rwb = Workbook.getWorkbook(new File(file));
            //表
            Sheet rs = rwb.getSheet(0);
            //得到所有的列
            int clos = rs.getColumns();
            //得到所有的行
            int rows = rs.getRows();

            System.out.println("表的列数：" + clos + " 表的行数:" + rows);
            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < clos; j++) {
                    //第一个是列数，第二个是行数
                    //默认最左边编号也算一列 所以这里得j++
                    String bk_host_id = rs.getCell(j++, i).getContents();
                    String bk_host_innerip = rs.getCell(j++, i).getContents();
                    String bk_host_outerip = rs.getCell(j++, i).getContents();
                    String operator = rs.getCell(j++, i).getContents();
                    String bk_bak_operator = rs.getCell(j++, i).getContents();
                    String bk_asset_id = rs.getCell(j++, i).getContents();
                    String bk_sn = rs.getCell(j++, i).getContents();
                    String bk_comment = rs.getCell(j++, i).getContents();
                    String bk_service_term = rs.getCell(j++, i).getContents();
                    String bk_sla = rs.getCell(j++, i).getContents();
                    String bk_state_name = rs.getCell(j++, i).getContents();
                    String bk_province_name = rs.getCell(j++, i).getContents();
                    String bk_isp_name = rs.getCell(j++, i).getContents();
                    String os_name = rs.getCell(j++, i).getContents();
                    String serial_number = rs.getCell(j++, i).getContents();
                    String bk_brand = rs.getCell(j++, i).getContents();
                    String name = rs.getCell(j++, i).getContents();
                    String Annotation = rs.getCell(j++, i).getContents();
                    String status = rs.getCell(j++, i).getContents();
                    String pc_ip = rs.getCell(j++, i).getContents();
                    String committed = rs.getCell(j++, i).getContents();
                    String pc_username = rs.getCell(j++, i).getContents();
                    String unKnow = rs.getCell(j++, i).getContents();
                    String bk_biz_name = rs.getCell(j++, i).getContents();
                    String host_cpu = rs.getCell(j++, i).getContents();
                    String host_mem = rs.getCell(j++, i).getContents();
                    String host_disk = rs.getCell(j++, i).getContents();
                    String group_id = rs.getCell(j++, i).getContents();
                    String public_cloud = rs.getCell(j++, i).getContents();
                    String bk_host_name = rs.getCell(j++, i).getContents();
                    String bk_os_type = rs.getCell(j++, i).getContents();
                    String bk_os_name = rs.getCell(j++, i).getContents();
                    String bk_os_version = rs.getCell(j++, i).getContents();
                    String bk_os_bit = rs.getCell(j++, i).getContents();
                    String bk_cpu = rs.getCell(j++, i).getContents();
                    String bk_cpu_mhz = rs.getCell(j++, i).getContents();
                    String bk_cpu_module = rs.getCell(j++, i).getContents();
                    String bk_mem = rs.getCell(j++, i).getContents();
                    String bk_disk = rs.getCell(j++, i).getContents();
                    String bk_mac = rs.getCell(j++, i).getContents();
                    String bk_outer_mac = rs.getCell(j++, i).getContents();

                    if (bk_host_innerip.trim().length() > 0) {
                        log.info("bk_host_innerip: {}, os_name: {}, bk_biz_name: {}, bk_host_name: {}, bk_os_type: {}", bk_host_innerip, os_name, bk_biz_name, bk_host_name, bk_os_type);
                        list.add(new Cmdb(bk_host_innerip, os_name, bk_biz_name, bk_host_name, bk_os_type));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* public static boolean isExist(int id) {
         try {
             DB db = new DB();
             ResultSet rs = db.Search("select * from BizData where id=?", new String[]{id + ""});
             if (rs.next()) {
                 return true;
             }
         } catch (SQLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         return false;
     }*/
    public static void main(String[] args) {


//        System.out.println(isExist(1));

//        getBizByExcel("D:\\tmp\\excel\\topic.xls");
        getCmdbByExcel("D:\\tmp\\excel\\cmdb.xls");

    }

}