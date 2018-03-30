package com.antrpc.remoting;

import com.antrpc.remoting.model.RemotingResponse;

/**
 * Created with IntelliJ IDEA.
 * Description: 远程调用之后的回调函数
 * User: zhubo
 * Date: 2018-03-29
 * Time: 17:22
 */
public interface InvokeCallback {

    void operationComplete(final RemotingResponse remotingResponse);
}
