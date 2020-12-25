package com.github.xiesen.junit.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/12/24 15:02
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ZorkDataMap implements Serializable {
    private static final long serialVersionUID = 2943421317428493346L;
    private Map<String, Object> map;
}