package com.example.nettypractice.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;

import java.net.URI;

/**
 * @description: 指令下发记录netty客户端
 * @author: YangWanYi
 * @create: 2022-05-13 19:21
 **/
public class CommandRecordNettyClient {

    public static void main(String[] args) {
        connect();
    }


    public static void connect(){
        String ip = "183.230.202.111";
        Integer socketPort = 30018;
        EventLoopGroup group = new NioEventLoopGroup(); // 客户端需要一个事件循环组
        try {
            String url = "ws://183.230.202.111:30018/websocket?projectId=5001120003&type=iot,text&getFullData=true";
            CommandRecordNettyClientHandler handler =
                    new CommandRecordNettyClientHandler(WebSocketClientHandshakerFactory.newHandshaker(new URI(url), WebSocketVersion.V13, null,
                            true, new DefaultHttpHeaders()));
            Bootstrap bootstrap = new Bootstrap(); // 创建客户端启动对象 注意客户端使用的不是ServerBootstrap，而是Bootstrap
            bootstrap.group(group) // 设置线程组
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel作为客户端的通道实现
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new HttpClientCodec(), // 解码成HttpRequest
                                    new HttpObjectAggregator(1024 * 10), // 解码成FullHttpRequest
                                    WebSocketClientCompressionHandler.INSTANCE,
                                    handler); // 加入自定义处理器
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(ip, socketPort).sync(); // 启动客户端去连接服务器端
            channelFuture.addListener((ChannelFutureListener) future -> {
                Channel channel = channelFuture.channel();
                if (null != channel && channel.isActive()) {
                    System.out.println("netty client连接成功");
                }
            });
            handler.handshakeFuture().sync();
            channelFuture.channel().closeFuture().sync(); // 监听关闭通道
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
