package com.github.xiesen.junit.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.algorithm.xxhash.AbstractLongHashFunction;
import com.github.xiesen.junit.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 谢森
 * @since 2021/5/25
 */
@Slf4j
public class ReqDedupHelper {
    /**
     * @param reqJSON     请求的参数，这里通常是JSON
     * @param excludeKeys 请求参数里面要去除哪些字段再求摘要
     * @return 去除参数的MD5摘要
     */
    public String dedupParamMD5(final String reqJSON, String... excludeKeys) {
        String decreptParam = reqJSON;

        TreeMap paramTreeMap = JSON.parseObject(decreptParam, TreeMap.class);
        if (excludeKeys != null) {
            List<String> dedupExcludeKeys = Arrays.asList(excludeKeys);
            if (!dedupExcludeKeys.isEmpty()) {
                for (String dedupExcludeKey : dedupExcludeKeys) {
                    paramTreeMap.remove(dedupExcludeKey);
                }
            }
        }

        String paramTreeMapJSON = JSON.toJSONString(paramTreeMap);
        String md5deDupParam = jdkMD5(paramTreeMapJSON);
        log.debug("md5deDupParam = {}, excludeKeys = {} {}", md5deDupParam, Arrays.deepToString(excludeKeys),
                paramTreeMapJSON);
        return md5deDupParam;
    }

    private static String jdkMD5(String src) {
        String res = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(src.getBytes());
            res = DatatypeConverter.printHexBinary(mdBytes);
        } catch (Exception e) {
            log.error("", e);
        }
        return res;
    }


    public static final String config = "job.env{\n" +
            "    job.name = \"alarm2es\"\n" +
            "    execution.parallelism = 8\n" +
            "}\n" +
            "\n" +
            "ddl {\n" +
            "    # 支持表，函数定义\n" +
            "    define = \"\"\n" +
            "}\n" +
            "\n" +
            "\n" +
            "source {\n" +
            "     ZorkDataKafkaStream {\n" +
            "        # json\n" +
            "        data.type = \"json\"\n" +
            "        topics = \"dwd_alarm_real\"\n" +
            "        consumer.bootstrap.servers = \"node122:9092,node121:9092,node120:9092\"\n" +
            "        consumer.group.id = \"alarm2es\"\n" +
            "        # latest/earliest/none\n" +
            "        offset.reset=\"none\"\n" +
            "        kafka.security.model=\"none\"\n" +
            "        result_table_name = \"ZorkDataKafkaStreamSource\"\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "transform {\n" +
            "}\n" +
            "\n" +
            "sink {\n" +
            "    Alarm2Es {\n" +
            "        hosts = [\"node122:9200\",\"node121:9200\",\"node120:9200\"]\n" +
            "        es.version = \"6\"\n" +
            "        es.prefix=\"dwd_alarm_real\"\n" +
            "        storageWay=\"old\"\n" +
            "        result_table_name = \"ZorkDataElasticsearch6\"\n" +
            "        isCustomId=true\n" +
            "    }\n" +
            "}";

    public static void main(String[] args) {
        ReqDedupHelper reqDedupHelper = new ReqDedupHelper();
        String json = "{\"execution.flink.checkpoint.interval\":60000,\"execution.checkpoint.interval\":60000," +
                "\"execution.checkpoint.mode\":\"EXACTLY_ONCE\",\"execution.checkpoint.timeout\":60000,\"execution" +
                ".max-concurrent-checkpoints\":1,\"execution.checkpoint.cleanup-mode\":false,\"execution.flink" +
                ".checkpoint.cleanup.mode\":false,\"execution.state.backend\":\"ROCKSDB\",\"execution.state" +
                ".checkpoints.dir\":\"hdfs://cdh-2:8020/tmp/xiesen/demo\"}";
        String s = reqDedupHelper.dedupParamMD5(json);
        System.out.println(s);


        String result = HttpClientUtil.sendHttpGet("http://192.168.70.120:8081/jobs/352fcdad13525c187f04170265b19abd" +
                "/config");

        JSONObject jsonObject = JSONObject.parseObject(result);
        String userConfig =
                JSONObject.parseObject(jsonObject.get("execution-config").toString()).get("user-config").toString();


        String coreConfig = JSONObject.parseObject(userConfig).get("coreConfig").toString();
        System.out.println(coreConfig);


        long l = AbstractLongHashFunction.xx().hashChars(coreConfig.trim());
        long l1 = AbstractLongHashFunction.xx().hashChars(config.trim());
        System.out.println(l);
        System.out.println(l1);

        String s1 = StringEscapeUtils.unescapeJava(coreConfig.trim());
        String s2 = StringEscapeUtils.unescapeJava(config.trim());
        System.out.println(s1.equalsIgnoreCase(s2));


        String s3 = replaceBlank(s1);
        String s4 = replaceBlank(s2);

        System.out.println(s3.equalsIgnoreCase(s4));
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
