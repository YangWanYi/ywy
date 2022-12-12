package com.ywy.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * 队列模式消费者客户端监听器
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-11  00:28
 */
public class FanoutListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("队列模式消费者客户端收到消息：" + new String(message.getBody()));
    }
}
