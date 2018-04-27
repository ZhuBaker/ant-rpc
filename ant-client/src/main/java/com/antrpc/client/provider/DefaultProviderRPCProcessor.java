package com.antrpc.client.provider;

import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.remoting.ConnectionUtils;
import com.antrpc.remoting.model.NettyRequestProcessor;
import com.antrpc.remoting.model.RemotingTransporter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年04月24日
 * @modifytime:
 */
public class DefaultProviderRPCProcessor implements NettyRequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProviderRPCProcessor.class);

    private DefaultProvider defaultProvider;

    public DefaultProviderRPCProcessor(DefaultProvider defaultProvider) {
        this.defaultProvider = defaultProvider;
    }

    @Override
    public RemotingTransporter processRequest(ChannelHandlerContext ctx, RemotingTransporter request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("receive request, {} {} {}",//
                    request.getCode(), //
                    ConnectionUtils.parseChannelRemoteAddr(ctx.channel()), //
                    request);
        }

        switch (request.getCode()) {
            case AntProtocal.RPC_REQUEST:
                //这边稍微特殊处理一下，可以返回null,我们不需要叫外层代码帮我们writeAndFlush 发出请求，因为我们持有channel，这样做rpc可以更加灵活一点
                this.defaultProvider.handlerRPCRequest(request,ctx.channel());
                break;
        }
        return null;
    }


}
