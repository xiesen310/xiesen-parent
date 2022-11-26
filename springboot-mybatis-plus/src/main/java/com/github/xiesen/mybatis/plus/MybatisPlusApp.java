package com.github.xiesen.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiesen
 * @title: MybatisPlusApp
 * @projectName xiesen-parent
 * @description: MybatisPlusApp
 * @date 2022/11/26 20:52
 */
@SpringBootApplication
@MapperScan("com.github.xiesen.mybatis.plus.mapper")
public class MybatisPlusApp {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApp.class,args);
    }
}
