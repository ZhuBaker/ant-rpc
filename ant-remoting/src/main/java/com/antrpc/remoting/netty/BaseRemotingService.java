package com.antrpc.remoting.netty;

import com.antrpc.remoting.RPCHook;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 15:13
 */
public interface BaseRemotingService {

    /**
     * Netty参数初始化工作
     */
    void init();

    /**
     * 启动Netty方法
     */
    void start();

    /**
     * 关闭Netty C/S 实例
     */
    void shutdown();

    /**
     * 注入钩子，netty在处理过程中可以嵌入一些方法，增加代码的灵活性
     * @param rpcHook
     */
    void registerRPCHook(RPCHook rpcHook);
}
