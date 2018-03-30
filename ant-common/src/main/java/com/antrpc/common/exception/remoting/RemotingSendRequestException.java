package com.antrpc.common.exception.remoting;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 16:32
 */
public class RemotingSendRequestException extends RemotingException {

    public RemotingSendRequestException(String addr) {
        this(addr,null);
    }

    public RemotingSendRequestException(String addr, Throwable cause) {
        super("send request to <"+ addr +"> failed", cause);
    }
}
