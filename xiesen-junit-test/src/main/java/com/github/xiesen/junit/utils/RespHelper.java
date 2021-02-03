package com.github.xiesen.junit.utils;

/**
 * @Description 响应工具类 0: 成功; 1: 失败
 * @className com.zorkdata.smartdata.streamx.web.utils.RespHelper
 * @Author 谢森
 * @Email xiesen@zork.com.cn
 * @Date 2020/2/26 11:15
 */
public class RespHelper {
    public static <T> RespDto<T> createEmptyRespDto() {
        return new RespDto<>();
    }

    public static <T> RespDto<T> fail(int code) {
        RespDto<T> t = createEmptyRespDto();
        t.setCode(code);
        t.setMessage(null);
        return t;
    }

    public static <T> RespDto<T> fail(int code, String message) {
        RespDto<T> t = createEmptyRespDto();
        t.setCode(code);
        t.setMessage(message);
        return t;
    }

    public static <T> RespDto<T> fail(int code, T data, String message) {
        RespDto<T> t = createEmptyRespDto();
        t.setCode(code);
        t.setData(data);
        t.setMessage(message);
        return t;
    }

    public static <T> RespDto<T> ok(T data) {
        RespDto<T> t = createEmptyRespDto();
        t.setCode(0);
        t.setData(data);
        return t;
    }

    public static <T> RespDto<T> ok(boolean flag) {
        RespDto<T> t = createEmptyRespDto();
        if (flag) {
            t.setCode(0);
            t.setMessage("成功");
        } else {
            t.setCode(1);
            t.setMessage("失败");
        }
        return t;
    }

    public static <T> RespDto<T> ok(T data, String message) {
        RespDto<T> t = createEmptyRespDto();
        t.setCode(0);
        t.setData(data);
        t.setMessage(message);
        return t;
    }
}
