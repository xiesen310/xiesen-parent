package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.common.utils.DateUtil;
import com.github.xiesen.mock.util.SaslConfig;
import com.github.xiesen.mock.util.ScramSaslConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.security.auth.login.Configuration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @Description 模拟解析格式数据
 * @Email xiesen310@163.com
 * @Date 2020/12/14 13:21
 */
public class MockKafkaScramProducer {

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
        props.put("retries", 5);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", serializerClassName);
        props.put("batch.size", 16384);
        props.put("linger.ms", 0);
        props.put("buffer.memory", 33554432);

        // kerberos 认证
        /*System.setProperty("java.security.krb5.conf", "D:\\tmp\\kerberos\\krb5.conf");
        System.setProperty("java.security.auth.login.config", "D:\\tmp\\kerberos\\kafka_server_jaas.conf");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");*/

        // sasl 认证
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        Configuration.setConfiguration(new ScramSaslConfig("xiesen", "xiesen"));


        return new KafkaProducer<>(props);
    }

    /**
     * {"name":"99999999999999","age":77,"country_code":"bubububububu","appsystem":"appsystemTest"}
     *
     * @return
     */
    public static String buildMsg() {
        String str = "{\"component_name\":\"nginx\",\"host\":{\"os\":{\"name\":\"CentOS Linux\",\"codename\":\"Core\",\"platform\":\"centos\",\"version\":\"7 (Core)\",\"kernel\":\"3.10.0-1062.el7.x86_64\",\"family\":\"redhat\"},\"name\":\"zork-rd-dev-7092\",\"architecture\":\"x86_64\",\"hostname\":\"zork-rd-dev-7092\",\"id\":\"560a9e4ea53645668420a2c269bf8fe4\",\"containerized\":false},\"appprogramname\":\"模块\",\"clustername\":\"集群\",\"input\":{\"type\":\"log\"},\"servicecode\":\"模块\",\"servicename\":\"模块\",\"topicname\":\"ods_nginx_log\",\"collector_type\":\"filebeat\",\"collectruleid\":138,\"agent\":{\"id\":\"27314890-5e9f-45c8-ba48-72386771f755\",\"ephemeral_id\":\"6966b76c-336a-4911-b2fa-dc8295ec54f2\",\"version\":\"7.4.0\",\"hostname\":\"zork-rd-dev-7092\",\"type\":\"filebeat\"},\"message\":\"192.168.50.4 - - [30/Jun/2022:16:14:45 +0800] \\\"GET /app/icube5/icube-monitor/v1/alarmMessage/listAlarmMessageSources HTTP/1.1\\\" 200 130 \\\"http://paas.devnoah.com/app/icube5/\\\" \\\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36\\\"\",\"transtime\":\"2022-06-30T08:14:51.648Z\",\"ecs\":{\"version\":\"1.1.0\"},\"appsystem\":\"poctest\",\"tags\":[\"beats_input_codec_plain_applied\"],\"@timestamp\":\"2022-06-30T08:14:50.458Z\",\"ip\":\"192.168.70.92\",\"collecttime\":\"2022-06-30T08:14:50.458Z\",\"log\":{\"file\":{\"path\":\"/home/inoah-deploy/docker/run-all/logs/nginx/paas_access.log\"},\"offset\":78240771},\"@version\":\"1\",\"transip\":\"192.168.70.92\"}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        jsonObject.put("@timestamp", DateUtil.getUTCTimeStr());
        jsonObject.put("logTypeName","nginx_access_common");
        return jsonObject.toJSONString();
    }

    /**
     * 发送数据
     *
     * @param producer
     * @param topic
     * @param message
     * @param <T>
     */
    private static <T> void send(KafkaProducer<String, T> producer, String topic, T message) {
        ProducerRecord<String, T> producerRecord = new ProducerRecord<>(topic, null, message);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (null != exception) {
                    System.out.println(String.format("消息发送失败：%s", metadata.toString()));
                    exception.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) throws InterruptedException {
        String topic = "xiesen";
        String bootstrapServers = "192.168.70.6:39092,192.168.70.7:39092,192.168.70.8:39092";
        long records = 1000L;

        System.out.println(buildMsg());

        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());

        for (long index = 0; index < records; index++) {
            String message = buildMsg();
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);

    }
}
