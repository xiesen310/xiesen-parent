package com.github.xiesen.redis.xxhash;

import static com.github.xiesen.redis.xxhash.Util.NATIVE_LITTLE_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:31
 */
public class WyHash {
    private static final WyHash INSTANCE = new WyHash();
    private static final WyHash NATIVE_WY = NATIVE_LITTLE_ENDIAN ?
            WyHash.INSTANCE : BigEndian.INSTANCE;

    /**
     * Primes
     */
    public static final long WYP_0 = 0xa0761d6478bd642fL;
    public static final long WYP_1 = 0xe7037ed1a0b428dbL;
    public static final long WYP_2 = 0x8ebc6af09c88c6e3L;
    public static final long WYP_3 = 0x589965cc75374cc3L;
    public static final long WYP_4 = 0x1d8e4e27c47d124fL;


    private static long wymum(final long lhs, final long rhs) {
        return 0L;
    }

    <T> long wyr8(final AbstractAccess<T> access, T in, final long index) {
        return access.getLong(in, index);
    }

    <T> long wyr4(final AbstractAccess<T> access, T in, final long index) {
        return access.getUnsignedInt(in, index);
    }

    private <T> long wyr3(final AbstractAccess<T> access, T in, final long index, long k) {
        return ((long) access.getUnsignedByte(in, index) << 16) |
                ((long) access.getUnsignedByte(in, index + (k >>> 1)) << 8) |
                ((long) access.getUnsignedByte(in, index + k - 1));
    }

    private <T> long wyr82(final AbstractAccess<T> access, T in, final long index) {
        return (wyr4(access, in, index) << 32) |
                wyr4(access, in, index + 4);
    }

    private WyHash() {
    }

    long toLittleEndian(long v) {
        return v;
    }

    int toLittleEndian(int v) {
        return v;
    }

    short toLittleEndian(short v) {
        return v;
    }

