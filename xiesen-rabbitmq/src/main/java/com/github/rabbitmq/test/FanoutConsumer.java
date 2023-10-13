package com.github.rabbitmq.test;

import com.github.rabbitmq.test.util.RabbitmqListenerUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author xiesen
 */
public class FanoutConsumer {
    private static final String EXCHANGE_NAME = "smartdata.datarelationship.e.q.f";
    private static final String QUEUE_NAME = "fanout_queue";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String ADDRESS = "192.168.70.42:5672";

    public static void main(String[] args) throws Exception {
        final RabbitmqListenerUtils rabbitmqListenerUtils = new RabbitmqListenerUtils(ADDRESS, USERNAME, PASSWORD);
        final Channel channel = rabbitmqListenerUtils.getChannel();
//        test1(EXCHANGE_NAME, channel);
        consumerMessage(EXCHANGE_NAME, channel);
    }

    private static void test1(String exchange, Channel channel) throws IOException {
        final String queueName = Thread.currentThread().getName();
        System.out.println("test1: " + queueName);

        // 声明交换器
        channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);

        // 声明队列
        channel.queueDeclare(queueName, false, false, false, null);

        // 将队列绑定到交换器
//        channel.queueBind(queueName, exchange, "");

        System.out.println("Waiting for messages...");

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException, UnsupportedEncodingException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received message: " + message);
            }
        };

        // 开始消费消息
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    public static void consumerMessage(String exchange, Channel channel) throws Exception {
        //获取一个临时队列
        String queueName = channel.queueDeclare().getQueue();
//        String queueName = "streamx-" + Thread.currentThread().getName();
        System.out.println("consumerMessage: " + queueName);
        //在信道中设置交换器
        channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true);
//        channel.queueDeclare(queueName, false, false, true, null);
        channel.queueBind(queueName, exchange, "");

        channel.basicQos(1);

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException, UnsupportedEncodingException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received message: " + message);
            }
        };

        //回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(queueName, false, consumer);


    }
}
