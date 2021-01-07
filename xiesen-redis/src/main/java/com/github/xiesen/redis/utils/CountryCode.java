package com.github.xiesen.redis.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 谢森
 * @Description Model
 * @Email xiesen310@163.com
 * @Date 2021/1/7 9:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryCode {
    private String en_name;
    private String ch_name;
    private String code;
    private int tel_code;
    private int time_diff;
}
