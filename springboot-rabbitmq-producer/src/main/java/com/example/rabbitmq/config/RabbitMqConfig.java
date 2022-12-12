package com.example.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置文件
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-11  14:57
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 定义交换机名称
     */
    public static final String EXCHANGE_NAME = "boot_topic_exchange";

    /**
     * 定义队列名称
     */
    public static final String QUEUE_NAME = "boot_queue";

    /**
     * 声明交换机
     *
     * @return org.springframework.amqp.core.Exchange
     * @author YangWanYi
     * @date 2022/12/11 15:04
     */
    @Bean
    public Exchange declareExchange() {
        // P1:交换机名称 P2:是否持久化
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 声明队列
     *
     * @return org.springframework.amqp.core.Queue
     * @author YangWanYi
     * @date 2022/12/11 15:07
     */
    @Bean
    public Queue declareQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    /**
     * 绑定队列与交换机
     *
     * @param queue    队列
     * @param exchange 交换机
     * @return org.springframework.amqp.core.Binding
     * @author YangWanYi
     * @date 2022/12/11 15:13
     */
    @Bean
    public Binding bindQueueAndExchange(@Qualifier("declareQueue") Queue queue, @Qualifier("declareExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

}
