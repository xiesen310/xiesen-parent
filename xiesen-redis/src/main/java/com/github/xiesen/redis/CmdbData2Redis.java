package com.github.xiesen.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.redis.mysql.BaseDao;
import com.github.xiesen.redis.pojo.RedisInfo;
import com.github.xiesen.redis.utils.RedisHelper;
import com.github.xiesen.redis.xxhash.AbstractLongHashFunction;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 谢森
 * @Description InsertSideData2Redis
 * @Email xiesen310@163.com
 * @Date 2021/1/7 9:53
 */
public class CmdbData2Redis {
    public static final String METRIC_PREFIX = "xiesen_metric";

    public static final String REDIS_HOST = "192.168.70.21";
    public static final int REDIS_PORT = 6379;
    public static final int REDIS_DATABASE = 15;
    public static final String REDIS_PASSWORD = "zorkdata.8888";
    private static JedisPool jedisPool = null;
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    public static final int MOCK_DATA_SIZE = 5;
    private static final CountDownLatch latch = new CountDownLatch(MOCK_DATA_SIZE);


    public static void main(String[] args) throws SQLException {
        readCmdbTopo();
        final RedisInfo redisInfo = RedisInfo.builder().host(REDIS_HOST).pwd(REDIS_PASSWORD).database(REDIS_DATABASE).port(REDIS_PORT).build();
        final RedisHelper redisHelper = new RedisHelper(redisInfo);

        final Map<String, String> map = readCmdbTopo();
        redisHelper.insert(map);
    }

