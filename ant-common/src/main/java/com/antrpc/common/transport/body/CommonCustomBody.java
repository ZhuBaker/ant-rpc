package com.antrpc.common.transport.body;

import com.antrpc.common.exception.remoting.RemotingCommonCustomException;

/**
 * Created with IntelliJ IDEA.
 * Description: 传输对象的主体对象
 * User: zhubo
 * Date: 2018-03-29
 * Time: 15:26
 */
public interface CommonCustomBody {

    void checkFields() throws RemotingCommonCustomException;
}
