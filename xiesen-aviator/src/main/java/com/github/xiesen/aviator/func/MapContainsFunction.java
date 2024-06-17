package com.github.xiesen.aviator.func;

import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * @author xiesen
 * 外面传过来的字符串是否包含在数据字段中
 * map.contains(dimensions.ip)
 */
public class MapContainsFunction extends AbstractFunction {
    public static final String FUNCTION_NAME = "map.contains";
    Map<String, Object> ips;

    public MapContainsFunction(Map<String, Object> ips) {
        this.ips = ips;
//        Console.log("ips = {}", ips);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject paramName) {
        Object target = FunctionUtils.getStringValue(paramName, env);
//        Console.log("target = {}", target);
        return ips.containsKey(target) ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
    }


    @Override
    public String getName() {
        return FUNCTION_NAME;
    }
}
