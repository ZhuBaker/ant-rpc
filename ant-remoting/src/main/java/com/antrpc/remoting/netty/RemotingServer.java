package com.antrpc.remoting.netty;

import com.antrpc.common.exception.remoting.RemotingSendRequestException;
import com.antrpc.common.exception.remoting.RemotingTimeoutException;
import com.antrpc.remoting.model.NettyChannelInactiveProcessor;
import com.antrpc.remoting.model.NettyRequestProcessor;
import com.antrpc.remoting.model.RemotingTransporter;
import io.netty.channel.Channel;
import javafx.util.Pair;

import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * Description: Netty客户端的一些抽象方法
 * 1)作为服务端自然要处理来自客户端请求的一些请求，每一个请求都会有一个与之对应的处理器
 * 2)这样做的好处就是简化了netty的handler的配置，将handler中的业务逻辑放置到每一个对应的处理器中来
 * User: zhubo
 * Date: 2018-03-29
 * Time: 16:12
 */
public interface RemotingServer extends BaseRemotingService {

    void registerProecessor(final byte requestCode, final NettyRequestProcessor processor,final ExecutorService executor);

    void registerChannelInactiveProcessor(final NettyChannelInactiveProcessor processor, final ExecutorService executor);

    void registerDefaultProcessor(final NettyRequestProcessor processor, final ExecutorService executor);

    Pair<NettyRequestProcessor, ExecutorService> getProcessorPair(final int requestCode);

    RemotingTransporter invokeSync(final Channel channel, final RemotingTransporter request, final long timeoutMillis) throws InterruptedException, RemotingSendRequestException,
            RemotingTimeoutException;

}
