package com.antrpc.common.exception.remoting;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 15:28
 */
@SuppressWarnings("serial")
public class RemotingCommonCustomException extends RemotingException {

    public RemotingCommonCustomException(String message) {
        super(message,null);
    }

    public RemotingCommonCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
