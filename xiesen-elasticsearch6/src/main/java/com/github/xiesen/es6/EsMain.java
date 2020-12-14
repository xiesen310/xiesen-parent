package com.github.xiesen.es6;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author 谢森
 * @Description EsMain
 * @Email xiesen310@163.com
 * @Date 2020/11/20 11:28
 */
public class EsMain {
    public static void main(String[] args) throws IOException {

        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "dev-es6").build();

        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.70.25"), 9300));

        BulkRequestBuilder builder = client.prepareBulk();

        IndexRequestBuilder request = client.prepareIndex("lib2", "books", "8").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "python")
                        .field("price", 99)
                        .endObject()
        );

        IndexRequestBuilder request2 = client.prepareIndex("lib2", "books", "9").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "VR")
                        .field("price", 29)
                        .endObject()
        );

        IndexRequestBuilder request3 = client.prepareIndex("lib2", "books", "10").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "VR")
                        .field("price", "29")
                        .endObject()
        );

        IndexRequestBuilder request4 = client.prepareIndex("lib2", "books", "11").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "VR")
                        .field("price", "29")
                        .field("ts", new DateTime())
                        .endObject()
        );

        IndexRequestBuilder request5 = client.prepareIndex("lib2", "books", "12").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "VR")
                        .field("price", "29")
                        .field("ts", new DateTime() + "")
                        .endObject()
        );
        IndexRequestBuilder request6 = client.prepareIndex("lib2", "books", "13").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "VR")
                        .field("price", "29")
                        .field("ts", "")
                        .endObject()
        );

        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("title", "VR")
                .field("price", "29")
                .field("ts", "")
                .endObject();

        IndexRequestBuilder request7 = client.prepareIndex("lib2", "books", "14").setSource(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "VR")
                        .field("price", "29")
                        .field("ts", new DateTime())
                        .endObject()
        );

        builder.add(request);
        builder.add(request2);
        builder.add(request3);
        builder.add(request4);
        builder.add(request5);
        builder.add(request6);
        builder.add(request7);


        //该方法ES默认是分片1秒钟后刷新，即插入成功后马上查询，插入的数据不能马上被查出
        BulkResponse response = builder.get();
        System.out.println(response.status());
        if (response.hasFailures()) {
            System.out.println("操作失败");
        }
    }
}
