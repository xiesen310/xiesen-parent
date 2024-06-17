package com.github.xiesen.aviator.func;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author xiesen
 * 外面传过来的字符串是否包含在数据字段中
 * str.contains(dimensions.ip)
 * @date 2022/10/26 19:21
 */
public class StrContainsFunction extends AbstractFunction {
    public static final String FUNCTION_NAME = "str.contains";
    String ips;

    public StrContainsFunction(String ips) {
        this.ips = ips;
//        Console.log("ips = {}", ips);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject paramName) {
        String target = FunctionUtils.getStringValue(paramName, env);
//        Console.log("target = {}", target);
        return ips.contains(target) ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
    }


    @Override
    public String getName() {
        return FUNCTION_NAME;
    }
}
