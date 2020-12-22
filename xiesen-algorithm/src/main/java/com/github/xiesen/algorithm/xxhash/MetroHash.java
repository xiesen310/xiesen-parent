package com.github.xiesen.algorithm.xxhash;

import static com.github.xiesen.algorithm.xxhash.Util.NATIVE_LITTLE_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * @author xiese
 * @Description MetroHash
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:32
 */
public class MetroHash {
    private static final MetroHash INSTANCE = new MetroHash();
    private static final MetroHash NATIVE_METRO = NATIVE_LITTLE_ENDIAN ?
            MetroHash.INSTANCE : BigEndian.INSTANCE;

    /**
     * primes
     */
    private static final long K_0 = 0xD6D018F5L;
    private static final long K_1 = 0xA2AA033BL;
    private static final long K_2 = 0x62992FC1L;
    private static final long K_3 = 0x30BC5B29L;

    <T> long fetch64(AbstractAccess<T> access, T in, long off) {
        return access.getLong(in, off);
    }

    <T> long fetch32(AbstractAccess<T> access, T in, long off) {
        return access.getUnsignedInt(in, off);
    }

    <T> long fetch16(AbstractAccess<T> access, T in, long off) {
        return access.getUnsignedShort(in, off);
    }

    <T> int fetch8(AbstractAccess<T> access, T in, long off) {
        return access.getUnsignedByte(in, off);
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


    <T> long metroHash64(long seed, T input, AbstractAccess<T> access, long off, long length) {
        long remaining = length;

        long h = (seed + K_2) * K_0;

        if (length >= 32) {
            long v0 = h;
            long v1 = h;
            long v2 = h;
            long v3 = h;

            do {
                v0 += fetch64(access, input, off) * K_0;
                v0 = Long.rotateRight(v0, 29) + v2;
                v1 += fetch64(access, input, off + 8) * K_1;
                v1 = Long.rotateRight(v1, 29) + v3;
                v2 += fetch64(access, input, off + 16) * K_2;
                v2 = Long.rotateRight(v2, 29) + v0;
                v3 += fetch64(access, input, off + 24) * K_3;
                v3 = Long.rotateRight(v3, 29) + v1;

                off += 32;
                remaining -= 32;
            } while (remaining >= 32);

            v2 ^= Long.rotateRight(((v0 + v3) * K_0) + v1, 37) * K_1;
            v3 ^= Long.rotateRight(((v1 + v2) * K_1) + v0, 37) * K_0;
            v0 ^= Long.rotateRight(((v0 + v2) * K_0) + v3, 37) * K_1;
            v1 ^= Long.rotateRight(((v1 + v3) * K_1) + v2, 37) * K_0;

            h += v0 ^ v1;
        }

        if (remaining >= 16) {
            long v0 = h + (fetch64(access, input, off) * K_2);
            v0 = Long.rotateRight(v0, 29) * K_3;
            long v1 = h + (fetch64(access, input, off + 8) * K_2);
            v1 = Long.rotateRight(v1, 29) * K_3;
            v0 ^= Long.rotateRight(v0 * K_0, 21) + v1;
            v1 ^= Long.rotateRight(v1 * K_3, 21) + v0;
            h += v1;

            off += 16;
            remaining -= 16;
        }

        if (remaining >= 8) {
            h += fetch64(access, input, off) * K_3;
            h ^= Long.rotateRight(h, 55) * K_1;

            off += 8;
            remaining -= 8;
        }

        if (remaining >= 4) {
            h += fetch32(access, input, off) * K_3;
            h ^= Long.rotateRight(h, 26) * K_1;

            off += 4;
            remaining -= 4;
        }

        if (remaining >= 2) {
            h += fetch16(access, input, off) * K_3;
            h ^= Long.rotateRight(h, 48) * K_1;

            off += 2;
            remaining -= 2;
        }

        if (remaining >= 1) {
            h += fetch8(access, input, off) * K_3;
            h ^= Long.rotateRight(h, 37) * K_1;
        }

        return finalize(h);
    }

    private static long finalize(long h) {
        h ^= Long.rotateRight(h, 28);
        h *= K_0;
        h ^= Long.rotateRight(h, 29);
        return h;
    }

    private static class BigEndian extends MetroHash {
        private static final BigEndian INSTANCE = new BigEndian();

        private BigEndian() {
        }

        @Override
        <T> long fetch64(AbstractAccess<T> access, T in, long off) {
            return Long.reverseBytes(super.fetch64(access, in, off));
        }

        @Override
        <T> long fetch32(AbstractAccess<T> access, T in, long off) {
            return Integer.reverseBytes(access.getInt(in, off)) & 0xFFFFFFFFL;
        }

        @Override
        <T> long fetch16(AbstractAccess<T> access, T in, long off) {
            return Short.reverseBytes((short) access.getShort(in, off)) & 0xFFFFL;
        }

        @Override
        <T> int fetch8(AbstractAccess<T> access, T in, long off) {
            return super.fetch8(access, in, off);
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

    private static class AsAbstractLongHashFunction extends AbstractLongHashFunction {
        private static final long serialVersionUID = 0L;
        private static final AsAbstractLongHashFunction SEEDLESS_INSTANCE = new AsAbstractLongHashFunction();
        private static final long VOID_HASH = MetroHash.finalize(K_2 * K_0);

        private Object readResolve() {
            return SEEDLESS_INSTANCE;
        }

        protected long seed() {
            return 0L;
        }

        @Override
        public long hashLong(long input) {
            input = NATIVE_METRO.toLittleEndian(input);
            long h = (seed() + K_2) * K_0;
            h += input * K_3;
            h ^= Long.rotateRight(h, 55) * K_1;

            return MetroHash.finalize(h);
        }

        @Override
        public long hashInt(int input) {
            input = NATIVE_METRO.toLittleEndian(input);
            long h = (seed() + K_2) * K_0;
            h += Primitives.unsignedInt(input) * K_3;
            h ^= Long.rotateRight(h, 26) * K_1;

            return MetroHash.finalize(h);
        }

        @Override
        public long hashShort(short input) {
            input = NATIVE_METRO.toLittleEndian(input);
            long h = (seed() + K_2) * K_0;
            h += Primitives.unsignedShort(input) * K_3;
            h ^= Long.rotateRight(h, 48) * K_1;

            return MetroHash.finalize(h);
        }

        @Override
        public long hashChar(char input) {
            return hashShort((short) input);
        }

        @Override
        public long hashByte(byte input) {
            long h = (seed() + K_2) * K_0;
            h += Primitives.unsignedByte(input) * K_3;
            h ^= Long.rotateRight(h, 37) * K_1;

            return MetroHash.finalize(h);
        }

        @Override
        public long hashVoid() {
            return VOID_HASH;
        }

        @Override
        public <T> long hash(T input, AbstractAccess<T> access, long off, long len) {
            long seed = seed();
            if (access.byteOrder(input) == LITTLE_ENDIAN) {
                return MetroHash.INSTANCE.metroHash64(seed, input, access, off, len);
            } else {
                return BigEndian.INSTANCE.metroHash64(seed, input, access, off, len);
            }
        }
    }

    static AbstractLongHashFunction asLongHashFunctionWithoutSeed() {
        return AsAbstractLongHashFunction.SEEDLESS_INSTANCE;
    }

    static AbstractLongHashFunction asLongHashFunctionWithSeed(long seed) {
        return new AsAbstractLongHashFunctionSeeded(seed);
    }

    private static class AsAbstractLongHashFunctionSeeded extends AsAbstractLongHashFunction {
        private static final long serialVersionUID = 0L;

        private final long seed;
        private final transient long voidHash;

        AsAbstractLongHashFunctionSeeded(long seed) {
            this.seed = seed;
            voidHash = MetroHash.finalize((seed + K_2) * K_0);
        }

        @Override
        public long hashVoid() {
            return voidHash;
        }

        @Override
        protected long seed() {
            return seed;
        }
    }
}
