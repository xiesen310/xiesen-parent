package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.KafkaTools;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockUserJson {
    public static String mockUserJson() {
        Map<String, Object> bigMap = new HashMap<>();
        bigMap.put("id", new Random().nextInt(100000));
        bigMap.put("name", UUID.randomUUID().toString().replaceAll("-", ""));
        bigMap.put("age", new Random().nextInt(100));
        return JSON.toJSONString(bigMap);
    }

    public static void main(String[] args) throws Exception {

        String topic = "test";
        String bootstrapServers = "192.168.70.6:29092,192.168.70.7:29092,192.168.70.8:29092";
        long records = 1000L;

        KafkaProducer<String, String> producer = KafkaTools.buildProducer(bootstrapServers, StringSerializer.class.getName());
        for (long index = 0; index < records; index++) {
            KafkaTools.send(producer, topic, mockUserJson());
            Thread.sleep(2000);
        }

        Thread.sleep(2000);
    }
}
