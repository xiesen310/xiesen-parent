package com.github.xiesen.redis;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MockStatusData2Redis {
    public static final String BIZ_LOG_STATUS_PROCESS = "process";
    private static final String EXPIRE_TIME_CONFIG = "5m";
    private static final int MAX_TOTAL = 120;
    private static final int MAX_IDLE = 120;
    private static final int MIN_IDLE = 100;
    private static final long MAX_WAIT_MILLIS = 10000L;

    public static final String REDIS_HOST = "192.168.70.21";
    public static final int REDIS_PORT = 6379;
    public static final int REDIS_DATABASE = 4;
    public static final String REDIS_PASSWORD = "zorkdata.8888";
    private static volatile JedisPool jedisPool = null;

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            String jobName = i + "-" + "xiesen_2024.03.25";
            writeStatusToRedis(jobName);
            Console.log("写入数据成功: {}", jobName);
        }
        selectAllStatus();
    }

    private static void writeStatusToRedis(String jobName) {
        getJedisPool();
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis == null) {
                throw new IllegalStateException("Failed to get Jedis resource from pool");
            }

            int expireTime = convertScrollToSeconds(EXPIRE_TIME_CONFIG);
            long total = RandomUtil.randomLong(10000, 1000000);
            long processCount = RandomUtil.randomLong(0, total);
            String scrollId = RandomUtil.randomString(128);

            writeStatusInternal(jedis, jobName, scrollId, String.valueOf(total), String.valueOf(processCount), BIZ_LOG_STATUS_PROCESS, expireTime);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Console.error("Error occurred while writing status to Redis", e);
        } catch (Exception e) {
            Console.error("Unexpected error occurred while writing status to Redis", e);
        }
    }


    private static void writeStatusInternal(Jedis jedis, String jobName, String scrollId, String total, String processCount, String status, int expireTime) {
        try {
            Pipeline pipelined = jedis.pipelined();
            pipelined.hset(jobName, "scrollId", scrollId);
            pipelined.hset(jobName, "total", total);
            pipelined.hset(jobName, "processCount", processCount);
            pipelined.hset(jobName, "status", status);
            pipelined.hset(jobName, "ts", String.valueOf(System.currentTimeMillis()));

            // 控制缓存过期时间
//            pipelined.expire(jobName, expireTime);
            pipelined.sync();
        } catch (Exception e) {
            Console.error("Failed to write status to Redis: ", e);
        }
    }

    /**
     * 将Elasticsearch的scroll时间值转换为秒。
     *
     * @param scrollValue Elasticsearch scroll时间值，例如"1m"或"5h"。
     * @return 对应的秒数。
     */
    public static int convertScrollToSeconds(String scrollValue) {
        Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z]+)");
        Matcher matcher = pattern.matcher(scrollValue);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid scroll value format: " + scrollValue);
        }

        int number = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2).toLowerCase();

        switch (unit) {
            case "s":
                return number;
            case "m":
                return number * 60;
            case "h":
                return number * 3600;
            default:
                throw new IllegalArgumentException("Unsupported time unit: " + unit);
        }
    }


    private static synchronized void initJedisPool() {
        if (jedisPool == null) {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMinIdle(MIN_IDLE);
            config.setMaxWaitMillis(MAX_WAIT_MILLIS);

            try {
                jedisPool = new JedisPool(config, REDIS_HOST, REDIS_PORT, 10000, REDIS_PASSWORD, REDIS_DATABASE);
            } catch (Exception e) {
                // 记录异常日志
                Console.error("Failed to initialize JedisPool: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize JedisPool", e);
            }
        }
    }

    public static void getJedisPool() {
        if (jedisPool == null) {
            initJedisPool();
        }
    }

    private static void selectAllStatus() {
        try (Jedis jedis = jedisPool.getResource()) {
            String cursor = "0";
            ScanParams scanParams = new ScanParams().count(100);

            while (true) {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                cursor = scanResult.getStringCursor();
                List<String> keys = scanResult.getResult();

                if (keys.isEmpty()) {
                    break;
                }

                for (String key : keys) {
                    Map<String, String> map = jedis.hgetAll(key);
                    String json = JSON.toJSONString(map);
                    Console.log("key: {}, value: {}", key, json);
                }

                if ("0".equals(cursor)) {
                    break;
                }
            }
        } catch (Exception e) {
            Console.error("Error occurred while scanning Redis keys", e);
        }
    }

}

