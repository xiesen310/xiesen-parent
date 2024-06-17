package com.github.xiesen.aviator.func;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.List;
import java.util.Map;

/**
 * @author xiesen
 * 外面传过来的字符串是否包含在数据字段中
 * list.contains(dimensions.ip)
 */
public class ListContainsFunction extends AbstractFunction {
    public static final String FUNCTION_NAME = "list.contains";
    List<String> ips;

    public ListContainsFunction(List<String> ips) {
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
