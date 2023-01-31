package com.github.xiesen.redis.xxhash;

import java.lang.reflect.Field;

/**
 * @author xiese
 * @Description ModernHotSpotStringHash
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:13
 */
enum ModernHotSpotStringHash implements StringHash {

    /**
     * INSTANCE
     */
    INSTANCE;

    private static final long VALUE_OFFSET;

    static {
        try {
            Field valueField = String.class.getDeclaredField("value");
            VALUE_OFFSET = UnsafeAbstractAccess.UNSAFE.objectFieldOffset(valueField);
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public long longHash(String s, AbstractLongHashFunction hashFunction, int off, int len) {
        char[] value = (char[]) UnsafeAbstractAccess.UNSAFE.getObject(s, VALUE_OFFSET);
        return hashFunction.hashChars(value, off, len);
    }

    @Override
    public void hash(final String s, final AbstractLongTupleHashFunction hashFunction,
                     final int off, final int len, final long[] result) {
        final char[] value = (char[]) UnsafeAbstractAccess.UNSAFE.getObject(s, VALUE_OFFSET);
        hashFunction.hashChars(value, off, len, result);
    }
}
