package com.github.xiesen.mock.ops;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author xiesen
 */
public class DsResourceDelete {
    public static final String SEARCH_URL = "http://192.168.70.193:12345/dolphinscheduler/resources?id=6&type=FILE&searchVal=&pageNo=1&pageSize=50";
    private static final String DELETE_URL_PREFIX = "http://192.168.70.193:12345/dolphinscheduler/resources/";

    public static void main(String[] args) {
        JSONArray totalList = searchResourceInfo();
        Long num = 0L;
        while (totalList.size() > 0) {
            num++;
            Console.log("第 {} 次查询: totalList.size = {}", num, totalList.size());
            for (Object o : totalList) {
                final JSONObject innerJsonObject = JSON.parseObject(o.toString());
                final Integer id = innerJsonObject.getInteger("id");
                final String result = deleteResource(id);
                Console.log("id = {}; result = {};", id, result);
            }
            totalList = searchResourceInfo();
        }
    }

    private static JSONArray searchResourceInfo() {
        final String body = HttpRequest.get(SEARCH_URL)
                .header("token", "91134bfc18459c8c8c1770dde540018a")
                .timeout(20000).execute().body();

        final JSONObject jsonObject = JSON.parseObject(body);

        final JSONObject data = jsonObject.getJSONObject("data");
        final JSONArray totalList = data.getJSONArray("totalList");
        return totalList;
    }

    private static String deleteResource(Integer id) {
        return HttpRequest.delete(DELETE_URL_PREFIX + id)
                .header("token", "91134bfc18459c8c8c1770dde540018a")
                .timeout(20000).execute().body();
    }
}
