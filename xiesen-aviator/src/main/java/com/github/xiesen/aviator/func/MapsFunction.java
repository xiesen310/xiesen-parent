package com.github.xiesen.aviator.func;

import cn.hutool.core.map.MapUtil;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * @author xiesen
 * @title: EqualsIgnoreCaseFunction
 * @projectName xiesen-parent
 * @description: 从 map 中获取值,使用方式:
 * * maps(字段名称) --> 返回值
 * * maps('metricsetname')=='streamx_metric_cpu1'
 * * maps('dimensions.appsystem')=='streamx'
 * @date 2022/10/26 19:21
 */
public class MapsFunction extends AbstractFunction {
    public static final String FUNCTION_NAME = "maps";
    public static final String POINT_SEPARATOR = "\\.";

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject paramName) {
        String param = paramName.getValue(env).toString();
        String[] split = param.split(POINT_SEPARATOR);
        if (split.length == 1) {
            String paramValue = MapUtil.getStr(env, param);
            return new AviatorString(paramValue);
        } else if (split.length == 2) {
            Map<String, String> map = MapUtil.get(env, split[0], Map.class);
            String paramValue = MapUtil.getStr(map, split[1]);
            return new AviatorString(paramValue);
        } else {
            System.out.println("不支持这种语法: " + param);
            return new AviatorString(null);
        }
    }


    @Override
    public String getName() {
        return FUNCTION_NAME;
    }
}
