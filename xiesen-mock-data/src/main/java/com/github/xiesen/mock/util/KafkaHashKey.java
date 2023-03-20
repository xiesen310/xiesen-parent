package com.github.xiesen.mock.util;

import org.apache.kafka.common.utils.Utils;

import java.nio.charset.StandardCharsets;

/**
 * @author xiesen
 */
public class KafkaHashKey {
    public static void main(String[] args) {
        int numPartitions = 3;
        String key = "zork1-92.host.com_/opt/xiesen/filebeat-7.4.0-linux-x86_64/kcbpLog/20230308/runlog0.log";
        final byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        final int i = Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
        System.out.println(i);
    }

    public static int toPositive(int number) {
        return number & 0x7fffffff;
    }

    public static int murmur2(final byte[] data) {
        int length = data.length;
        int seed = 0x9747b28c;
        // 'm' and 'r' are mixing constants generated offline.
        // They're not really 'magic', they just happen to work well.
        final int m = 0x5bd1e995;
        final int r = 24;

        // Initialize the hash to a random value
        int h = seed ^ length;
        int length4 = length / 4;

        for (int i = 0; i < length4; i++) {
            final int i4 = i * 4;
            int k = (data[i4 + 0] & 0xff) + ((data[i4 + 1] & 0xff) << 8) + ((data[i4 + 2] & 0xff) << 16) + ((data[i4 + 3] & 0xff) << 24);
            k *= m;
            k ^= k >>> r;
            k *= m;
            h *= m;
            h ^= k;
        }

        // Handle the last few bytes of the input array
        switch (length % 4) {
            case 3:
                h ^= (data[(length & ~3) + 2] & 0xff) << 16;
            case 2:
                h ^= (data[(length & ~3) + 1] & 0xff) << 8;
            case 1:
                h ^= data[length & ~3] & 0xff;
                h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }

}
