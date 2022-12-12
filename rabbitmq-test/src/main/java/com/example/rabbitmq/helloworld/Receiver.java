package com.example.rabbitmq.helloworld;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 消费对象实现类 做消息处理
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-09  15:16
 */
public class Receiver extends DefaultConsumer {

    private Channel channel;

    /**
     * 重写构造函数 Channel通道对象需要从外层传入，在handleDelivery中需要用到
     *
     * @param channel
     */
    public Receiver(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String msg = new String(body);
        System.out.println("消费者接收到的消息：" + msg);
        System.out.println("消费者接收到的消息ID：" + envelope.getDeliveryTag());
        /*
            签收消息
            P1:消息ID
            P2:false表示只确认签收当前的消息 true表示签收该消费者所有未签收的消息
         */
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}
