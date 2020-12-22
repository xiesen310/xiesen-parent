package com.github.xiesen.algorithm.xxhash;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;

/**
 * @author xiese
 * @Description HotSpotPrior7u6StringHash
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:12
 */
@ParametersAreNonnullByDefault
enum HotSpotPrior7u6StringHash implements StringHash {
    /**
     * INSTANCE
     */
    INSTANCE;


    private static final long VALUE_OFFSET;
    private static final long OFFSET_OFFSET;

    static {
        try {
            Field valueField = String.class.getDeclaredField("value");
            VALUE_OFFSET = UnsafeAbstractAccess.UNSAFE.objectFieldOffset(valueField);

            Field offsetField = String.class.getDeclaredField("offset");
            OFFSET_OFFSET = UnsafeAbstractAccess.UNSAFE.objectFieldOffset(offsetField);
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public long longHash(String s, AbstractLongHashFunction hashFunction, int off, int len) {
        char[] value = (char[]) UnsafeAbstractAccess.UNSAFE.getObject(s, VALUE_OFFSET);
        int offset = UnsafeAbstractAccess.UNSAFE.getInt(s, OFFSET_OFFSET);
        return hashFunction.hashChars(value, offset + off, len);
    }

    @Override
    public void hash(final String s, final AbstractLongTupleHashFunction hashFunction,
                     final int off, final int len, final long[] result) {
        final char[] value = (char[]) UnsafeAbstractAccess.UNSAFE.getObject(s, VALUE_OFFSET);
        final int offset = UnsafeAbstractAccess.UNSAFE.getInt(s, OFFSET_OFFSET);
        hashFunction.hashChars(value, offset + off, len, result);
    }
}
