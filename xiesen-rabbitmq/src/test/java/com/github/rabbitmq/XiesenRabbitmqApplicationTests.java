package com.github.rabbitmq;

import com.github.rabbitmq.test.RabbitMqSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBootConfiguration
class XiesenRabbitmqApplicationTests {
    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Test
    void send() {
        rabbitMqSender.send();
    }

}
