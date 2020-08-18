package com.github.xiesen.jmeter.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/8/14 12:44
 */
public class ProducerPool implements Closeable {

    private Producer[] pool;

    private int threadNum = 15;

    /**
     * 轮循id
     */
    private int index = 0;

    private static ProducerPool producerInstance;

    public static ProducerPool getInstance(String kafkaAddress) {
        if (producerInstance == null) {
            producerInstance = new ProducerPool(kafkaAddress);
        }
        return ProducerPool.producerInstance;
    }

    private ProducerPool(String kafkaAddress) {
        init(kafkaAddress);
    }

    public void init(String kafkaAddress) {
        pool = new Producer[threadNum];
        for (int i = 0; i < threadNum; i++) {
            pool[i] = new Producer(kafkaAddress);
        }
    }

    public Producer getProducer() {
        final int maxThread = 65535;
        if (index > maxThread) {
            index = 0;
        }
        return pool[index++ % threadNum];
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * <p> As noted in {@link AutoCloseable#close()}, cases where the
     * close may fail require careful attention. It is strongly advised
     * to relinquish the underlying resources and to internally
     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code IOException}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
    }
}

