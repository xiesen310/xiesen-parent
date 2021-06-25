package com.github.xiesen.es6.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/6/13
 */
public class MockJsonData {
    public static void main(String[] args) throws IOException {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.70.25", 9200, "http")));

        Map<String, Object> map = new HashMap<>(16);
        map.put("timestamp", System.currentTimeMillis());
        map.put("app", "clickhouse");
        map.put("message", "/gsnews/gsf10/capital/main/1.0?code=601618&market=SH&gs_proxy_params=eyJnc19yZXFfd");
        int j = 0;
        while (j < 10000) {
            j++;
            BulkRequest bulkRequest = new BulkRequest();
            for (int i = 0; i < 1000; i++) {
                IndexRequest request = new IndexRequest().index("clickhouse").type("doc").source(map);
                bulkRequest.add(request);
            }
            BulkResponse responses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(responses.status());
        }

        client.close();
    }
}
