package com.example.nettypractice.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @description: 自定义netty处理器
 * @author: YangWanYi
 * @create: 2022-05-16 16:23
 **/
public class TestNettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象，含有通道channel，管道pipeline
     * @param msg 客户端发送的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程：" + Thread.currentThread().getName());
        ByteBuf buf = (ByteBuf) msg; // 把msg转成ByteBuf，类似NIO的ByteBuffer
        System.out.println("客户端发消息说：" + buf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕处理方法
     *
     * @param ctx 上下文对象，含有通道channel，管道pipeline
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = Unpooled.copiedBuffer("你好！客户端！", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
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
        ctx.close();
    }

}
