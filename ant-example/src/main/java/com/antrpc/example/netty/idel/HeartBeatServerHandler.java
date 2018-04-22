package com.antrpc.example.netty.idel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: BakerZhu
 * @Date: 2018/4/22 13:46
 * @Description:
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server channelRead..");
        System.out.println(ctx.channel().remoteAddress() + "->Server :" + msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
