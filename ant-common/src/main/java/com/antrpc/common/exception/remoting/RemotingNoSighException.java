package com.antrpc.common.exception.remoting;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-31
 * Time: 12:10
 */
public class RemotingNoSighException extends RemotingException {

    public RemotingNoSighException(String message) {
        super(message, null);
    }


    public RemotingNoSighException(String message, Throwable cause) {
        super(message, cause);
    }
}
