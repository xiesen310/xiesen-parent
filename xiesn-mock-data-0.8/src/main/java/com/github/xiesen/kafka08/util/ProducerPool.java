package com.github.xiesen.kafka08.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author xiese
 * @Description 连接池
 * @Email xiesen310@163.com
 * @Date 2020/7/23 20:26
 */
public class ProducerPool implements Closeable {
    private CustomerProducer08[] pool;

    private int threadNum = 15;
    // 轮循id
    private int index = 0;

    private static ProducerPool producerInstance = null;

    public static ProducerPool getInstance(String propertiesName) {
        if (producerInstance == null) {
            producerInstance = new ProducerPool(propertiesName);
        }
        return ProducerPool.producerInstance;
    }

    private ProducerPool(String propertiesName) {
        init(propertiesName);
    }


    public void init(String propertiesName) {
        pool = new CustomerProducer08[threadNum];
        for (int i = 0; i < threadNum; i++) {
            pool[i] = new CustomerProducer08(propertiesName);
        }
    }

    public CustomerProducer08 getProducer() {
        if (index > 65535) {
            index = 0;
        }
        return pool[index++ % threadNum];
    }

    @Override
    public void close() throws IOException {

    }
}

