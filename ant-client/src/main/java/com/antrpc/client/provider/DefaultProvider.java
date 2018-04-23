package com.antrpc.client.provider;

import com.antrpc.common.exception.remoting.RemotingException;
import com.antrpc.remoting.model.RemotingTransporter;
import com.antrpc.remoting.netty.NettyClientConfig;
import com.antrpc.remoting.netty.NettyRemotingClient;
import com.antrpc.remoting.netty.NettyRemotingServer;
import com.antrpc.remoting.netty.NettyServerConfig;
import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * @author: zhubo
 * @description: provider端的接口
 * @time: 2018-04-23
 * @modifytime:
 */
public class DefaultProvider implements Provider {


    private NettyClientConfig clientConfig;               // 向注册中心连接的netty client配置
    private NettyServerConfig serverConfig; 			  // 等待服务提供者连接的netty server的配置
    private NettyRemotingClient nettyRemotingClient; 	  // 连接monitor和注册中心
    private NettyRemotingServer nettyRemotingServer;      // 等待被Consumer连接
    private NettyRemotingServer nettyRemotingVipServer;   // 等待被Consumer VIP连接
    private ProviderRegistryController providerController;// provider端向注册中心连接的业务逻辑的控制器







    /**
     * 启动provider的实例
     *
     * @throws RemotingException
     * @throws InterruptedException
     */
    @Override
    public void start() throws InterruptedException, RemotingException {

    }

    /**
     * 发布服务
     *
     * @throws InterruptedException
     * @throws RemotingException
     */
    @Override
    public void publishedAndStartProvider() throws InterruptedException, RemotingException {

    }

    /**
     * 暴露服务的地址
     *
     * @param exposePort
     * @return
     */
    @Override
    public Provider serviceListenPort(int exposePort) {
        return null;
    }

    /**
     * 设置注册中心的地址  host:port,host1:port1
     *
     * @param registryAddress
     * @return
     */
    @Override
    public Provider registryAddress(String registryAddress) {
        return null;
    }

    /**
     * 监控中心的地址，不是强依赖，不设置也没有关系
     *
     * @param monitorAddress
     * @return
     */
    @Override
    public Provider monitorAddress(String monitorAddress) {
        return null;
    }

    /**
     * 需要暴露的实例
     *
     * @param obj
     */
    @Override
    public Provider publishService(Object... obj) {
        return null;
    }

    /**
     * 处理消费者的rpc请求
     *
     * @param request
     * @param channel
     * @return
     */
    @Override
    public void handlerRPCRequest(RemotingTransporter request, Channel channel) {

    }
}
