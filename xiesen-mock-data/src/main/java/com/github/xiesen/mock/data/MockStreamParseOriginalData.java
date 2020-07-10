package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

/**
 * @author xiese
 * @Description 模拟解析原始数据
 * @Email xiesen310@163.com
 * @Date 2020/7/10 14:43
 */
public class MockStreamParseOriginalData {
    public static String buildMsg() {
        JSONObject fileBeatObject = new JSONObject();
        JSONObject beat = new JSONObject();
        JSONObject host = new JSONObject();
        JSONObject log = new JSONObject();
        JSONObject path = new JSONObject();
        log.put("file", path);
        path.put("path", "/var/log/access.log");
        JSONObject metadata = new JSONObject();
        metadata.put("beat", "filebeat");
        metadata.put("type", "doc");
        metadata.put("version", "6.8.1");
        JSONObject prospector = new JSONObject();
        prospector.put("type", "log");

        beat.put("hostname", "zorkdata-151");
        beat.put("name", "zorkdata-151");
        beat.put("version", "6.8.1");

        host.put("containerized", "false");
        host.put("name", "zorkdata-151");
        host.put("architecture", "x86_64");

        String message = "";
        fileBeatObject.put("offset", "1593590026413");
        fileBeatObject.put("log", log);
        fileBeatObject.put("@metadata", metadata);
        fileBeatObject.put("prospector", prospector);
        fileBeatObject.put("logTypeName", "test_topic_log8");
        fileBeatObject.put("source", "/var/log/nginx/access.log");
        fileBeatObject.put("message", message);
        fileBeatObject.put("collectorruleid", "1");
        fileBeatObject.put("appprogramname", "test_appprogramname8");
        fileBeatObject.put("@timestamp", DateUtil.getUTCTimeStr());

        fileBeatObject.put("servicecode", "test_cdde8");
        fileBeatObject.put("appsystem", "test_appsystem8");
        fileBeatObject.put("beat", beat);
        fileBeatObject.put("host", host);
        fileBeatObject.put("servicename", "test_appprogramname8");
        fileBeatObject.put("grokmessage", "192.168.1.151 - - [29/Jun/2020:16:09:23 +0800] \"GET /webserver/scene/getSceneList.do?menuItemId=1039&sceneGroupId=&templateFlag= HTTP/1.0\" 200 3167");
        fileBeatObject.put("datemessage", "2020-07-06T08:22:00.666Z");
        fileBeatObject.put("jsonmessage", "{\"testmessage\":\"test\"}");
        fileBeatObject.put("geoipmessage", "151.101.230.217");
        fileBeatObject.put("csvmessage", "aaaa,bbbbb,cccc");
        fileBeatObject.put("kvmessage", "pin=12345~0&d=123&e=foo@bar.com&oq=bobo&ss=12345");
        fileBeatObject.put("mutatemessage", "8e3dfc85999b4e02bae4adf4b92b909a");
        int length = fileBeatObject.toJSONString().length();

        StringBuilder msg = new StringBuilder();
        String strs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        while (length <= 1024) {
            msg.append(strs);
            message = message + msg;
            length += strs.length();
        }
        fileBeatObject.put("message", message);
        return fileBeatObject.toJSONString();
    }


    public static void main(String[] args) throws InterruptedException {
        long size = 10000000L * 1;
        String topicName = "shaojiao_1000w";
        for (int i = 0; i < size; i++) {
            String json = buildMsg();
            CustomerProducer producer = ProducerPool.getInstance("config.properties").getProducer();
            producer.sendJsonLog(json);
        }
        Thread.sleep(1000);
    }
}
