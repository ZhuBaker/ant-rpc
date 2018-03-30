package com.antrpc.remoting.model;

import com.antrpc.common.exception.remoting.RemotingSendRequestException;
import com.antrpc.common.exception.remoting.RemotingTimeoutException;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * Description: 处理channel关闭 或者 inactive状态的时候改变
 * User: zhubo
 * Date: 2018-03-29
 * Time: 16:30
 */
public interface NettyChannelInactiveProcessor {

    void processChannelInactive(ChannelHandlerContext ctx) throws RemotingSendRequestException, RemotingTimeoutException, InterruptedException;
}
