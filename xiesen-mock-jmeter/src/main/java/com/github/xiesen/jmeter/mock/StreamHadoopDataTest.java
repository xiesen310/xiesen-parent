package com.github.xiesen.jmeter.mock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.jmeter.util.Producer;
import org.apache.jmeter.samplers.SampleResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟 streamx 告警数据
 * @Email xiesen310@163.com
 * @Date 2020/8/18 10:57
 */
public class StreamHadoopDataTest {

    private static String buildHadoopJson() {
        JSONObject hadoop = new JSONObject();
        JSONObject measures = new JSONObject();
        measures.put("latence", 0.0);
        measures.put("latency", 0.1);
        measures.put("spendtime", 0.2);

        JSONObject normalFields = new JSONObject();
        normalFields.put("indexTime", DateUtil.getUTCTimeStr());
        normalFields.put("bsflag", "unknown");
        normalFields.put("productcode", "unknown");
        normalFields.put("developercode", "unknown");
        normalFields.put("fmillsecond", "585606599");
        normalFields.put("inputtype", "Z");
        normalFields.put("logchecktime", DateUtil.getUTCTimeStr());
        normalFields.put("message", "13811110000-110101199003075517-上海市浦东新区张江微电子港-zorkdata@163" +
                ".com-123456789-wanghaiying123-王海鹰-192.168.1.1-00-50-56-C0-00-08-6227002470170278192-持仓1000万");
        normalFields.put("end_logtime", DateUtil.getUTCTimeStr());
        normalFields.put("smillsecond", "585606599");
        normalFields.put("featurecode", "unknown");
        normalFields.put("authcode", "4402");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        normalFields.put("fundid", "unknown");
        normalFields.put("deserializerTime", DateUtil.getUTCTimeStr());
        normalFields.put("messid", "0000011404342B32233DDCDA");
        normalFields.put("custid", "4402");
        normalFields.put("netaddr", "4402");
        normalFields.put("versioninfo", DateUtil.getUTCTimeStr());
        normalFields.put("authinfo", "unknown");

        String offset = "" + new Random().nextInt(10000);
        String logTypeName = "kcbp_biz_log";
        String source = "d:\\kcbp\\log\\run\\20200918\\runlog23.log";
        String timestamp = DateUtil.getCurrentTimestamp();
        JSONObject dimensions = new JSONObject();
        dimensions.put("appsystem", "jzjy");
        dimensions.put("appprogramname", "jzc9-kcbp1_9600");
        dimensions.put("hostname", "jzc9-kcbp1");
        dimensions.put("func", "jzc9-kcbp1");

        hadoop.put("measures", measures);
        hadoop.put("normalFields", normalFields);
        hadoop.put("offset", offset);
        hadoop.put("logTypeName", logTypeName);
        hadoop.put("source", source);
        hadoop.put("timestamp", timestamp);
        hadoop.put("dimensions", dimensions);


        return hadoop.toJSONString();
    }

    public static void buildHadoopData(SampleResult results, Producer producer, String topicName,
                                       JSONObject jsonObject) {
        String logTypeName = jsonObject.get("logTypeName").toString();
        String source = jsonObject.get("source").toString();
        String offset = jsonObject.get("offset").toString();
        String timestamp = DateUtil.getUTCTimeStr();

        Map<String, Double> measures = new HashMap<>(16);
        measures.put("latence", 0.0);
        measures.put("latency", 0.1);
        measures.put("spendtime", 0.2);

        Map<String, String> dimensions = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("dimensions")),
                Map.class);
        Map<String, String> normalFields = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("normalFields")),
                Map.class);

        normalFields.put("indexTime", DateUtil.getUTCTimeStr());
        normalFields.put("logchecktime", DateUtil.getUTCTimeStr());
        normalFields.put("end_logtime", DateUtil.getUTCTimeStr());
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        normalFields.put("deserializerTime", DateUtil.getUTCTimeStr());


        producer.sendLogAvro(topicName, logTypeName, timestamp, source, offset, dimensions, measures, normalFields);
        results.setResponseCode("0");
        results.setResponseData(jsonObject.toJSONString(), "UTF-8");
        results.setDataType(SampleResult.TEXT);
        results.setSuccessful(true);

    }


    public static void main(String[] args) {
        System.out.println(buildHadoopJson());

    }

}