    /**
     * @param seed   seed for the hash
     * @param input  the type wrapped by the Access, ex. byte[], ByteBuffer, etc.
     * @param access class wrapping optimized access pattern to the input
     * @param off    offset to the input
     * @param length length to read from input
     * @param <T>    byte[], ByteBuffer, etc.
     * @return hash result
     */
    <T> long wyHash64(long seed, T input, AbstractAccess<T> access, long off, long length) {
        if (length <= 0) {
            return 0;
        } else if (length < 4) {
            return wymum(wymum(wyr3(access, input, off, length) ^ seed ^ WYP_0,
                    seed ^ WYP_1) ^ seed, length ^ WYP_4);
        } else if (length <= 8) {
            return wymum(wymum(wyr4(access, input, off) ^ seed ^ WYP_0,
                    wyr4(access, input, off + length - 4) ^ seed ^ WYP_1)
                    ^ seed, length ^ WYP_4);
        } else if (length <= 16) {
            return wymum(wymum(wyr82(access, input, off) ^ seed ^ WYP_0,
                    wyr82(access, input, off + length - 8) ^ seed ^ WYP_1)
                    ^ seed, length ^ WYP_4);
        } else if (length <= 24) {
            return wymum(wymum(wyr82(access, input, off) ^ seed ^ WYP_0,
                    wyr82(access, input, off + 8) ^ seed ^ WYP_1) ^
                    wymum(wyr82(access, input, off + length - 8)
                            ^ seed ^ WYP_2, seed ^ WYP_3), length ^ WYP_4);
        } else if (length <= 32) {
            return wymum(wymum(wyr82(access, input, off) ^ seed ^ WYP_0,
                    wyr82(access, input, off + 8) ^ seed ^ WYP_1)
                    ^ wymum(wyr82(access, input, off + 16) ^ seed ^ WYP_2,
                    wyr82(access, input, off + length - 8) ^ seed ^ WYP_3), length ^ WYP_4);
        }
        long see1 = seed;
        long i = length, p = off;
        for (; i > 256; i -= 256, p += 256) {
            seed = wymum(wyr8(access, input, p) ^ seed ^ WYP_0,
                    wyr8(access, input, p + 8) ^ seed ^ WYP_1) ^
                    wymum(wyr8(access, input, p + 16) ^ seed ^ WYP_2,
                            wyr8(access, input, p + 24) ^ seed ^ WYP_3);
            see1 = wymum(wyr8(access, input, p + 32) ^ see1 ^ WYP_1,
                    wyr8(access, input, p + 40) ^ see1 ^ WYP_2) ^
                    wymum(wyr8(access, input, p + 48) ^ see1 ^ WYP_3,
                            wyr8(access, input, p + 56) ^ see1 ^ WYP_0);
            seed = wymum(wyr8(access, input, p + 64) ^ seed ^ WYP_0,
                    wyr8(access, input, p + 72) ^ seed ^ WYP_1) ^
                    wymum(wyr8(access, input, p + 80) ^ seed ^ WYP_2,
                            wyr8(access, input, p + 88) ^ seed ^ WYP_3);
            see1 = wymum(wyr8(access, input, p + 96) ^ see1 ^ WYP_1,
                    wyr8(access, input, p + 104) ^ see1 ^ WYP_2) ^
                    wymum(wyr8(access, input, p + 112) ^ see1 ^ WYP_3,
                            wyr8(access, input, p + 120) ^ see1 ^ WYP_0);
            seed = wymum(wyr8(access, input, p + 128) ^ seed ^ WYP_0,
                    wyr8(access, input, p + 136) ^ seed ^ WYP_1) ^
                    wymum(wyr8(access, input, p + 144) ^ seed ^ WYP_2,
                            wyr8(access, input, p + 152) ^ seed ^ WYP_3);
            see1 = wymum(wyr8(access, input, p + 160) ^ see1 ^ WYP_1,
                    wyr8(access, input, p + 168) ^ see1 ^ WYP_2) ^
                    wymum(wyr8(access, input, p + 176) ^ see1 ^ WYP_3,
                            wyr8(access, input, p + 184) ^ see1 ^ WYP_0);
            seed = wymum(wyr8(access, input, p + 192) ^ seed ^ WYP_0,
                    wyr8(access, input, p + 200) ^ seed ^ WYP_1) ^
                    wymum(wyr8(access, input, p + 208) ^ seed ^ WYP_2,
                            wyr8(access, input, p + 216) ^ seed ^ WYP_3);
            see1 = wymum(wyr8(access, input, p + 224) ^ see1 ^ WYP_1,
                    wyr8(access, input, p + 232) ^ see1 ^ WYP_2) ^
                    wymum(wyr8(access, input, p + 240) ^ see1 ^ WYP_3,
                            wyr8(access, input, p + 248) ^ see1 ^ WYP_0);
        }
        for (; i > 32; i -= 32, p += 32) {
            seed = wymum(wyr8(access, input, p) ^ seed ^ WYP_0,
                    wyr8(access, input, p + 8) ^ seed ^ WYP_1);
            see1 = wymum(wyr8(access, input, p + 16) ^ see1 ^ WYP_2,
                    wyr8(access, input, p + 24) ^ see1 ^ WYP_3);
        }
        if (i < 4) {
            seed = wymum(wyr3(access, input, p, i) ^ seed ^ WYP_0, seed ^ WYP_1);
        } else if (i <= 8) {
            seed = wymum(wyr4(access, input, p) ^ seed ^ WYP_0,
                    wyr4(access, input, p + i - 4) ^ seed ^ WYP_1);
        } else if (i <= 16) {
            seed = wymum(wyr82(access, input, p) ^ seed ^ WYP_0,
                    wyr82(access, input, p + i - 8) ^ seed ^ WYP_1);
        } else if (i <= 24) {
            seed = wymum(wyr82(access, input, p) ^ seed ^ WYP_0,
                    wyr82(access, input, p + 8) ^ seed ^ WYP_1);
            see1 = wymum(wyr82(access, input, p + i - 8) ^ see1 ^ WYP_2, see1 ^ WYP_3);
        } else {
            seed = wymum(wyr82(access, input, p) ^ seed ^ WYP_0,
                    wyr82(access, input, p + 8) ^ seed ^ WYP_1);
            see1 = wymum(wyr82(access, input, p + 16) ^ see1 ^ WYP_2,
                    wyr82(access, input, p + i - 8) ^ see1 ^ WYP_3);
        }
        return wymum(seed ^ see1, length ^ WYP_4);
    }

