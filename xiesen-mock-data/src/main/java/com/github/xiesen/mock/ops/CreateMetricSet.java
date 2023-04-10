package com.github.xiesen.mock.ops;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 创建以及发布指标集*
 *
 * @author xiesen
 */
public class CreateMetricSet {

    public static final String CREATE_METRICSET_URL = "https://paas.devnoah.zorkdata.com/app/icube5/restful/v1/metricSet/addMetricSet";
    public static final String DEPLOY_METRICSET_URL = "https://paas.devnoah.zorkdata.com/app/icube5/restful/v1/metricSet/publicMetricSet";

    public static final String METRIC = "{\n" +
            "    \"comment\":\"\",\n" +
            "    \"metricSetCategoryID\":8,\n" +
            "    \"metricSetCategoryName\":\"网络\",\n" +
            "    \"indicatorsSourceName\":\"自定义\",\n" +
            "    \"metricSetColumnList\":[\n" +
            "        {\n" +
            "             \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"total\",\n" +
            "            \"displayName\":\"总内存大小\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"1\",\n" +
            "            \"displayType\":\"0\",\n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"total\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"used_bytes\",\n" +
            "            \"displayName\":\"已有内存大小\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"2\",\n" +
            "            \"displayType\":\"0\",\n" +
            "      \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"used_bytes\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"free\",\n" +
            "            \"displayName\":\"可用内存大小\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"3\",\n" +
            "            \"displayType\":\"0\",\n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"free\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"used_pct\",\n" +
            "            \"displayName\":\"内存使用率\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"4\",\n" +
            "            \"displayType\":\"0\",\n" +
            "  \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"used_pct\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"actual_used_bytes\",\n" +
            "            \"displayName\":\"实际已用内存大小\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"5\",\n" +
            "            \"displayType\":\"0\",\n" +
            "   \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"actual_used_bytes\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"actual_free\",\n" +
            "            \"displayName\":\"实际可用内存大小\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"6\",\n" +
            "            \"displayType\":\"0\",\n" +
            "        \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"actual_free\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"actual_used_pct\",\n" +
            "            \"displayName\":\"实际内存利用率\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"7\",\n" +
            "            \"displayType\":\"0\",\n" +
            "\n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"actual_used_pct\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"swap_total\",\n" +
            "            \"displayName\":\"swap内存总大小\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"8\",\n" +
            "            \"displayType\":\"0\",\n" +
            "\n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"swap_total\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"swap_used_bytes\",\n" +
            "            \"displayName\":\"swap已用内存大小\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"9\",\n" +
            "            \"displayType\":\"0\",\n" +
            "\n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"swap_used_bytes\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"swap_free\",\n" +
            "            \"displayName\":\"swap可用内存大小\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"10\",\n" +
            "            \"displayType\":\"0\",\n" +
            "          \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"swap_free\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"swap_used_pct\",\n" +
            "            \"displayName\":\"swap内存使用率\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"1\",\n" +
            "            \"dimensionid\":0,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"11\",\n" +
            "            \"displayType\":\"0\",\n" +
            " \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":2,\n" +
            "            \"measureName\":\"swap_used_pct\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":2\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"hostname\",\n" +
            "            \"displayName\":\"主机名\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"0\",\n" +
            "            \"dimensionid\":1,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"12\",\n" +
            "            \"displayType\":\"0\",\n" +
            "\n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":null,\n" +
            "            \"measureName\":\"hostname\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":1\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"ip\",\n" +
            "            \"displayName\":\"主机ip\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"0\",\n" +
            "            \"dimensionid\":1,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"13\",\n" +
            "            \"displayType\":\"0\",\n" +
            "   \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":null,\n" +
            "            \"measureName\":\"ip\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":1\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"appsystem\",\n" +
            "            \"displayName\":\"系统简称\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"0\",\n" +
            "            \"dimensionid\":1,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"14\",\n" +
            "            \"displayType\":\"0\",\n" +
            "       \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":null,\n" +
            "            \"measureName\":\"appsystem\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":1\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"servicename\",\n" +
            "            \"displayName\":\"组件名称\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"0\",\n" +
            "            \"dimensionid\":1,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"15\",\n" +
            "            \"displayType\":\"0\",\n" +
            "\n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":null,\n" +
            "            \"measureName\":\"servicename\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":1\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"clustername\",\n" +
            "            \"displayName\":\"集群名称\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"0\",\n" +
            "            \"dimensionid\":1,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"16\",\n" +
            "            \"displayType\":\"0\",\n" +
            "      \n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":null,\n" +
            "            \"measureName\":\"clustername\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":1\n" +
            "        },\n" +
            "        {\n" +
            "            \"metricSetColumnId\":0,\n" +
            "            \"metricSetId\":0,\n" +
            "            \"metricSetColumnName\":\"appprogramname\",\n" +
            "            \"displayName\":\"组件名称\",\n" +
            "            \"unit\":null,\n" +
            "            \"tagGroup\":null,\n" +
            "            \"columnType\":\"0\",\n" +
            "            \"dimensionid\":1,\n" +
            "            \"percentage\":1,\n" +
            "            \"displayOrder\":\"17\",\n" +
            "            \"displayType\":\"0\",\n" +
            "            \"comment\":null,\n" +
            "            \"dataType\":null,\n" +
            "            \"measureName\":\"appprogramname\",\n" +
            "            \"measureDisplayName\":null,\n" +
            "            \"columnValue\":1\n" +
            "        }\n" +
            "    ],\n" +
            "    \"metricSetID\":0,\n" +
            "    \"metricSetName\":\"aaa\",\n" +
            "    \"metricSetTypeID\":2,\n" +
            "    \"metricSetTypeName\":\"K线/基线\",\n" +
            "    \"timeGranularityID\":4,\n" +
            "    \"timeGranularityName\":\"1min\",\n" +
            "    \"version\":0,\n" +
            "    \"state\":0,\n" +
            "    \"displayName\":\"aaa\"\n" +
            "}";

    public static void main(String[] args) {
        for (int i = 500; i < 1000; i++) {
            try {

                final JSONObject jsonObject = JSON.parseObject(METRIC);
                jsonObject.put("displayName", "memory_system_mb_" + i);
                jsonObject.put("metricSetName", "memory_system_mb_" + i);
                System.out.println(jsonObject.toJSONString());
                final HttpResponse execute = HttpUtil.createPost(CREATE_METRICSET_URL)
                        .body(jsonObject.toJSONString()).execute();
                final String body = execute.body();
                final JSONObject result = JSON.parseObject(body);
                final Object o = result.getJSONObject("data").get("metricSetID");
                final Integer metricSetID = Integer.valueOf(o.toString());
                /// 发布指标集
                final HttpResponse deployMetricSet = HttpUtil.createPost(DEPLOY_METRICSET_URL)
                        .form("metricSetId", metricSetID)
                        .execute();

                System.out.println(execute);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

        }

    }
}
