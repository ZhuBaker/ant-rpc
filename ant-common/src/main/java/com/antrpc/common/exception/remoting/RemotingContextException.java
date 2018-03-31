package com.antrpc.common.exception.remoting;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-31
 * Time: 11:34
 */
public class RemotingContextException extends RemotingException {


    public RemotingContextException(String message) {
        super(message, null);
    }


    public RemotingContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
