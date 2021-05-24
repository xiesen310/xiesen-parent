package com.github.xiesen.mock.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @since 2021/5/19
 */
public class MockElasticsearchSinkJsonDataSong {
    private static final String[] IDS = new String[]{"AO", "AF", "AL", "DZ", "AD", "AI", "AG", "AR", "AM", "AU", "AT"
            , "AZ", "BS", "BH",
            "BD", "BB", "BY", "BE", "BZ", "BJ", "BM", "BO", "BW", "BR", "BN", "BG", "BF", "MM", "BI", "CM", "CA", "CF"
            , "TD", "CL", "CN", "CO", "CG", "CK", "CR", "CU", "CY", "CZ", "DK", "DJ", "DO", "EC", "EG", "SV", "EE",
            "ET", "FJ", "FI", "FR", "GF", "GA", "GM", "GE", "DE", "GH", "GI", "GR", "GD", "GU", "GT", "GN", "GY", "HT"
            , "HN", "HK", "HU", "IS", "IN", "ID", "IR", "IQ", "IE", "IL", "IT", "JM", "JP", "JO", "KH", "KZ", "KE",
            "KR", "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LI", "LT", "LU", "MO", "MG", "MW", "MY", "MV", "ML"
            , "MT", "MU", "MX", "MD", "MC", "MN", "MS", "MA", "MZ", "NA", "NR", "NP", "NL", "NZ", "NI", "NE", "NG",
            "KP", "NO", "OM", "PK", "PA", "PG", "PY", "PE", "PH", "PL", "PF", "PT", "PR", "QA", "RO", "RU", "LC", "VC"
            , "SM", "ST", "SA", "SN", "SC", "SL", "SG", "SK", "SI", "SB", "SO", "ZA", "ES", "LK", "SD", "SR", "SZ",
            "SE", "CH", "SY", "TW", "TJ", "TZ", "TH", "TG", "TO", "TT", "TN", "TR", "TM", "UG", "UA", "AE", "GB", "US"
            , "UY", "UZ", "VE", "VN", "YE", "YU", "ZW", "ZR", "ZM"};

    private static final String[] NAMES = new String[]{"闫明", "苏岩", "朱志刚", "朱明磊", "刘阿康", "宋倩倩", "荣权", "宋志鹏",
            "王帝", "崔武", "尹丹丽"};

    private String getRandomCountryCode() {
        return IDS[new Random().nextInt(IDS.length)];
    }

    private String getRandomName() {
        return NAMES[new Random().nextInt(NAMES.length)];
    }

    private String getRandomAddress() {
        return "步行街" + new Random().nextInt(1000) + "号";
    }

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
        /*props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");
        Configuration.setConfiguration(new SaslConfig("admin", "admin"));*/


        return new KafkaProducer<>(props);
    }


    /**
     * {"country_code":"TW","name":"荣权","id":"635a273a-333d-4d77-b903-327c103a4bb3","age":41}
     *
     * @return
     */
    public String buildMsg() {
        JSONObject bigJson = new JSONObject();
        /*bigJson.put("id", (new Random().nextInt(100)+1));
        bigJson.put("name", getRandomName());
        bigJson.put("age", new Random().nextInt(100));
        bigJson.put("country_code", getRandomCountryCode());*/
        bigJson.put("hostname", ("hostname"+new Random().nextInt(100)+1));
        bigJson.put("ip", "192.168.70."+ new Random().nextInt(600)+11);
        bigJson.put("appsystem", "dev_test");
        bigJson.put("servicename", "linux模块");
        bigJson.put("clustername", "jichujiankong");
        bigJson.put("appprogramname", "linuxmokuai");
        bigJson.put("timestamp", System.currentTimeMillis());
        return bigJson.toJSONString();
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
        String topic = "xiesen_song";
        //String topic = "song_test_hbase";
        //String topic = "caobaode08";
        String bootstrapServers = "kafka-1:19092,kafka-2:19092,kafka-3:19092";
        //String bootstrapServers = "192.168.30.54:9092";
        long records = 2000L;
        MockElasticsearchSinkJsonDataSong data = new MockElasticsearchSinkJsonDataSong();

        KafkaProducer<String, String> producer = buildProducer(bootstrapServers, StringSerializer.class.getName());

        for (long index = 0; index < records; index++) {
            String message = data.buildMsg();
            System.out.println(message);
            send(producer, topic, message);
            TimeUnit.SECONDS.sleep(1);
        }

        producer.flush();
        producer.close();
        Thread.sleep(1000L);

    }
}
