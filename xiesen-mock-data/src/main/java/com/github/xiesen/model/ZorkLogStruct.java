package com.github.xiesen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author：hubinbin
 * @date：2022/6/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ZorkLogStruct implements Serializable{
    /**
     * 创建时间
     * 用于做时间范围查询
     * "2018-09-15T12:21:28.370+08:00"
     */
    @NonNull
    private String timestamp;

    /**
     * logTypeName: es 日志类型的标识,即 "_type"
     */
    private String logTypeName;

    @NonNull
    private int offset;
}
