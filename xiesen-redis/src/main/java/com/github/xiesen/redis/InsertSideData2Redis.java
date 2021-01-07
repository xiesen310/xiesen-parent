package com.github.xiesen.redis;

import com.github.xiesen.redis.utils.CountryCode;
import com.github.xiesen.redis.utils.JdbcUtils;
import redis.clients.jedis.Jedis;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢森
 * @Description InsertSideData2Redis
 * @Email xiesen310@163.com
 * @Date 2021/1/7 9:53
 */
public class InsertSideData2Redis {
    public static void main(String[] args) {
        List<CountryCode> countryCodes = queryAllRecord();
        insertRedis(countryCodes);
    }


    /**
     * 插入数据到 redis
     *
     * @param models
     */
    public static void insertRedis(List<CountryCode> models) {
        String prefix = "country_";

        Jedis jedis = new Jedis("192.168.1.224");
        jedis.select(9);

        for (CountryCode model : models) {
            String outerKey = prefix + "" + model.getCode();
            jedis.hset(outerKey, "en_name", model.getEn_name());
            jedis.hset(outerKey, "ch_name", model.getCh_name());
            jedis.hset(outerKey, "code", model.getCode());
            jedis.hset(outerKey, "tel_code", String.valueOf(model.getTel_code()));
            jedis.hset(outerKey, "time_diff", String.valueOf(model.getTime_diff()));
        }
        System.out.println("done");
        jedis.close();
    }


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
