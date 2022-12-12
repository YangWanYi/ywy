package com.example.nettypractice.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.util.CharsetUtil;

/**
 * @description: netty客户端处理器
 * @author: YangWanYi
 * @create: 2022-05-16 16:39
 **/
public class TestNettyClientHandler extends ChannelInboundHandlerAdapter {

    private final WebSocketClientHandshaker handShaker;

    public TestNettyClientHandler(WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker;
    }

    /**
     * 当客户端连接服务器完成就会触发
     *
     * @param ctx 上下文对象，含有通道channel，管道pipeline
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("指令下发记录netty客户端与服务端建立连接，通道开启 .....");
        handShaker.handshake(ctx.channel());
    }

    /**
     * 当通道有读取事件时会触发，即服务端发送数据给客户端时会触发
     *
     * @param ctx 上下文对象，含有通道channel，管道pipeline
     * @param msg 服务端发送的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务端发消息：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("服务端的地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 处理异常，一般是需要关闭通道
     *
     * @param ctx   上下文对象，含有通道channel，管道pipeline
     * @param cause 异常信息
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
