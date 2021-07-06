package com.github.xiesen.es6.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author 谢森
 * @since 2021/5/25
 */
public class ElasticSearchDocumentAPI {
    public RestHighLevelClient restHighLevelClient;

    @Before
    public void connectES() {
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.70.25", 9200, "http")));
    }

    /**
     * es创建索引API，方法一，使用json串
     */
    @Test
    public void testIndex() throws IOException {
        String jsonString = "{" +
                "    \"user\" : \"kimchy\",\n" +
                "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                "    \"message\" : \"trying out Elasticsearch\"\n" +
                "}";

        IndexRequest request = new IndexRequest("posts", "doc", "2");
        request.source(jsonString, XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
        restHighLevelClient.close();
    }

    /**
     * es创建索引API，方法二,MAP
     */
    @Test
    public void testIndex2() throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("post_date", "2009-11-15T14:12:12");
        jsonMap.put("message", "trying out Elasticsearch");

        IndexRequest request = new IndexRequest("posts", "doc", "3");
        request.source(jsonMap);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
        restHighLevelClient.close();
    }

    /**
     * es创建索引API，方法三，XContentBuilder
     */
    @Test
    public void testIndex3() throws IOException {
        XContentBuilder builder = jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.field("post_date", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();

        IndexRequest request = new IndexRequest("posts", "doc", "4");
        request.source(builder);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
        restHighLevelClient.close();
    }

    /**
     * es创建索引API，方法四，Object
     */
    @Test
    public void testIndex4() throws IOException {

        IndexRequest request = new IndexRequest("posts", "doc", "5")
                .source("user", "kimchy",
                        "post_date", new Date(),
                        "message", "trying out Elasticsearch");

        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
        restHighLevelClient.close();
    }

    public void update1() throws IOException {
        IndexRequest indexRequest = new IndexRequest("index", "type", "1")
                .source(jsonBuilder()
                        .startObject()
                        .field("name", "Joe Smith")
                        .field("gender", "male")
                        .endObject());
        UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
                .doc(jsonBuilder()
                        .startObject()
                        .field("gender", "male")
                        .endObject())
                .upsert(indexRequest);


    }
}
