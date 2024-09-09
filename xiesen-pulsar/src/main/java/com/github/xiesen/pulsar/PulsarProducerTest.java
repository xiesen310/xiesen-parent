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
    public static final String PULSAR_URL = "pulsar://192.168.80.21:31286";

    public static void main(String[] args) throws PulsarClientException, InterruptedException {

        PulsarClient client = PulsarClient.builder()
                .serviceUrl(PULSAR_URL)
                .build();


        Producer<String> producer = client.newProducer(Schema.STRING)
                .topic("my-topic")
                .create();
        for (int i = 0; i < 100; i++) {
            producer.send("My message" + i);
            Thread.sleep(1000);
        }

        producer.close();
        client.close();
    }
}
