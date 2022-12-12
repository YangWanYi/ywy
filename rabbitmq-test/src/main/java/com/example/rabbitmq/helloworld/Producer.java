package com.example.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式 发布者
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-09  14:49
 */
public class Producer {
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
        // 定义需要发送的消息
        String msg = "超多钱！";
        /*
            发送消息
            P1:交换机 简单模式不需要指定交换机
            P2:队列名称
            P3:其他参数
            P4:要发送的消息
         */
        channel.basicPublish("", RabbitConstant.QUEUE_HELLO_WORLD, null, msg.getBytes(StandardCharsets.UTF_8));
        // 关闭连接
        channel.close();
        connection.close();
        System.out.println("恭喜！消息发送成功！");
    }
}
