package com.antrpc.common.exception.remoting;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 16:35
 */
public class RemotingTimeoutException extends RemotingException{


    public RemotingTimeoutException(String message) {
        super(message);
    }


    public RemotingTimeoutException(String addr, long timeoutMillis) {
        this(addr, timeoutMillis, null);
    }


    public RemotingTimeoutException(String addr, long timeoutMillis, Throwable cause) {
        super("wait response on the channel <" + addr + "> timeout, " + timeoutMillis + "(ms)", cause);
    }

}
