package com.antrpc.common.exception.remoting;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 15:29
 */
@SuppressWarnings("serial")
public class RemotingException extends Exception implements Serializable{

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }
}