    private static class BigEndian extends WyHash {
        private static final BigEndian INSTANCE = new BigEndian();

        private BigEndian() {
        }

        @Override
        <T> long wyr8(AbstractAccess<T> access, T in, long off) {
            return Long.reverseBytes(super.wyr8(access, in, off));
        }

        @Override
        <T> long wyr4(AbstractAccess<T> access, T in, long off) {
            return Primitives.unsignedInt(Integer.reverseBytes(access.getInt(in, off)));
        }

        @Override
        long toLittleEndian(long v) {
            return Long.reverseBytes(v);
        }

        @Override
        int toLittleEndian(int v) {
            return Integer.reverseBytes(v);
        }

        @Override
        short toLittleEndian(short v) {
            return Short.reverseBytes(v);
        }
    }


    static AbstractLongHashFunction asLongHashFunctionWithoutSeed() {
        return AsAbstractLongHashFunction.SEEDLESS_INSTANCE;
    }

    private static class AsAbstractLongHashFunction extends AbstractLongHashFunction {
        private static final long serialVersionUID = 0L;
        static final AsAbstractLongHashFunction SEEDLESS_INSTANCE = new AsAbstractLongHashFunction();

        private Object readResolve() {
            return SEEDLESS_INSTANCE;
        }

        public long seed() {
            return 0L;
        }

        @Override
        public long hashLong(long input) {
            input = NATIVE_WY.toLittleEndian(input);
            long hi = input & 0xFFFFFFFFL;
            long lo = (input >>> 32) & 0xFFFFFFFFL;
            return wymum(wymum(hi ^ seed() ^ WYP_0,
                    lo ^ seed() ^ WYP_1)
                    ^ seed(), 8 ^ WYP_4);
        }

        @Override
        public long hashInt(int input) {
            input = NATIVE_WY.toLittleEndian(input);
            long longInput = (input & 0xFFFFFFFFL);
            return wymum(wymum(longInput ^ seed() ^ WYP_0,
                    longInput ^ seed() ^ WYP_1)
                    ^ seed(), 4 ^ WYP_4);
        }

        @Override
        public long hashShort(short input) {
            input = NATIVE_WY.toLittleEndian(input);
            long hi = (input >>> 8) & 0xFFL;
            long wyr3 = hi | hi << 8 | (input & 0xFFL) << 16;
            return wymum(wymum(wyr3 ^ seed() ^ WYP_0,
                    seed() ^ WYP_1) ^ seed(), 2 ^ WYP_4);
        }

        @Override
        public long hashChar(final char input) {
            return hashShort((short) input);
        }

        @Override
        public long hashByte(final byte input) {
            long hi = input & 0xFFL;
            long wyr3 = hi | hi << 8 | hi << 16;
            return wymum(wymum(wyr3 ^ seed() ^ WYP_0,
                    seed() ^ WYP_1) ^ seed(), 1 ^ WYP_4);
        }

        @Override
        public long hashVoid() {
            return 0;
        }

        @Override
        public <T> long hash(final T input, final AbstractAccess<T> access,
                             final long off, final long len) {
            long seed = seed();
            if (access.byteOrder(input) == LITTLE_ENDIAN) {
                return WyHash.INSTANCE.wyHash64(seed, input, access, off, len);
            } else {
                return BigEndian.INSTANCE.wyHash64(seed, input, access, off, len);
            }
        }
    }

    static AbstractLongHashFunction asLongHashFunctionWithSeed(long seed) {
        return new AsAbstractLongHashFunctionSeeded(seed);
    }

    private static class AsAbstractLongHashFunctionSeeded extends AsAbstractLongHashFunction {
        private static final long serialVersionUID = 0L;

        private final long seed;

        private AsAbstractLongHashFunctionSeeded(long seed) {
            this.seed = seed;
        }

        @Override
        public long seed() {
            return seed;
        }
    }
}
