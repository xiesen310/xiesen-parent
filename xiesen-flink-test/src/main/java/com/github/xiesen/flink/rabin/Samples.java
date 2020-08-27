package com.github.xiesen.flink.rabin;

import com.github.xiesen.flink.fingerprint.RabinFingerprintLong;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiese
 * @Description Samples
 * @Email xiesen310@163.com
 * @Date 2020/8/24 9:17
 */
public class Samples {

    /**
     * 生成 es index id
     *
     * @param rabin       RabinFingerprintLong
     * @param timestamp   时间戳
     * @param source      数据源目录
     * @param offset      offset
     * @param appSystem   系统简称
     * @param logTypeName 日志集名称
     * @return
     */
    public static String generateEsId(RabinFingerprintLong rabin, String timestamp, String source, Long offset, String appSystem, String logTypeName) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotEmpty(timestamp)) {
            builder.append(timestamp);
        }
        if (StringUtils.isNotEmpty(source)) {
            builder.append(source);
        }

        if (offset != null) {
            builder.append(offset);
        }

        if (StringUtils.isNotEmpty(appSystem)) {
            builder.append(appSystem);
        }

        if (StringUtils.isNotEmpty(logTypeName)) {
            builder.append(logTypeName);
        }

        rabin.reset();
        rabin.pushBytes(builder.toString().getBytes());
        String id = Long.toString(rabin.getFingerprintLong(), 16);
        return id;
    }

    public static void main(String[] args) throws Exception {
//        example();

        RabinFingerprintLong rabin = RabinFingerprintLong.getInstance();
        int size = 1000;
        String timestamp = "2020-08-21T17:28:05.079+08:00";
        String source = "/var/log/tdx.log";
        Long offset = 0L;
        String appSystem = "tdx";
        String logTypeName = "tdx_filebeat";
        long start = System.nanoTime();

        for (int i = 0; i < size; i++) {
            System.out.println(generateEsId(rabin, timestamp, source, offset, appSystem, logTypeName));
        }

        long end = System.nanoTime();
        System.out.println("耗时: " + (end - start) / 1000 + " ns");
    }


    /**
     * 测试
     */
    private static void example() {
        RabinFingerprintLong rabin = RabinFingerprintLong.getInstance();
        long start = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            rabin.reset();
            rabin.pushBytes("2020-08-21T17:28:05.079+08:00/var/log/tdx.log0tdxtdx_filebeat2020-08-21T17:28:06.880+08:00".getBytes());
//            rabin.pushBytes("xiesenxie".getBytes());
            System.out.println(Long.toString(rabin.getFingerprintLong(), 16));
        }
        long end = System.nanoTime();

        System.out.println("耗时: " + (end - start) / 1000 + " ns");
    }
}
