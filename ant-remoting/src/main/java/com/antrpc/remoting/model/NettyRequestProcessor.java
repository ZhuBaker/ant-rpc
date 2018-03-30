package com.antrpc.remoting.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 16:19
 */
public interface NettyRequestProcessor {

    RemotingTransporter processRequest(ChannelHandlerContext ctx , RemotingTransporter request)
            throws Exception;


}
