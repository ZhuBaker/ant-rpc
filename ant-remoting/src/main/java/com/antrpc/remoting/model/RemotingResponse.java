package com.antrpc.remoting.model;

import com.antrpc.remoting.InvokeCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 17:19
 */
public class RemotingResponse {

    // 远程端返回的结果集
    private volatile RemotingTransporter remotingTransporter;
    // 该请求抛出的异常，如果存在的话
    private volatile Throwable cause;
    // 发送端是否发送成功
    private volatile boolean sendRequestOK = true;

    //请求的 opaque
    private final long opaque;
    // 默认回调函数
    private final InvokeCallback invokeCallback;
    //请求的默认超时时间
    private final long timeoutMillis;

    private final long beginTimestamp = System.currentTimeMillis();

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    public RemotingResponse(long opaque, InvokeCallback invokeCallback, long timeoutMillis) {
        this.opaque = opaque;
        this.invokeCallback = invokeCallback;
        this.timeoutMillis = timeoutMillis;
    }

    public void executeInvokeCallback(){
        if(invokeCallback != null){
            invokeCallback.operationComplete(this);
        }
    }

    public boolean isSendRequestOK() {
        return sendRequestOK;
    }

    public void setSendRequestOK(boolean sendRequestOK) {
        this.sendRequestOK = sendRequestOK;
    }

    public long getOpaque() {
        return opaque;
    }

    public RemotingTransporter getRemotingTransporter() {
        return remotingTransporter;
    }

    public void setRemotingTransporter(RemotingTransporter remotingTransporter) {
        this.remotingTransporter = remotingTransporter;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public long getBeginTimestamp() {
        return beginTimestamp;
    }


    /**
     * 当请求发送方发送请求之后，它就会调用 waitResponse这个方法，等待响应结果
     * @return
     * @throws InterruptedException
     */
    public RemotingTransporter waitResponse() throws InterruptedException {
        this.countDownLatch.wait(this.timeoutMillis,TimeUnit.MILLISECONDS.ordinal());
        return this.remotingTransporter;
    }

    /**
     * 当远程返回结果的时候，TCP的长连接上层载体channel 的handler会将其放入与requestId对应的Response中去
     * 当消息响应后就会调用putResponse
     * @param remotingTransporter
     */
    public void putResponse(final RemotingTransporter remotingTransporter) {
        this.remotingTransporter = remotingTransporter;
        this.countDownLatch.countDown();
    }



}
