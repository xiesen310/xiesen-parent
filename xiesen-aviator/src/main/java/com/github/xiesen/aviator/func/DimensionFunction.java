package com.github.xiesen.aviator.func;

import cn.hutool.core.map.MapUtil;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * @author xiesen
 * @title: EqualsIgnoreCaseFunction
 * @projectName xiesen-parent
 * @description: 获取 dimensions 中的值
 * *
 * @date 2022/10/26 19:21
 */
public class DimensionFunction extends AbstractFunction {

    public static final String FUNCTION_NAME = "dim";
    public static final String DIMENSIONS_STR = "dimensions";

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject paramName) {
        Map<String, String> dimensions = MapUtil.get(env, DIMENSIONS_STR, Map.class);
        String paramValue = MapUtil.getStr(dimensions, paramName.getValue(env));
        return new AviatorString(paramValue);
    }

    @Override
    public String getName() {
        return FUNCTION_NAME;
    }
}
