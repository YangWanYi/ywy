package com.example.eurekaclient01.config;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

/**
 * @description: 指令下发记录netty客户端处理器
 * @author: YangWanYi
 * @create: 2022-05-13 19:23
 **/
/*
    因为一个ChannelHandler可以从属于多个ChannelPipeline，所以它也可以绑定到多个ChannelHandlerContext实例。
    用于这种用法的ChannelHandler必须要使用@Sharable注解标注；否则，试图将它添加到多个ChannelPipeline时将会触发异常。
    为了安全地被用于多个并发的Channel（即连接），这样的ChannelHandler必须是线程安全的。
 */
@ChannelHandler.Sharable
public class TestHandler extends ChannelInboundHandlerAdapter {

    private ChannelPromise handshakeFuture;

    private final WebSocketClientHandshaker handShaker;

    public TestHandler(WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    /**
     * 当客户端连接服务器完成就会触发
     *
     * @param ctx 上下文对象，含有通道channel，管道pipeline
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("通道开启 .....");
        handShaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("channelInactive");
        ctx.pipeline().remove(this);
        ctx.channel().close();
    }


    /**
     * 当通道有读取事件时会触发，即服务端发送数据给客户端时会触发
     *
     * @param ctx 上下文对象，含有通道channel，管道pipeline
     * @param msg 服务端发送的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();
        if (!handShaker.isHandshakeComplete()) {
            try {
                handShaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("websocket Handshake 完成!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                System.out.println("websocket连接失败!");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            response.release(); // 释放
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            System.out.println(textFrame.text());
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            textFrame.release(); // 释放
        } else if (frame instanceof PongWebSocketFrame) {
        } else if (frame instanceof CloseWebSocketFrame) {
            ch.close();
        }
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
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

}
