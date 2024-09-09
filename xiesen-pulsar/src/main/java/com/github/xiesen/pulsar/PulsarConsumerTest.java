package com.github.xiesen.pulsar;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * @author 谢森
 * @Description PulsarConsumerTest
 * @Email xiesen310@163.com
 * @Date 2021/1/23 18:42
 */
public class PulsarConsumerTest {
    public static final String PULSAR_URL = "pulsar://192.168.80.21:31286";
    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(PULSAR_URL)
                .build();

        Consumer consumer = client.newConsumer()
//                .topic("my-topic")
                .topic("flink-topic")
                .subscriptionName("my-subscription")
                .subscribe();

        while (true) {
            // Wait for a message
            Message msg = consumer.receive();

            try {
                // Do something with the message
                System.out.println("Message received: " + new String(msg.getData()));

                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg);
            } catch (Exception e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        }
    }

}
