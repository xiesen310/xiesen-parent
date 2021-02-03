package com.github.xiesen.str;

import lombok.Data;

/**
 * @author 谢森
 * @since 2021/2/2
 */
@Data
public class Person {
    private Integer id;
    private String name;

    public Person(String name) {
        this.name = name;
    }
}
