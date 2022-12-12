package com.example.rabbitmq.pubsub;

import com.example.rabbitmq.helloworld.RabbitConstant;
import com.example.rabbitmq.helloworld.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式 生产者
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-10  13:28
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取TCP长连接
        Connection connection = RabbitUtil.getConnection();
        // 获取输入信息
        String next = new Scanner(System.in).next();
        // 创建通信通道 相当于TCP中的虚拟连接
        Channel channel = connection.createChannel();
        /*
            发送消息
            P1:交换机名称
            P2:队列名称 发布订阅模式在发布消息时不需要指定队列名，在消费端指定队列名即可
            P3:其他参数
            P4:要发送的消息
         */
        channel.basicPublish(RabbitConstant.EXCHANGE_NEWS, "", null, next.getBytes(StandardCharsets.UTF_8));
        // 关闭连接
        channel.close();
        connection.close();
    }
}
