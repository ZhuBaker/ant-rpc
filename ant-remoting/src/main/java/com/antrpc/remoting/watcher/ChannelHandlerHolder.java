package com.antrpc.remoting.watcher;

import io.netty.channel.ChannelHandler;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-30
 * Time: 10:06
 */
public interface ChannelHandlerHolder {

    ChannelHandler[] handlers();
}
