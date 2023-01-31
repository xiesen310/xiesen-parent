package com.github.xiesen.redis.utils;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.redis.pojo.MetricModel;
import com.github.xiesen.redis.pojo.MetricSet;
import com.github.xiesen.redis.pojo.RedisInfo;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * @author xiesen
 */
public class RedisHelper {
    private static JedisPool jedisPool = null;
    private RedisInfo redisInfo;

    private RedisHelper() {
    }

    public RedisHelper(RedisInfo redisInfo) {
        this.redisInfo = redisInfo;
        init();
    }

    private void init() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(10);
        config.setMinIdle(5);
        config.setMaxWaitMillis(10000L);
        if (null == jedisPool) {
            jedisPool = new JedisPool(config, redisInfo.getHost(), redisInfo.getPort(), redisInfo.getTimeout(), redisInfo.getPwd(), redisInfo.getDatabase());
        }
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    private void close(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }

    public Map<String, MetricModel> getDataByKey(String outerKey) {
        Jedis jedis = jedisPool.getResource();
        final Map<String, String> mapList = jedis.hgetAll(outerKey);
        Map<String, MetricModel> map = new HashMap<>(mapList.size());
        mapList.forEach((k, v) -> {
            final MetricModel metricModel = JSON.parseObject(v, MetricModel.class);
            map.put(k, metricModel);
        });
        close(jedis);
        return map;
    }

    public MetricSet getMetricSetByKey(String outerKey) {
        Jedis jedis = jedisPool.getResource();
        final Map<String, String> mapList = jedis.hgetAll(outerKey);

        MetricSet metricSet = null;
        for (Map.Entry<String, String> entry : mapList.entrySet()) {
            final String value = entry.getValue();
            metricSet = JSON.parseObject(value, MetricSet.class);
            if (null != metricSet) {
                break;
            }
        }

        close(jedis);
        return metricSet;
    }

    public Set<String> getListKey(String prefix) {
        Jedis jedis = jedisPool.getResource();
        final Set<String> keys = jedis.keys(prefix + "*");
        close(jedis);
        return keys;
    }
}
