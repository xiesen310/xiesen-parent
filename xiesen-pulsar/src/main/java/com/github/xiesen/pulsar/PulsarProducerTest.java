package com.github.xiesen.pulsar;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;

/**
 * @author 谢森
 * @Description PulsarTest
 * @Email xiesen310@163.com
 * @Date 2021/1/23 18:33
 */
public class PulsarProducerTest {
    public static void main(String[] args) throws PulsarClientException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://192.168.42.140:6650")
                .build();


        Producer<String> stringProducer = client.newProducer(Schema.STRING)
                .topic("my-topic")
                .create();
        for (int i = 0; i < 10; i++) {
            stringProducer.send("My message" + i);
        }

        stringProducer.close();
        client.close();
    }
}
