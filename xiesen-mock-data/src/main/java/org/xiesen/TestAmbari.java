package org.xiesen;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.xiesen.model.TaskInfo;

/**
 * @author xiesen
 * @title: TestAmbari
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/17 14:28
 */
public class TestAmbari {
    public static void main(String[] args) {
        String s = HttpUtil.get("http://zork7084:8088/ws/v1/cluster/apps/application_1658813332711_0026");
        System.out.println(s);
        System.out.println("============================================");
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.parseObject(s).getString("app"));
        String id1 = jsonObject.getString("id");
        String user = jsonObject.getString("user");
        String name = jsonObject.getString("name");
        String queue = jsonObject.getString("queue");
        String state = jsonObject.getString("state");
        String trackingUrl = jsonObject.getString("trackingUrl");
        String applicationType = jsonObject.getString("applicationType");
        String startedTime = jsonObject.getString("startedTime");
        String finishedTime = jsonObject.getString("finishedTime");
        String elapsedTime = jsonObject.getString("elapsedTime");
        String allocatedMB = jsonObject.getString("allocatedMB");
        String allocatedVCores = jsonObject.getString("allocatedVCores");
        String runningContainers = jsonObject.getString("runningContainers");
        TaskInfo taskInfo = TaskInfo.builder().id(id1).user(user).name(name).queue(queue).state(state)
                .trackingUrl(trackingUrl).applicationType(applicationType).startedTime(startedTime)
                .finishedTime(finishedTime).elapsedTime(elapsedTime).allocatedMB(allocatedMB)
                .allocatedVCores(allocatedVCores).runningContainers(runningContainers)
                .build();
        System.out.println(JSONObject.toJSONString(taskInfo));


        System.out.println("============================================");

    }
}
