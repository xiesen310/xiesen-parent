package com.github.xiesen.mock.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author xiese
 * @Description kafka producer 连接池
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:54
 */
public class ProducerPool implements Closeable {
    private CustomerProducer[] pool;

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
        pool = new CustomerProducer[threadNum];
        for (int i = 0; i < threadNum; i++) {
            pool[i] = new CustomerProducer(propertiesName);
        }
    }

    public CustomerProducer getProducer() {
        if (index > 65535) {
            index = 0;
        }
        return pool[index++ % threadNum];
    }

    @Override
    public void close() throws IOException {

    }
}
