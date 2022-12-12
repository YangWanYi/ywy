package com.example.springbootrabbitmqproducer;

import com.example.rabbitmq.config.RabbitMqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootRabbitmqProducerApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void sendMSg() {
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME,"boot.money","超多钱超多钱……");
    }

}
