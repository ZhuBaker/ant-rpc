/*
package com.antrpc.client.provider;

import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.remoting.ConnectionUtils;
import com.antrpc.remoting.model.NettyRequestProcessor;
import com.antrpc.remoting.model.RemotingTransporter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

*/
/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: provider端注册的处理器
 * @time: 2018年04月24日
 * @modifytime:
 *//*

public class DefaultProviderRegistryProcessor implements NettyRequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProviderRegistryProcessor.class);

    private DefaultProvider defaultProvider;

    public DefaultProviderRegistryProcessor(DefaultProvider defaultProvider) {
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
            case AntProtocal.DEGRADE_SERVICE:
                return this.defaultProvider.handlerDegradeServiceRequest(request,ctx.channel(),AntProtocal.DEGRADE_SERVICE);
            case AntProtocal.AUTO_DEGRADE_SERVICE:
                return this.defaultProvider.handlerDegradeServiceRequest(request,ctx.channel(),AntProtocal.AUTO_DEGRADE_SERVICE);
        }
        return null;
    }
}
*/
