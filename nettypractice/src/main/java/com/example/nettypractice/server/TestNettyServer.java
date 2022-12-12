package com.example.nettypractice.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: netty服务端测试
 * @author: YangWanYi
 * @create: 2022-05-16 15:43
 **/
public class TestNettyServer {
    public static void main(String[] args) {
        /*
            创建两个线程组 bossGroup和workerGroup，含有的子线程NioEventLoop的个数默认为cpu核数的两倍
            bossGroup只是处理连接请求，真正和客户端的业务处理会交给workerGroup
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap(); // 创建服务端的启动对象
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务器的通道实现
                    // 初始化服务器连接队列大小，服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接。多个客户端同时来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理。
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建通道初始化对象，设置初始化参数
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TestNettyServerHandler()); // 对workerGroup的SocketChannel设置处理器
                        }
                    });
            System.out.println("netty server start…");
            /*
                 绑定一个端口并且同步，生成一个ChannelFuture异步对象，通过isDone()等方法可以判断异步事件的执行情况
                 启动服务器并绑定端口，bind是异步操作，sync方法是等待异步操作执行完毕
             */
            ChannelFuture channelFuture = bootstrap.bind(9997).sync();
            // 给channelFuture注册监听器
            channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("监听端口9997成功");
                } else {
                    System.out.println("监听端口9997失败");
                }
            });
            /*
                对通道关闭进行监听，closeFuture是异步操作，监听通道关闭
                通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成
             */
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("netty server failure…");
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
