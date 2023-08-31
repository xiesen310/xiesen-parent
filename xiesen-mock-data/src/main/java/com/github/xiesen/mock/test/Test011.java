package com.github.xiesen.mock.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiesen
 */
public class Test011 {
    public static final String CC = "业务板块,实物量-组网专线,组网专线净增用户数\n" +
            "业务板块,实物量-组网专线,组网专线新增用户数\n" +
            "业务板块,实物量-拆机数,宽带拆机用户数\n" +
            "业务板块,实物量-新增数,智家?全屋wifi新增用户数\n" +
            "业务板块,实物量-拆机数,229以上高套宽带拆机用户数\n" +
            "业务板块,产数收入,业务月收入\n" +
            "业务板块,实物量-拆机数,智家?全屋wifi拆机用户数\n" +
            "业务板块,基础业务收入,业务月收入\n" +
            "业务板块,实物量-净增数,智家全屋wifi净增数\n" +
            "业务板块,实物量-新增数,229以上高套宽带新增用户数\n" +
            "业务板块,实物量-在网用户数,固话在网用户数\n" +
            "业务板块,实物量-翼支付,翼支付活跃用户渗透率\n" +
            "业务板块,实物量-新增数,固话新增用户数\n" +
            "业务板块,实物量-在网用户数,低套宽带计费在网用户数\n" +
            "业务板块,业务活跃度,移动活跃出账\n" +
            "业务板块,实物量-新增数,低价值宽带新增用户数\n" +
            "业务板块,实物量-在网用户数,宽带计费在网用户数\n" +
            "业务板块,业务活跃度,宽带活跃计费\n" +
            "业务板块,实物量-翼支付,翼支付活跃用户数\n" +
            "业务板块,实物量-新增数,宽带新增用户数\n" +
            "业务板块,实物量-在网用户数,移动有效在网用户数\n" +
            "业务板块,实物量-拆机数,固话拆机用户数\n" +
            "业务板块,实物量-组网专线,组网专线拆机用户数\n" +
            "业务板块,实物量-在网用户数,IPTV在网用户数\n" +
            "业务板块,实物量-新增数,IPTV新增用户数\n" +
            "业务板块,实物量-拆机数,IPTV拆机用户数\n" +
            "业务板块,台阶收入,业务月收入\n" +
            "业务板块,年度大单签约完成率,年度大单签约完成率\n" +
            "业务板块,实物量-新增数,移动新增用户数\n" +
            "业务板块,实物量-净增数,IPTV净增数\n" +
            "业务板块,实物量-拆机数,低价值宽带拆机用户数\n" +
            "业务板块,实物量-在网用户数,智家全屋wifi在网用户数\n" +
            "业务板块,实物量-净增数,固话用户净增数\n" +
            "业务板块,实物量-净增数,移动有效用户净增数\n" +
            "业务板块,实物量-净增数,宽带计费净增数\n" +
            "业务板块,实物量-在网用户数,229以上高套宽带计费在网用户数\n" +
            "业务板块,年度累计四融率到达数,年度累计四融率到达数\n" +
            "业务板块,实物量-组网专线,组网专线在网用户到达数\n" +
            "业务板块,年度累计大单签约额到达值,年度累计大单签约额到达值\n" +
            "业务板块,实物量-拆机数,移动拆机用户数\n";

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        final String[] strings = CC.split("\n");
        for (String s : strings) {
            final String[] split = s.split(",");
            String sectionName = split[0];
            String subSectionName = split[1];
            String targetName = split[2];
//            System.out.println("sectionName = " + sectionName + "; subSectionName = " + subSectionName + " ; targetName = " + targetName);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sectionName", sectionName);
            jsonObject.put("subSectionName", subSectionName);
            jsonObject.put("targetName", targetName);
            jsonObject.put("dimName", "无");
            jsonObject.put("dimValue", "无");
            jsonObject.put("scopeStart", "20230101");
            jsonObject.put("scopeEnd", "20230901");
            String measurement = sectionName + "_" + subSectionName + "_" + targetName;
//            System.out.println("tableName : " + measurement);
            System.out.println(jsonObject.toJSONString());
            list.add(jsonObject.toJSONString());
        }
        System.out.println("list: " + JSONObject.toJSONString(list));
    }
}
