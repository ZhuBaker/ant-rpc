package com.antrpc.example.netty.idel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: BakerZhu
 * @Date: 2018/4/22 14:02
 * @Description:
 */
public class ChannelInactiveHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("============channelInactive============");
        super.channelInactive(ctx);
    }
}
