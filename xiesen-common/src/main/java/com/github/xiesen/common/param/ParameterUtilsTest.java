package com.github.xiesen.common.param;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * @author xiesen
 * @title: ParameterUtilsTest
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2023/5/5 14:08
 */
public class ParameterUtilsTest {
    public static void main(String[] args) {
        Map<String, String> globalParamMap = new HashMap<>();
        List<Property> globalParamList = new ArrayList<>();
        //define scheduleTime
        Date scheduleTime = DateUtils.stringToDate("2019-12-20 00:00:00");

        //test var $ startsWith
        globalParamMap.put("bizDate", "${system.biz.date}");
        globalParamMap.put("b1zCurdate", "${system.biz.curdate}");

        Property property2 = new Property("testParamList1", Direct.IN, DataType.VARCHAR, "testParamList");
        Property property3 = new Property("testParamList2", Direct.IN, DataType.VARCHAR, "{testParamList1}");
        Property property4 = new Property("testParamList3", Direct.IN, DataType.VARCHAR, "${b1zCurdate}");

        globalParamList.add(property2);
        globalParamList.add(property3);
        globalParamList.add(property4);

        String result5 = ParameterUtils.curingGlobalParams(globalParamMap, globalParamList, CommandType.START_CURRENT_TASK_PROCESS, scheduleTime, null);
        if (result5.equalsIgnoreCase(JSON.toJSONString(globalParamList))) {
            System.out.println("==");
        } else {
            System.out.println("!=");
        }
        String result6 = ParameterUtils.curingGlobalParams(globalParamMap, globalParamList, CommandType.START_CURRENT_TASK_PROCESS, scheduleTime, null);
        System.out.println(result6);
        testConvertParameterPlaceholders();
        testConvertParameterPlaceholders2();

    }


    public static void testConvertParameterPlaceholders() {
        // parameterString,parameterMap is null
        System.out.println(ParameterUtils.convertParameterPlaceholders(null, null));

        // parameterString is null,parameterMap is not null
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("testParameter", "testParameter");
        System.out.println(ParameterUtils.convertParameterPlaceholders(null, parameterMap));

        // parameterString、parameterMap is not null
        String parameterString = "test_parameter";
        System.out.println(ParameterUtils.convertParameterPlaceholders(parameterString, parameterMap));

        //replace variable ${} form
        parameterMap.put("testParameter2", "${testParameter}");
        System.out.println(PlaceholderUtils.replacePlaceholders(parameterString, parameterMap, true));

        // replace time $[...] form, eg. $[yyyyMMdd]
        Date cronTime = new Date();
        System.out.println(TimePlaceholderUtils.replacePlaceholders(parameterString, cronTime, true));

        // replace time $[...] form, eg. $[yyyyMMdd]
        Date cronTimeStr = DateUtils.stringToDate("2019-02-02 00:00:00");
        System.out.println(TimePlaceholderUtils.replacePlaceholders(parameterString, cronTimeStr, true));
    }

    public static void testConvertParameterPlaceholders2() {
        String parameterString =
                "${user} is userName, '$[1]' '$[add_months(yyyyMMdd,12*2)]' '$[add_months(yyyyMMdd,-12*2)]' '$[add_months(yyyyMMdd,3)]' '$[add_months(yyyyMMdd,-4)]' "
                        + "'$[yyyyMMdd+7*2]' '$[yyyyMMdd-7*2]'  '$[yyyyMMdd+3]'  '$[0]' '$[yyyyMMdd-3]' '$[HHmmss+2/24]' '$[HHmmss-1/24]' '$[HHmmss+3/24/60]' '$[HHmmss-2/24/60]'  '$[3]'";
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("user", "Kris");
        parameterMap.put(Constants.PARAMETER_DATETIME, "20201201123000");
        parameterString = ParameterUtils.convertParameterPlaceholders(parameterString, parameterMap);
        System.out.println(parameterString);
    }
}

