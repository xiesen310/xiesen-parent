package com.github.xiesen.algorithm;

import com.github.xiesen.algorithm.constants.AlarmConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @Description test
 * @Email xiesen310@163.com
 * @Date 2020/12/18 19:21
 */
public class TestEsId {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> extFields = new HashMap<>();
        extFields.put(AlarmConstants.EXPRESSION_ID, "e_30_newAlarm_30_990bdbf97a9bb9263ca1dd417efe7bc6");
        extFields.put(AlarmConstants.ALARM_TIME, "2020-12-18T19:10:30.000+08:00");

        Map<String, String> sources = new HashMap<>();
        sources.put(AlarmConstants.HOSTNAME, "zhangtest16");
        sources.put(AlarmConstants.APP_SYSTEM, "CEZY");
//        sources.put(AlarmConstants.IP, "");


        map.put(AlarmConstants.TIMESTAMP, "2020-12-18T19:10:30.000+08:00");
        map.put(AlarmConstants.ALARM_TYPE_NAME, "alarm_metric");
        map.put(AlarmConstants.STATUS, "PROBLEM");
        map.put(AlarmConstants.EXT_FIELDS, extFields);

        map.put(AlarmConstants.SOURCES, sources);

        System.out.println(EsIdGenerate.xxHashAlarm2es(map));
    }
}
