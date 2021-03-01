package com.github.xiesen.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author 谢森
 * @since 2021/3/1
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public RedissonClient getRedisSon() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.1.224:6379").setDatabase(10);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
