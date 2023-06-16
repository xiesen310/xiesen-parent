package com.github.xiesen.ds;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 删除 dolphin 资源文件
 *
 * @author xiesen
 */
public class DeleteDsResource {
    public static final String url = "http://192.168.70.193:12345/dolphinscheduler/resources";
    public static final String TOKEN = "91134bfc18459c8c8c1770dde540018a";

    public static void main(String[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", 6);
        paramMap.put("type", "FILE");
        paramMap.put("searchVal", "");
        paramMap.put("pageNo", 1);
        paramMap.put("pageSize", 50);
        final HttpResponse response = HttpUtil.createGet(url).header("token", TOKEN).form(paramMap).timeout(30000).execute();
        final String body = response.body();
        Integer total = 0;
        if (!body.isEmpty()) {
            final JSONObject jsonObject = JSON.parseObject(body, JSONObject.class);
            final JSONObject data = jsonObject.getJSONObject("data");
            total = data.getInteger("total");
            System.out.println(total);
        }
        System.out.println(response);

        Set<Integer> ids = new HashSet<>();
        if (total != 0) {
            paramMap.put("pageSize", total);

            final HttpResponse httpResponse = HttpUtil.createGet(url).header("token", TOKEN).form(paramMap).timeout(30000).execute();
            final String body1 = httpResponse.body();
            if (!body1.isEmpty()) {
                final JSONObject jsonObject = JSON.parseObject(body, JSONObject.class);
                final JSONObject data = jsonObject.getJSONObject("data");
                final JSONArray totalList = data.getJSONArray("totalList");
                for (Object o : totalList) {
                    final JSONObject object = JSON.parseObject(o.toString(), JSONObject.class);
                    final Integer id = object.getInteger("id");
                    ids.add(id);
                }

            }
        }


        ids.forEach(id -> {
            String deleteUrl = "http://192.168.70.193:12345/dolphinscheduler/resources/" + id;
            final HttpResponse execute = HttpUtil.createRequest(Method.DELETE, deleteUrl).header("token", TOKEN).timeout(30000).execute();
            if (!execute.body().isEmpty()) {
                final JSONObject jsonObject = JSON.parseObject(execute.body(), JSONObject.class);
                final String msg = jsonObject.getString("msg");
                Console.log(deleteUrl + " " + msg);
            }
        });

    }
}
