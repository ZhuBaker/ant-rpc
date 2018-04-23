package com.antrpc.client.provider;

import com.antrpc.common.exception.remoting.RemotingException;
import com.antrpc.remoting.model.RemotingTransporter;
import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * @author: zhubo
 * @description: provider端的接口
 * 1)需要暴露哪些服务【必要】{@link Provider#publishService(Object... obj)}
 * 2)暴露服务在哪个端口上提供【必要】{@link Provider#serviceListenPort(int)}
 * 3)设置注册中心地址【必要】{@link Provider#registryAddress(String)}
 * 4)暴露启动服务提供者的方法【必须调用】{@link Provider#start()}
 * 5)设置provider端提供的监控地址【非必要】{@link Provider#monitorAddress(String)}
 * @time: 2018-04-23
 * @modifytime:
 */
public interface Provider {

    /**
     * 启动provider的实例
     * @throws RemotingException
     * @throws InterruptedException
     */
    void start() throws InterruptedException, RemotingException;


    /**
     * 发布服务
     * @throws InterruptedException
     * @throws RemotingException
     */
    void publishedAndStartProvider() throws InterruptedException, RemotingException;

    /**
     * 暴露服务的地址
     * @param exposePort
     * @return
     */
    Provider serviceListenPort(int exposePort);


    /**
     * 设置注册中心的地址  host:port,host1:port1
     * @param registryAddress
     * @return
     */
    Provider registryAddress(String registryAddress);

    /**
     * 监控中心的地址，不是强依赖，不设置也没有关系
     * @param monitorAddress
     * @return
     */
    Provider monitorAddress(String monitorAddress);

    /**
     * 需要暴露的实例
     * @param obj
     */
    Provider publishService(Object ...obj);

    /**
     * 处理消费者的rpc请求
     * @param request
     * @param channel
     * @return
     */
    void handlerRPCRequest(RemotingTransporter request, Channel channel);










}
