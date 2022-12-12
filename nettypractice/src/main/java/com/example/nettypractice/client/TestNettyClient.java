package com.example.nettypractice.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.net.URL;

/**
 * @description: 测试netty客户端
 * @author: YangWanYi
 * @create: 2022-05-16 16:34
 **/
public class TestNettyClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup(); // 客户端需要一个事件循环组
        try {
            /*
                创建客户端启动对象
                注意客户端使用的不是ServerBootstrap，而是Bootstrap
             */
            Bootstrap bootstrap = new Bootstrap();
            String url = " ws://183.230.202.111:30018/websocket?projectId=5001120003&type=iot,text&getFullData=true";
            bootstrap.group(group) // 设置线程组
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel作为客户端的通道实现
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new TestNettyClientHandler(
                                            WebSocketClientHandshakerFactory.newHandshaker(new URL(url).toURI(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders())
                                    )
                            ); // 加入处理器
                        }
                    });
            System.out.println("netty client start…");
            // 启动客户端去连接服务器端
//            ws://183.230.202.111:30018/websocket?projectId=5001120003&type=iot,text&getFullData=true
            ChannelFuture channelFuture = bootstrap.connect("183.230.202.111", 30018).sync();
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
