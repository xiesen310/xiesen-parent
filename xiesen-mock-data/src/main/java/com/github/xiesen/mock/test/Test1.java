package com.github.xiesen.mock.test;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;

/**
 * @author xiesen
 * @title: Test1
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/7/29 15:52
 */
public class Test1 {
    public static void main(String[] args) {
        Cache<String, String> lruCache = CacheUtil.newLRUCache(3);
        //通过实例化对象创建
        //LRUCache<String, String> lruCache = new LRUCache<String, String>(3);
        lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        lruCache.get("key1");//使用时间推近
        lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        //由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
        String value2 = lruCache.get("key");//null

    }
}
