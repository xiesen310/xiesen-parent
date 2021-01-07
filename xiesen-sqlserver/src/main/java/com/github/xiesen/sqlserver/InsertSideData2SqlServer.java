package com.github.xiesen.sqlserver;

import com.github.xiesen.sqlserver.utils.CountryCode;
import com.github.xiesen.sqlserver.utils.JdbcUtils;
import com.github.xiesen.sqlserver.utils.SqlServerJdbcUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢森
 * @Description InsertSideData2SqlServer
 * @Email xiesen310@163.com
 * @Date 2021/1/7 10:00
 */
public class InsertSideData2SqlServer {

    public static void main(String[] args) throws IOException {
        List<CountryCode> countryCodes = queryAllRecord();
        insertSQLServer(countryCodes);
    }

    /**
     * 插入数据到 SQL server 中
     *
     * @param models
     * @throws IOException
     */
    public static void insertSQLServer(List<CountryCode> models) throws IOException {
        String insertSQL = "insert into flinkx.contry_info1 values (?,?,?,?,?)";
        List<Object[]> paramsList = new ArrayList<Object[]>();
        for (CountryCode model : models) {
            Object[] params = new Object[]{
                    model.getEn_name(),
                    model.getCh_name(),
                    model.getCode(),
                    model.getTel_code(),
                    model.getTime_diff()
            };
            paramsList.add(params);
        }
        SqlServerJdbcUtils.getInstance().executeBatch(insertSQL, paramsList);
        System.out.println("done");
    }


    /**
     * 查询 mysql 中的维表数据信息
     *
     * @return
     */
    public static List<CountryCode> queryAllRecord() {
        String selectSQL = "select * from country_code";
        List<CountryCode> lists = new ArrayList<>();

        JdbcUtils.getInstance().executeQuery(selectSQL, new JdbcUtils.QueryCallBack() {

            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    String en_name = rs.getString(1);
                    String ch_name = rs.getString(2);
                    String code = rs.getString(3);
                    int tel_code = rs.getInt(4);
                    int time_diff = rs.getInt(5);

                    CountryCode model = new CountryCode(en_name, ch_name, code, tel_code, time_diff);
                    lists.add(model);
                }
            }
        });

        return lists;
    }
}
