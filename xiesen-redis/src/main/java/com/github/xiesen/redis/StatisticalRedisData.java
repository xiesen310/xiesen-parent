package com.github.xiesen.redis;

import com.github.xiesen.redis.pojo.MetricModel;
import com.github.xiesen.redis.pojo.RedisInfo;
import com.github.xiesen.redis.utils.MysqlHelper;
import com.github.xiesen.redis.utils.RedisHelper;

import java.util.Map;
import java.util.Set;

/**
 * @author xiesen
 */
public class StatisticalRedisData {
    public static final String METRIC_PREFIX = "xiesen_metric";

    public static final String REDIS_HOST = "192.168.70.21";
    public static final int REDIS_PORT = 6379;
    public static final int REDIS_DATABASE = 13;
    public static final String REDIS_PASSWORD = "zorkdata.8888";

    public static void main(String[] args) {
        final RedisInfo redisInfo = RedisInfo.builder().host(REDIS_HOST).port(REDIS_PORT).database(REDIS_DATABASE).pwd(REDIS_PASSWORD).build();
        final RedisHelper redisHelper = new RedisHelper(redisInfo);
        final Set<String> listKey = redisHelper.getListKey(METRIC_PREFIX);
        int modelNum = 0;
        for (String key : listKey) {
            final Map<String, MetricModel> dataByKey = redisHelper.getDataByKey(key);
            modelNum += dataByKey.size();
            System.out.println(key + " 中模型数据一共 " + dataByKey.size() + " 条");
        }
        System.out.println("一共 " + modelNum + " 条模型数据.");
    }
}
