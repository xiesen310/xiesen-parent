package com.github.xiesen.jmeter.mock;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.jmeter.util.Producer;
import org.apache.jmeter.samplers.SampleResult;

import java.util.Random;

/**
 * @author xiese
 * @Description 模拟 streamx json 格式数据
 * @Email xiesen310@163.com
 * @Date 2020/10/13 10:18
 */
public class StreamJsonData {

    /**
     * @赵飞(赵飞/10059) @靳亚杰(靳亚杰/10068) @王慎芝(王慎芝/w90001) 王慎芝
     */
    private static String[] NAMES = {
            "王海鹰", "王振", "张瑞", "曾雨", "朱云伟", "李幸", "刘鹏程", "徐云川", "冷凯", "胡苗青",
            "汪敏", "靳亚杰", "赵飞", "王慎芝", "刘旺", "谢海清", "宋志鹏", "崔武", "王伟", "王珂",
            "邵娇", "荣权", "胡子玥", "廖鸣韬", "朱志刚", "杜凯", "张畅", "张亮", "张芳芳", "谷俊岭",
    };

    private static String[] COUNTRY_CODES = {
            "AO", "AF", "AL", "DZ", "AD", "AI", "AG", "AR", "AM", "AU",
            "AT", "AZ", "BS", "BH", "BD", "BB", "BY", "BE", "BZ", "BJ"
    };

    /**
     * 随机获取名称
     *
     * @return
     */
    private static String getRandomName() {
        return NAMES[new Random(NAMES.length).nextInt(NAMES.length)];
    }

    /**
     * 随机获取国家编码
     *
     * @return
     */
    private static String getRandomCountryCode() {
        return COUNTRY_CODES[new Random(COUNTRY_CODES.length).nextInt(COUNTRY_CODES.length)];
    }

    /**
     * {"country_code":"AD","name":"linda","age":30}
     *
     * @return
     */
    private static String buildStreamJson() {
        Random random = new Random();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("country_code", getRandomCountryCode());
        jsonObject.put("name", getRandomName());
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
    public static void buildStreamJson(SampleResult results, Producer producer, String topicName) {
        producer.sendJson(topicName, buildStreamJson());
        results.setResponseCode("0");
        results.setResponseData(buildStreamJson(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);
    }

    public static void main(String[] args) {
        System.out.println(buildStreamJson());
    }
}
