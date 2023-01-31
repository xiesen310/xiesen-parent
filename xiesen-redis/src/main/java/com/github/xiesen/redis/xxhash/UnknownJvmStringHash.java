package com.github.xiesen.redis.xxhash;


/**
 * @author xiese
 * @Description UnknownJvmStringHash
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:08
 */
enum UnknownJvmStringHash implements StringHash {
    /**
     * INSTANCE
     */
    INSTANCE;

    @Override
    public long longHash(String s, AbstractLongHashFunction hashFunction, int off, int len) {
        return hashFunction.hashNativeChars(s, off, len);
    }

    @Override
    public void hash(final String s, final AbstractLongTupleHashFunction hashFunction,
                     final int off, final int len, final long[] result) {
        AbstractLongTupleHashFunction.hashNativeChars(hashFunction, s, off, len, result);
    }
}
