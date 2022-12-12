package com.example.rabbitmq.workqueue;

import com.alibaba.fastjson.JSONObject;
import com.example.rabbitmq.helloworld.RabbitConstant;
import com.example.rabbitmq.helloworld.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 队列模式 生产者
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-09  16:41
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
        int times = 100;
        for (int i = 1; i < times; i++) {
            // 封装要发送的消息
            MessageVo messageVo = new MessageVo("骑手您好！", "平安路" + i + "号", "您有一个新的订单待配送！");
            String msg = JSONObject.toJSONString(messageVo);
            /*
                发送消息
                P1:交换机 队列模式不需要指定交换机
                P2:队列名称
                P3:其他参数
                P4:要发送的消息
             */
            channel.basicPublish("", RabbitConstant.QUEUE_HELLO_WORLD, null, msg.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("队列模式，消息发送成功！");
        channel.close();
        connection.close();
    }
}
