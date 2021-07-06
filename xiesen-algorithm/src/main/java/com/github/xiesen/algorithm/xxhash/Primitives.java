package com.github.xiesen.algorithm.xxhash;

/**
 * @author xiese
 * @Description Primitives
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:16
 */
final class Primitives {

    private Primitives() {
    }

    static long unsignedInt(int i) {
        return i & 0xFFFFFFFFL;
    }

    static int unsignedShort(int s) {
        return s & 0xFFFF;
    }

    static int unsignedByte(int b) {
        return b & 0xFF;
    }
}
