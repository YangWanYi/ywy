package com.example.rabbitmq.topic;

import com.example.rabbitmq.helloworld.RabbitConstant;
import com.example.rabbitmq.helloworld.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 主题模式 生产者
 * 主题模式的交换机类型是topic
 * @author YangWanYi
 * @version 1.0
 * @date 2022/12/10 22:29
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 模拟要发送的消息 key：routing-key value：消息详情
        Map<String, String> messages = new HashMap<>(10);
        int times = 10;
        for (int i = 0; i < times; i++) {
            messages.put("routing.key." + (i + 1), "消息详情" + (i + 1));
        }
        // 获取TCP长连接
        Connection connection = RabbitUtil.getConnection();
        // 创建通信通道 相当于TCP中的虚拟连接
        Channel channel = connection.createChannel();
        Iterator<Map.Entry<String, String>> iterator = messages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            /*
                发送消息
                P1:交换机名称
                P2:routing key
                P3:其他参数
                P4:要发送的消息
             */
            channel.basicPublish(RabbitConstant.EXCHANGE_TOPIC, entry.getKey(), null, entry.getValue().getBytes(StandardCharsets.UTF_8));
        }
        // 关闭连接
        channel.close();
        connection.close();
    }
}