    public static Map<String, String> readCmdbTopo() throws SQLException {
        Map<String, String> map = new HashMap<>();
        final Connection conn = BaseDao.getConn();
        String sql = "SELECT YWJC as appsystem, MKMC as clustername,ZUJIANMC as appprogramname,ZJMC AS hostname, IP as ip FROM Topo WHERE WHZMC in ('集中交易系统','君弘');";
        final PreparedStatement statement = conn.prepareStatement(sql);
        final ResultSet resultSet = statement.executeQuery();
        try {
            while (resultSet.next()) {
                final String ip = resultSet.getString("ip");
                //
                final String appsystem = resultSet.getString("appsystem");
                final String clustername = resultSet.getString("clustername");
                final String appprogramname = resultSet.getString("appprogramname");
                final String hostname = resultSet.getString("hostname");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ip", ip);
                jsonObject.put("appsystem", null == appsystem ? "未分配" : appsystem);
                jsonObject.put("clustername", null == clustername ? "未分配" : clustername);
                jsonObject.put("appprogramname", null == appprogramname ? "未分配" : appprogramname);
                jsonObject.put("hostname", null == hostname ? "未分配" : hostname);
                if (null != ip) {
                    map.put(ip, jsonObject.toJSONString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            conn.close();
        }
        return map;
    }

    private static void mulWriterData() {
        long start = System.currentTimeMillis();
        initJedisPool();
        for (int i = 0; i < MOCK_DATA_SIZE; i++) {
            pool.execute(new MockMetric(i));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("多线程生成 " + MOCK_DATA_SIZE + " 个指标数据总耗时: " + (end - start) + " ms");
        pool.shutdownNow();
    }


    private static void mulDeleteData() {
        long start = System.currentTimeMillis();
        initJedisPool();
        for (int i = 0; i < MOCK_DATA_SIZE; i++) {
            pool.execute(new MockMetric(i));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("多线程生成 " + MOCK_DATA_SIZE + " 个指标数据总耗时: " + (end - start) + " ms");
        pool.shutdownNow();
    }


    static class MockMetric implements Runnable {
        private int num;

        public MockMetric(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                writerModelData2Redis(jedis, num);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
                latch.countDown();
            }
        }
    }

    static class DeleteMetric implements Runnable {
        private int num;

        public DeleteMetric(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            Jedis jedis = jedisPool.getResource();
            try {
                deleteKey(jedis, num);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
                latch.countDown();
            }
        }
    }


    private static void initJedisPool() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(120);
        config.setMaxIdle(120);
        config.setMinIdle(100);
        config.setMaxWaitMillis(10000L);
        if (null == jedisPool) {
            jedisPool = new JedisPool(config, REDIS_HOST, REDIS_PORT, 10000, REDIS_PASSWORD, REDIS_DATABASE);
        }
        System.out.println();
    }


    private static void deleteData(int dataSize) {
        long start = System.currentTimeMillis();
        deleteKeyWithPrefix(dataSize);
        long end = System.currentTimeMillis();
        System.out.println("删除 " + dataSize + " 个指标数据总耗时: " + (end - start) + " ms");
    }

    private static void generateData(int dataSize) {
        long start = System.currentTimeMillis();
        writerModelData2Redis(dataSize);
        long end = System.currentTimeMillis();
        System.out.println("生成 " + dataSize + " 个指标数据总耗时: " + (end - start) + " ms");

    }

    private static void writerModelData2Redis(Jedis jedis, int num) {
        String outerKey = METRIC_PREFIX + "" + num;
        long start = System.currentTimeMillis();
        mockDataProcess2(jedis, outerKey);
        long end = System.currentTimeMillis();
        System.out.println("写入 [" + outerKey + "] 指标数据完成,耗时: " + (end - start) + " ms.");
    }

    private static void mockDataProcess2(Jedis jedis, String outerKey) {
        for (int i = 0; i < 255; i++) {
            for (int j = 0; j < 255; j++) {
                Map<String, Object> bigMap = new HashMap<>();
                bigMap.put("metricsetname", outerKey);
                Map<String, String> metrics = new HashMap<>();
                metrics.put("delay", "");
                metrics.put("Pool_free_mem", "");
                metrics.put("Fra_used", "");
                bigMap.put("metrics", metrics);
                Map<String, String> dimensions = new HashMap<>();
                dimensions.put("hostname", "zorkdata" + i + "-" + j + ".host.com");
                dimensions.put("appprogramname", "核心2");
                dimensions.put("clustername", "各核心kcbp群集");
                dimensions.put("ip", "192.168." + i + "." + j);
                dimensions.put("appsystem", "jzjy");
                bigMap.put("dimensions", dimensions);

                String innerKey = xxHashModelId(outerKey, dimensions);
                bigMap.put("instanceId", innerKey);
                jedis.hset(outerKey, innerKey, JSON.toJSONString(bigMap));
            }
        }
    }


    private static void writerModelData2Redis(int metricSize) {
        if (metricSize <= 0) {
            System.out.println("metricSize 必须大于0");
            return;
        }
        Jedis jedis = new Jedis(CmdbData2Redis.REDIS_HOST);
        jedis.auth(CmdbData2Redis.REDIS_PASSWORD);
        jedis.select(CmdbData2Redis.REDIS_DATABASE);

        for (int i = 0; i < metricSize; i++) {
            String outerKey = METRIC_PREFIX + "" + i;
            long start = System.currentTimeMillis();
            for (int j = 0; j < 255; j++) {
                Map<String, Object> bigMap = new HashMap<>();
                bigMap.put("metricsetname", outerKey);
                Map<String, String> metrics = new HashMap<>();
                metrics.put("delay", "");
                metrics.put("Pool_free_mem", "");
                metrics.put("Fra_used", "");
                bigMap.put("metrics", metrics);
                Map<String, String> dimensions = new HashMap<>();
                dimensions.put("hostname", "zorkdata70-" + j + ".host.com");
                dimensions.put("appprogramname", "核心2");
                dimensions.put("clustername", "各核心kcbp群集");
                dimensions.put("ip", "192.168.70." + j);
                dimensions.put("appsystem", "jzjy");
                bigMap.put("dimensions", dimensions);

                String innerKey = xxHashModelId(outerKey, dimensions);
                bigMap.put("instanceId", innerKey);
                jedis.hset(outerKey, innerKey, JSON.toJSONString(bigMap));
            }
            long end = System.currentTimeMillis();
            System.out.println("写入 [" + outerKey + "] 指标数据完成,耗时: " + (end - start) + " ms.");
        }
    }


    private static void deleteKeyWithPrefix(int metricSize) {
        Jedis jedis = new Jedis(CmdbData2Redis.REDIS_HOST);
        jedis.auth(CmdbData2Redis.REDIS_PASSWORD);
        jedis.select(CmdbData2Redis.REDIS_DATABASE);
        for (int i = 0; i < metricSize; i++) {
            long start = System.currentTimeMillis();
            String outerKey = METRIC_PREFIX + "" + i;
            final Map<String, String> map = jedis.hgetAll(outerKey);
            map.forEach((k, v) -> jedis.hdel(outerKey, k));
            long end = System.currentTimeMillis();
            System.out.println("删除 " + outerKey + " 指标数据耗时: " + (end - start) + " ms.");
        }
    }

    private static void deleteKey(Jedis jedis, int num) {
        long start = System.currentTimeMillis();
        String outerKey = METRIC_PREFIX + "" + num;
        final Map<String, String> map = jedis.hgetAll(outerKey);
        map.forEach((k, v) -> jedis.hdel(outerKey, k));
        long end = System.currentTimeMillis();
        System.out.println("删除 " + outerKey + " 指标数据耗时: " + (end - start) + " ms.");
    }

    /**
     * xx hash model id
     *
     * @return String
     */
    public static String xxHashModelId(String metricSetName, Map<String, String> dimensions) {
        StringBuilder builder = new StringBuilder().append(metricSetName);
        dimensions.forEach((k, v) -> builder.append(k).append(v));
        long l = AbstractLongHashFunction.xx().hashChars(builder.toString());
        return Long.toString(l, 16);
    }

}

