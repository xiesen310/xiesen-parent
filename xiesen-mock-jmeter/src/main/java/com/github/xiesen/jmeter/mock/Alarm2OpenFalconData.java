package com.github.xiesen.jmeter.mock;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.jmeter.util.Producer;
import org.apache.jmeter.samplers.SampleResult;

import java.util.Random;

/**
 * @author 谢森
 * @Description 模拟 jstorm 告警发送数据
 * @Email xiesen310@163.com
 * @Date 2020/11/9 10:25
 */
public class Alarm2OpenFalconData {


    /**
     * {"country_code":"AD","name":"linda","age":30}
     *
     * @return
     */
    private static String buildData() {
        Random random = new Random();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("age", random.nextInt(90));
        return jsonObject.toJSONString();
    }

    /**
     * 模拟 json 数据
     *
     * @param results
     * @param producer
     * @param topicName
     */
    public static void buildAlarm2OpenFalcon(SampleResult results, Producer producer, String topicName) {
        producer.sendJson(topicName, buildData());
        results.setResponseCode("0");
        results.setResponseData(buildData(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);
    }

    public static void main(String[] args) {
        System.out.println(buildData());
    }
}
