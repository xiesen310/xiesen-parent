package com.github.xiesen.kafka241;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.avro.AvroSerializerFactory;
import com.github.xiesen.common.utils.DateUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 海通网络日志
 *
 * @author 谢森
 * @since 2021/2/5
 */
public class NetProducerTest {
    private static String[] operator = {"登录", "委托", "转账"};
    private static String[] operator_status = {"success", "fail"};

    /**
     * kafka producer
     *
     * @param bootstrapServers
     * @param serializerClassName
     * @param <T>
     * @return
     */
    private static <T> KafkaProducer<String, T> buildProducer(String bootstrapServers, String serializerClassName) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", "all");
        props.put("delivery.timeout.ms", 30000);
        props.put("linger.ms", 10);
        props.put("request.timeout.ms", 20000);
        props.put("buffer.memory", 33554432);
        props.put("batch.size", 16384);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", serializerClassName);

        // kerberos 认证
        /*System.setProperty("java.security.krb5.conf", "D:\\tmp\\kerberos\\krb5.conf");
        System.setProperty("java.security.auth.login.config", "D:\\tmp\\kerberos\\kafka_server_jaas.conf");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");*/

        // sasl 认证
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");
        Configuration.setConfiguration(new SaslConfig("admin", "admin"));

        return new KafkaProducer<>(props);
    }

    public static void main(String[] args) throws Exception {
        String bootstrapServers = "zorkdata-92:9092";
//        String bootstrapServers = "zorkdata-91:9092";
        String topic = "networklog20";

        String logTypeName = "networklog";

        KafkaProducer<String, byte[]> producer = buildProducer(bootstrapServers, ByteArraySerializer.class.getName());

        long time1 = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            JSONObject jsonObject = new JSONObject();
            String timestamp = DateUtil.getUTCTimeStr();
            Map<String, String> dimensions = getRandomDimensions();
            Map<String, String> normalFieldsMap = getRandomNormalFields();
            Map<String, Double> measures = getRandomMetrics();

            jsonObject.put("logTypeName", logTypeName);
            jsonObject.put("timestamp", timestamp);
            jsonObject.put("source", "核新");

            String offset = getRandomOffset();
            jsonObject.put("offset", offset);

            jsonObject.put("dimensions", dimensions);
            jsonObject.put("measures", measures);
            jsonObject.put("normalFields", normalFieldsMap);

            TimeUnit.MILLISECONDS.sleep(10);
            System.out.println(jsonObject.toJSONString());

            byte[] bytes = AvroSerializerFactory.getLogAvroSerializer().serializingLog(logTypeName, timestamp, "核新",
                    offset, dimensions, measures, normalFieldsMap);
            producer.send(new ProducerRecord<>(topic, bytes));
        }

        long time2 = System.currentTimeMillis();
        System.out.println("当前程序耗时：" + (time2 - time1) / 1000 + "s");
        producer.flush();
        producer.close();
        Thread.sleep(1000L);

    }

    /**
     * 封装随机指标数据
     *
     * @return
     */
    private static Map<String, Double> getRandomMetrics() {
        Map<String, Double> dimensionsMap = new HashMap<>();
        Random random = new Random();
        int i = random.nextInt(200);
        dimensionsMap.put("latency", Double.valueOf(i));
        return dimensionsMap;
    }

    /**
     * 封装随机维度数据
     *
     * @return
     */
    private static Map<String, String> getRandomDimensions() {
        Random random = new Random();
        Map<String, String> dimensionsMap = new HashMap<>();
        dimensionsMap.put("operstatus", operator_status[random.nextInt(operator_status.length)]);
        dimensionsMap.put("funcno", operator[random.nextInt(operator.length)]);
        dimensionsMap.put("mark", random.nextInt(10000) + " ");
        dimensionsMap.put("appsystem", "核新");
        return dimensionsMap;

    }

    /**
     * 封装普通列数据
     *
     * @return
     */
    private static Map<String, String> getRandomNormalFields() {
        Map<String, String> normalFields = new HashMap<>();
        normalFields.put("logdate", "2020-10-08");
        normalFields.put("anstime", "2020-10-08 21:34:28.820");
        normalFields.put("readtime", "2020-10-08 21:35:14.382");
        normalFields.put("conncetmsg", "IP:X.X.X.X MAC:X 线程:001 通道ID:001 事务ID:001");
        normalFields.put("message",
                "[res]=21:34:28.820 成功处理 IP:X.X.X.X MAC:X 线程:001 通道ID:001 事务ID:001 请求:(0-061)XX 营业部:XXX 耗时A:78 耗时B:0 " +
                        "排队:0,[items]=[\\\"0||\\\"] [res]=21:34:28.820 成功处理 IP:X.X.X.X MAC:X 线程:001 通道ID:001 事务ID:001" +
                        " 请求:(0-061)XX 营业部:XXX 耗时A:78 耗时B:0 排队:0,[items]=[\\\"0||\\\"]");
        return normalFields;
    }

    /**
     * 获取随机 offset 信息
     *
     * @return
     */
    private static String getRandomOffset() {
        Random random = new Random();
        long l = random.nextInt(10000);
        return String.valueOf(l);
    }

}
