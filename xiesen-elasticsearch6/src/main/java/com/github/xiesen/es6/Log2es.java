package com.github.xiesen.es6;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.joda.time.DateTime;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/11/20 14:49
 */
public class Log2es {
    private static String mockData() {
        String logTypeName = "logTypeName";
        String timestamp = new DateTime() + "";
        String source = "/var/log/a.log";
        String offset = "1111";
        Map<String, String> dimensions = new HashMap<>(8);
        Map<String, Double> measures = new HashMap<>(8);
        Map<String, String> normalFields = new HashMap<>(8, 1);
        dimensions.put("ip", "192.168.1.1");
        dimensions.put("hostname", "zorkdata");
        normalFields.put("message", "aaaaa");
        normalFields.put("destTime", System.currentTimeMillis() + "");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logTypeName", logTypeName);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("source", source);
        jsonObject.put("offset", offset);
        jsonObject.put("dimensions", dimensions);
        jsonObject.put("measures", measures);
        jsonObject.put("normalFields", normalFields);
        return jsonObject.toJSONString();
    }

    public static void main(String[] args) throws Exception {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "dev-es6").build();

        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.70.25"), 9300));

        BulkRequestBuilder builder = client.prepareBulk();

        IndexRequestBuilder request1 = client.prepareIndex("xiesen4", "_doc", "15").setSource(
                mockData(), XContentType.JSON
        );

        builder.add(request1);

        //该方法ES默认是分片1秒钟后刷新，即插入成功后马上查询，插入的数据不能马上被查出
        BulkResponse response = builder.get();
        System.out.println(response.status());
        if (response.hasFailures()) {
            System.out.println("操作失败");
        }

        System.out.println(mockData());
    }
}
