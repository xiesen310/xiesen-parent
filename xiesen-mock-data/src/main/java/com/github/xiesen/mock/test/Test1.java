package com.github.xiesen.mock.test;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.StrUtil;

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

        System.out.println("开始时间: " + (System.currentTimeMillis() - 5 * 60 * 1000));
        System.out.println("结束时间: " + System.currentTimeMillis());

        String a = "2023-04-17T18:27:00+08:00";
        final int t = StrUtil.indexOf(a, 'T');
        final String s = StrUtil.sub(a, t + 1, t + 1 + 8);

        System.out.println(s);

        System.out.println("aaaa\n\rbbbb");
    }
}
