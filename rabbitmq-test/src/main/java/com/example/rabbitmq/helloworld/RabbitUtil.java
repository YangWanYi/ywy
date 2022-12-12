package com.example.rabbitmq.helloworld;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Rabbit工具类
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-09  16:41
 */
public class RabbitUtil {

    private static ConnectionFactory connectionFactory = new ConnectionFactory();

    static {
        // 设置主机信息
        connectionFactory.setHost("192.168.0.42");
        /*
            端口
            5672:RabbitMQ的通讯端口
            25672:RabbitMQ的节点间的CLI通讯端口
            15672:RabbitMQ HTTP_API的端口，管理员用户才能访问，用于管理RabbitMQ,需要启动Management插件。
            1883、8883:MQTT插件启动时的端口。
            61613、61614:STOMP客户端插件启用的时候的端口。
            15674、15675:基于webscoket的STOMP端口和MOTT端口。
         */
        connectionFactory.setPort(5672);
        // 这里最好使用建VirtualHost的用户登陆 否则可能找不到这个VirtualHost
        connectionFactory.setUsername("ywy");
        connectionFactory.setPassword("ywy");
        connectionFactory.setVirtualHost("myHost");
    }

    /**
     * 获取TCP连接
     *
     * @return com.rabbitmq.client.Connection
     * @author YangWanYi
     * @date 2022/12/9 16:33
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
