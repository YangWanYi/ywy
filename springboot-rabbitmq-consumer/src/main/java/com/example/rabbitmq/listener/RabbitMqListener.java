package com.example.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ监听器
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-11  15:54
 */
@Component
public class RabbitMqListener {

    /**
     * 监听消息
     *
     * @param msg 收到的消息
     * @return void
     * @author YangWanYi
     * @date 2022/12/11 15:56
     */
    @RabbitListener(queues = "boot_queue")
    public void listenQueue(Message msg) {
        System.out.println("springboot消费端收到消息：" + msg);
    }

}
