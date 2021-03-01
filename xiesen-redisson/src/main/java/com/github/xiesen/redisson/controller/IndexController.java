package com.github.xiesen.redisson.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 谢森
 * @since 2021/3/1
 */
@RestController
public class IndexController {

    @Autowired
    private RedissonClient redissonClient;


    @Autowired
    private StringRedisTemplate template;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/stock")
    public String getStock() {
        String goodsId = "product_001";
        RLock lock = redissonClient.getLock(goodsId);
        try {
            lock.lock();
            Integer stock = Integer.valueOf(template.opsForValue().get("stock"));
            if (stock > 0) {
                stock = stock - 1;
                template.opsForValue().set("stock", String.valueOf(stock));
                System.out.println("剩余库存: " + stock);
            } else {
                System.out.println("库存不足");
            }
        } finally {
            lock.unlock();
        }

        return "end";
    }


}
