package org.xiesen;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @author xiesen
 */
public class Test10 {
    public static final String TOPO_URL = "http://192.168.70.21:18888/data/map/topo";
    public static final List<String> apps = Arrays.asList("客户邮件发送系统", "全连接办公服务模块", "全连接PC门户模块",
            "全连接企业数字化运作系统", "全连接APP中台子系统模块", "全连接流程引擎模块", "全连接财富管理模块", "全连接可视化流程引擎模块", "邮件系统",
            "邮件水印跟踪系统", "君弘中台子系统", "全连接看见新闻模块");

    public static void main(String[] args) {
        Set<String> iconType = new HashSet<>();
        for (String app : apps) {
            String param = "app=" + app;
            getIconType(param, iconType);
        }

        for (String s : iconType) {
            System.out.println("icon = " + s);
        }
    }

    private static String getIconType(String param, Set<String> iconType) {
        final HttpResponse httpResponse = HttpUtil.createGet(TOPO_URL).body(param).execute();
        final String body = httpResponse.body();
        final JSONObject jsonObject = JSON.parseObject(body);
        final Long code = jsonObject.getLong("code");
        if (0 == code) {
            final JSONObject data = jsonObject.getJSONObject("data");
            final JSONArray nodes = data.getJSONArray("nodes");
            for (Object node : nodes) {
                final JSONObject nodeJsonObject = JSON.parseObject(node.toString());
                final String nodeLabel = nodeJsonObject.getString("nodelabel");
                final String type = nodeJsonObject.getString("type");
                final String img = nodeJsonObject.getString("img");
//                iconType.add(type + "#" + img);
                iconType.add(img);
//                Console.log("nodelabel: {}; type: {}; img: {};", nodeLabel, type, img);
            }
        }
        return body;
    }
}
