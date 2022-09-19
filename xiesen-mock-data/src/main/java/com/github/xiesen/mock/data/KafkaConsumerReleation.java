package com.github.xiesen.mock.data;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.xiesen.common.avro.AvroDeserializerFactory;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiese
 * @Description KafkaConsumerDemo
 * @Email xiesen310@163.com
 * @Date 2020/9/19 16:01
 */
public class KafkaConsumerReleation {
    public static void main(String[] args) {
        KafkaConsumer<String, byte[]> consumer = getStringKafkaConsumer();

//        consumer.subscribe(Collections.singletonList("skywalking-zork-relation"));
        consumer.subscribe(Collections.singletonList("skywalking-zork-span"));
        AtomicLong i = new AtomicLong();
        while (true) {
            //  从服务器开始拉取数据

            ConsumerRecords<String, byte[]> records = consumer.poll(100);

            records.forEach(record -> {
                GenericRecord value =
                        AvroDeserializerFactory.getLogsDeserializer().deserializing(record.value());
                i.getAndIncrement();

                /*if (value.get("metricsetname").equals("zork_error_data")) {
                    System.out.println(value);
                }*/
//                Map<String, String> dimensions = JSONUtil.toBean(JSONUtil.toJsonStr(value.get("dimensions")), new TypeReference<Map<String, String>>() {
//                }, true);
                Map<String,String> dimensions = (Map<String, String>) value.get("dimensions");
                dimensions.forEach((k,v) -> {
                    System.out.println(k + "-> " + v);
                });
//                Map<String, String> dimensions = JSONUtil.toBean(JSONUtil.toJsonStr(value.get("dimensions")), new TypeReference<Map<String, String>>() {
//                }, true);

                String endpoint = dimensions.get("endpoint");
                if (StrUtil.isBlank(endpoint)) {
                    System.out.println(value);
                    System.out.println("endpoint = " + endpoint);
                }else {
//                    System.out.println(value);
                }

                /*System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n", record.topic(),
                        record.partition(),
                        record.offset(), record.key(), AvroDeserializerFactory.getMetricDeserializer().deserializing
                        (record.value()));*/


                System.out.println("消费了 " + i + " 条数据");
            });
        }


    }

    private static KafkaConsumer<String, byte[]> getStringKafkaConsumer() {
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", "192.168.70.76:9092");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "xiesen");

        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        return consumer;
    }

    public static void main1(String[] args) {
        String a = "{\"ip\":\"192.168.70.27\",\"component\":\"Undertow\",\"endpoint\":\"/actuator/health\",\"hostname\":\"研发-国信-70.27\",\"operationname\":\"/actuator/health\",\"appsystem\":\"poctest\",\"servicename\":\"zork-apm\",\"calltype\":\"remote\",\"serviceinstance\":\"e5ec2ffce8b745fcb35cdd19e23bb89d@192.168.70.27\",\"status\":\"1\"}";
        Map<String, String> bean = JSONUtil.toBean(a, new TypeReference<Map<String, String>>() {
        }, true);
        System.out.println(bean.get("endpoint"));
    }
}
