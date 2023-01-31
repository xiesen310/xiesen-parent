package com.github.xiesen.redis;


import com.github.xiesen.redis.pojo.MetricModel;
import com.github.xiesen.redis.pojo.MetricSet;
import com.github.xiesen.redis.pojo.RedisInfo;
import com.github.xiesen.redis.utils.MysqlHelper;
import com.github.xiesen.redis.utils.RedisHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xiesen
 */
public class Redis2Mysql {
    public static final String METRIC_PREFIX = "xiesen_metric";

    public static final String REDIS_HOST = "192.168.70.21";
    public static final int REDIS_PORT = 6379;
    public static final int REDIS_DATABASE = 13;
    public static final String REDIS_PASSWORD = "zorkdata.8888";

    public static void main(String[] args) {
        // 由于测试,在写入数据前先清除mysql表中的数据
        MysqlHelper.testTruncateTables();

        // 模型同步
        long start = System.currentTimeMillis();
        final RedisInfo redisInfo = RedisInfo.builder().host(REDIS_HOST).port(REDIS_PORT).database(REDIS_DATABASE).pwd(REDIS_PASSWORD).build();
        final RedisHelper redisHelper = new RedisHelper(redisInfo);
        final Set<String> listKey = redisHelper.getListKey(METRIC_PREFIX);
        int modelNum = 0;
        for (String key : listKey) {
            final Map<String, MetricModel> dataByKey = redisHelper.getDataByKey(key);
            modelNum += dataByKey.size();
            MysqlHelper.writerMetricModel(dataByKey);
        }

        // 指标集数据同步
        final long modelEndTime = System.currentTimeMillis();
        System.out.println("redis 模型数据写入同步到 mysql 总耗时: " + (modelEndTime - start) + " ms" + " , 一共同步了 " + modelNum + " 条模型数据.");
        Map<String, MetricSet> metricSetMap = new HashMap<>();
        for (String metricSetName : listKey) {
            final MetricSet metricSet = redisHelper.getMetricSetByKey(metricSetName);
            metricSetMap.put(metricSetName, metricSet);
        }
        MysqlHelper.writerDataSet(metricSetMap);
        System.out.println("redis 指标集数据写入同步到 mysql 总耗时: " + (System.currentTimeMillis() - modelEndTime) + " ms" + " , 一共同步了 " + metricSetMap.size() + " 条指标集数据.");

        System.out.println("redis 数据同步到 mysql 总耗时: " + (System.currentTimeMillis() - start) + " ms" + " , 一共同步了 " + modelNum + " 条数据.");

    }
}
