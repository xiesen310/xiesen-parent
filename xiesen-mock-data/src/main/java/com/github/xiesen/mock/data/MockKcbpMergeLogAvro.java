package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSON;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.CustomerProducer;
import com.github.xiesen.mock.util.ProducerPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiese
 * @Description 模拟测试数据
 * @Email xiesen310@163.com
 * @Date 2020/7/24 17:02
 */
public class MockKcbpMergeLogAvro {

    private static String getRandomOffset() {
        Random random = new Random();
        long l = random.nextInt(100000);
        return String.valueOf(l);
    }


    private static final String[] FUNC_ID_LIST = new String[]{"410232", "410502", "410809", "410503", "unknown"};

    private static String getRandomFuncid() {
        return FUNC_ID_LIST[new Random().nextInt(FUNC_ID_LIST.length)];
    }


    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        Map<String, String> dimensions = new HashMap<>(11);
        dimensions.put("clustername", "集群");
        dimensions.put("hostname", "zork1-92.host.com");
        dimensions.put("appprogramname", "WEB");
        dimensions.put("operway", "A");
        dimensions.put("appsystem", "jzjy");
        dimensions.put("servicename", "模块");
        dimensions.put("nodeid", "11105");
        dimensions.put("ip", "192.168.1.92");
        dimensions.put("funcid", getRandomFuncid());
        dimensions.put("orgid", "");
        dimensions.put("servicecode", "模块");
        return dimensions;
    }

    private static Map<String, Double> getRandomMeasures() {
        Map<String, Double> measures = new HashMap<>(1);
        final int num = new Random().nextInt(100);
        measures.put("latency", Double.valueOf(num));
        return measures;
    }

    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>(2);
        normalFields.put("inputtype", "unknown");
        normalFields.put("topicname", "ods_jzjy_log23");
        normalFields.put("fundid", "unknown");
        normalFields.put("messid", "9100011204A8D83E27C4E7CF");
        normalFields.put("netaddr", "");
        normalFields.put("beg_logtime", "20230214-174343");
        normalFields.put("beat_time", "2023-02-15T02:34:02.696Z");
        normalFields.put("collector_type", "filebeat");
        normalFields.put("fmillsecond", "209568625");
        normalFields.put("message", "20230214-174343 Req: NodeId=11105, QueueId=202, MsgId=9100011204A8D83E27C4E7CF, Len=252, Buf=012000003005650000001000000000000000B4055DCD2852KCXP00  GV2gODkBbGg=                          410232  00126000000          191_CA=2.3&_ENDIAN=0&funcid=410232&custid=&custorgid=&trdpwd=&netaddr=&orgid=&operway=A&ext=0&sysdate=20230214&orderdate=20230214 Ans: NodeId=11105, QueueId=202, MsgId=9100011204A8D83E27C4E7CF, Len=367, Buf=013000003005650000003111051110511105B4055DCD2852KCXP00  GV2gODkBbGg=63EB57CF011105000013359540410232  00241000000          191_CA=2.4&_SYSID=11105&_ENDIAN=0&_RS_1=MESSAGE;3;LEVEL,CODE,MSG;1&_1=0,0,取得当前系统状态成功&_EORS_1=1&_RS_2=DATA;5;sysdate, orderdate, status, nightremark, systime;2&_2=20230214,20230215,0,夜市委托,17434307&_EORS_2=2&_RC=2&_CC=5&_TL=3:1;5:1;");
        normalFields.put("end_logtime", "20230214-174343");
        normalFields.put("smillsecond", "209568625");
        normalFields.put("loginip", "unknown");
        normalFields.put("custid", "");
        normalFields.put("collecttime", DateUtil.getUTCTimeStr());
        return normalFields;
    }


    public static void main(String[] args) throws Exception {
        long size = 1000000L * 1;
        String topicName = "xiesen_ods_default_log";
        for (int i = 0; i < size; i++) {
            String logTypeName = "kcbp_log";
            String timestamp = DateUtil.getUTCTimeStr();
            String source = "/opt/xiesen/filebeat-7.4.0-linux-x86_64/kcbpLog/20230214/runlog52.log";
            String offset = getRandomOffset();

            Map<String, String> dimensions = getRandomDimensions();
            Map<String, Double> measures = getRandomMeasures();
            Map<String, String> normalFields = getRandomNormalFields();
            Map<String, Object> map = new HashMap<>();
            map.put("logTypeName", logTypeName);
            map.put("timestamp", timestamp);
            map.put("source", source);
            map.put("offset", offset);
            map.put("dimensions", dimensions);
            map.put("measures", measures);
            map.put("normalFields", normalFields);
            System.out.println(JSON.toJSONString(map));
            CustomerProducer producer = ProducerPool.getInstance("D:\\develop\\workspace\\xiesen-parent\\xiesen-mock-data\\src\\main\\resources\\config.properties").getProducer();
            producer.sendLog(logTypeName, timestamp, source, offset, dimensions, measures, normalFields);

            Thread.sleep(1000);
        }
        Thread.sleep(1000);
    }
}
