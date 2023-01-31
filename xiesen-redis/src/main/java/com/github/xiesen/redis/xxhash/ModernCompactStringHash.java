package com.github.xiesen.redis.xxhash;

import java.lang.reflect.Field;

import static com.github.xiesen.redis.xxhash.Util.checkArrayOffs;


/**
 * @author xiese
 * @Description ModernCompactStringHash
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:13
 */
enum ModernCompactStringHash implements StringHash {
    /**
     * INSTANCE
     */
    INSTANCE;

    private static final long VALUE_OFFSET;
    private static final boolean ENABLE_COMPACT_STRINGS;
    private static final AbstractAccess<byte[]> COMPACT_LATIN_1_ACCESS
            = CompactLatin1CharSequenceAbstractAccess.INSTANCE;

    static {
        try {
            final Field valueField = String.class.getDeclaredField("value");
            VALUE_OFFSET = UnsafeAbstractAccess.UNSAFE.objectFieldOffset(valueField);

            final byte[] value = (byte[]) UnsafeAbstractAccess.UNSAFE.getObject("A", VALUE_OFFSET);
            ENABLE_COMPACT_STRINGS = (1 == value.length);
        } catch (final NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public long longHash(final String s, final AbstractLongHashFunction hashFunction,
                         final int off, final int len) {
        final int sl = s.length();
        if (len <= 0 || sl <= 0) {
            checkArrayOffs(sl, off, len);
            return hashFunction.hashVoid();
        } else {
            final byte[] value = (byte[]) UnsafeAbstractAccess.UNSAFE.getObject(s, VALUE_OFFSET);
            if (ENABLE_COMPACT_STRINGS && sl == value.length) {
                checkArrayOffs(sl, off, len);
                // 'off' and 'len' are passed as bytes
                return hashFunction.hash(value, COMPACT_LATIN_1_ACCESS, (long) off * 2L, (long) len * 2L);
            } else {
                return hashFunction.hashBytes(value, off * 2, len * 2);
            }
        }
    }

    @Override
    public void hash(final String s, final AbstractLongTupleHashFunction hashFunction,
                     final int off, final int len, final long[] result) {
        final int sl = s.length();
        if (len <= 0 || sl <= 0) {
            checkArrayOffs(sl, off, len);
            hashFunction.hashVoid(result);
        } else {
            final byte[] value = (byte[]) UnsafeAbstractAccess.UNSAFE.getObject(s, VALUE_OFFSET);
            if (ENABLE_COMPACT_STRINGS && sl == value.length) {
                checkArrayOffs(sl, off, len);
                // 'off' and 'len' are passed as bytes
                hashFunction.hash(value, COMPACT_LATIN_1_ACCESS, (long) off * 2L, (long) len * 2L, result);
            } else {
                hashFunction.hashBytes(value, off * 2, len * 2, result);
            }
        }
    }
}
