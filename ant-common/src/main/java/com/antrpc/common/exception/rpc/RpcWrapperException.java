package com.antrpc.common.exception.rpc;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年04月24日
 * @modifytime:
 */
public class RpcWrapperException extends RuntimeException {

    private static final long serialVersionUID = 5395455693773821359L;

    public RpcWrapperException() {}

    public RpcWrapperException(String message) {
        super(message);
    }

    public RpcWrapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcWrapperException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
