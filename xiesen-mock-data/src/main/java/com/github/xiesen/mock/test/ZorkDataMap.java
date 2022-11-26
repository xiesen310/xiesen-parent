package com.github.xiesen.mock.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author xiesen
 * @title: ZorkDataMap
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/20 10:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ZorkDataMap implements Serializable {
    private static final long serialVersionUID = 2943421317428493346L;
    private Map<String, Object> map;
}
