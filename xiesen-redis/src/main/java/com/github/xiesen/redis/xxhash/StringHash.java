package com.github.xiesen.redis.xxhash;

/**
 * @author xiese
 * @Description StringHash
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:09
 */


interface StringHash {
    /**
     * longHash
     *
     * @param s
     * @param hashFunction
     * @param off
     * @param len
     * @return
     */
    long longHash(String s, AbstractLongHashFunction hashFunction, int off, int len);

    /**
     * hash
     *
     * @param s
     * @param hashFunction
     * @param off
     * @param len
     * @param result
     */
    void hash(String s, AbstractLongTupleHashFunction hashFunction, int off, int len, long[] result);
}
