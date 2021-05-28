package com.github.xiesen.es6.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author 谢森
 * @since 2021/5/25
 */
public class ElasticSearchClient {
    public static void main(String[] args) throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.70.25", 9200,
                "http")));
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
