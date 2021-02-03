package com.github.xiesen.junit.utils;

import lombok.Data;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/27 12:37
 */
@Data
public class RespDto<T> {
    /**
     * 错误码
     */
    public int code;
    /**
     * 错误描述
     */
    public String message;

    /**
     * 此项当有值时才会返回
     */
    private T data;
}
