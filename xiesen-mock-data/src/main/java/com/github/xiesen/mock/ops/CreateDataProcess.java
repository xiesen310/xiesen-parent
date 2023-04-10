package com.github.xiesen.mock.ops;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 创建数据加工*
 *
 * @author xiesen
 */
public class CreateDataProcess {
    public static final String CREATE_DATA_PROCESS_URL = "http://localhost:8888/metric/process/add";
    public static final String CREATE_DATA_PROCESS_PARAM = "{\n" +
            "    \"sourceName\":\"cpu_system_mb\",\n" +
            "    \"sinkName\":\"cpu_norm_pct2\",\n" +
            "    \"sinkDimensions\": [\"hostname\",\"ip\",\"appsystem\"],\n" +
            "    \"windowTime\": \"60 sec\",\n" +
            "    \"metricCalculation\":\"metricsetname as metricsetname,avg(used_pct) as value,appsystem as appsystem,hostname as hostname, ip as ip\",\n" +
            "    \"queryCondition\":\"\",\n" +
            "    \"action\":\"start\",\n" +
            "    \"groupByKeys\": [\"hostname\",\"ip\",\"appsystem\"]\n" +
            "}";

    public static final String METRIC_SET_PREFIX = "memory_system_mb_";
    public static final String METRIC_DEFINE_PREFIX = "cpu_used_pct_";

    public static void main(String[] args) {
        for (int i = 500; i < 1000; i++) {
            final JSONObject jsonObject = JSON.parseObject(CREATE_DATA_PROCESS_PARAM);
            jsonObject.getString("sourceName");
            jsonObject.put("sourceName", METRIC_SET_PREFIX + i);
            jsonObject.put("sinkName", METRIC_DEFINE_PREFIX + i);
            final HttpResponse execute = HttpUtil.createPost(CREATE_DATA_PROCESS_URL).body(jsonObject.toJSONString())
                    .header("Authorization", "Bearer e3df8d92-d02b-4b32-8c31-51ac14ff7eb4").execute();
            System.out.println(execute);
        }
    }
}
