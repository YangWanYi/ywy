package com.example.rabbitmq.topic;

import com.example.rabbitmq.helloworld.RabbitConstant;
import com.example.rabbitmq.helloworld.RabbitUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 主题模式 消费者2号
 * topic模式与routing模式都可以根据routing key把消息路由到不同的队列，
 * 只是topic类型的交换机可以让队列在绑定routing key的时候使用通配符。
 * 通配符规则：
 * # 匹配一个或多个词；
 * * 匹配一个词。
 * 比如text.#能匹配text.abc或text.abc.123，text.*只能匹配text.abc。
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022/12/10 22:29
 */
public class Consumer2 {

    public static void main(String[] args) throws IOException {
        // 获取TCP长连接
        Connection connection = RabbitUtil.getConnection();
        // 创建通信通道 相当于TCP中的虚拟连接
        final Channel channel = connection.createChannel();
        /*
            声明队列
            P1:队列名称 如果队列名不存在 会自动创建
            P2:是否持久化，false表示不持久化数据，MQ停掉数据就会丢失。
            P3:是否队列私有化，false表示所有消费者都可以访问，true表示只有第一次拥有该队列的消费者才能一直使用。
            P4:是否自动删除，false表示连接停掉后不自动删除这个队列。
            P5:其他参数
         */
        channel.queueDeclare(RabbitConstant.QUEUE_ROUTING_2, false, false, false, null);
        /*
            绑定交换机
            P1:队列名
            P2:交换机名 必须先创建好，否则会报错。路由模式的交换机类型是direct（point-to-point）。
            P3:路由key 发布订阅模式还用不到这个参数
         */
        channel.queueBind(RabbitConstant.QUEUE_ROUTING_2, RabbitConstant.EXCHANGE_TOPIC, "*.*.1");
         /*
            如果不写basicQos(1)，MQ会自动把所有的消息平均发给所有的消费者。
            写了，MQ不再对消费者一次性发送多个消息，而是消费者处理完一个消息后（确认后），再从队列里获取一个新的。
         */
        channel.basicQos(1);
         /*
            从MQ服务器中获取数据
            P1:队列名
            P2:是否自动确认收到消息，false表示需要手动编码确认消息（MQ推荐手动）。
            P3:DefaultConsumer的实现类对象 做消息处理
         */
        channel.basicConsume(RabbitConstant.QUEUE_ROUTING_2, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 处理消息
                String msg = new String(body);
                System.out.println("主题模式消费者2号收到消息：" + msg);
                 /*
                    签收消息
                    P1:消息ID
                    P2:false表示只确认签收当前的消息 true表示签收该消费者所有未签收的消息
                 */
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
