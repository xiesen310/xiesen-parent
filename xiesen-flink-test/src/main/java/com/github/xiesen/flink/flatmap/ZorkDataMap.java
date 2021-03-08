package com.github.xiesen.flink.flatmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/3/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ZorkDataMap implements Serializable {
    private static final long serialVersionUID = 2943421317428493346L;
    private Map<String, Object> map;
}
