package com.github.rabbitmq.test;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 谢森
 * @since 2021/5/28
 */
@Component
public class RabbitMqSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        Message message = new Message("hello".getBytes());
        rabbitTemplate.send("xiesen", "xiesen", message);
    }

}
