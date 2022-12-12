package com.example.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式 消费者
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-09  15:11
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取TCP长连接
        Connection connection = RabbitUtil.getConnection();
        // 创建通信通道 相当于TCP中的虚拟连接
        Channel channel = connection.createChannel();
        /*
            如果队列名不存在 会自动创建
            P1:队列名称ID
            P2:是否持久化，false表示不持久化数据，MQ停掉数据就会丢失。
            P3:是否队列私有化，false表示所有消费者都可以访问，true表示只有第一次拥有该队列的消费者才能一直使用。
            P4:是否自动删除，false表示连接停掉后不自动删除这个队列。
            P5:其他参数
         */
        channel.queueDeclare(RabbitConstant.QUEUE_HELLO_WORLD, false, false, false, null);
        /*
            从MQ服务器中获取数据
            P1:队列名
            P2:是否自动确认收到消息，false表示需要手动编码确认消息（MQ推荐手动）。
            P3:DefaultConsumer的实现类对象 做消息处理
         */
        channel.basicConsume(RabbitConstant.QUEUE_HELLO_WORLD, false, new Receiver(channel));
    }
}
