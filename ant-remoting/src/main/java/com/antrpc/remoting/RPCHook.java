package com.antrpc.remoting;

import com.antrpc.remoting.model.RemotingTransporter;

/**
 * Created with IntelliJ IDEA.
 * Description: RPC 回调钩子，在发送请求和接收请求时出发，这样做增加程序的健壮性和灵活性
 * User: zhubo
 * Date: 2018-03-29
 * Time: 15:15
 */
public interface RPCHook {

    void doBeforeRequest(final String remoteAddr , final RemotingTransporter request);

    void doAfterResponse(final String remoteAddr , final RemotingTransporter request , final RemotingTransporter response);

}
