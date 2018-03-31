package com.antrpc.example.netty;

import com.antrpc.common.exception.remoting.RemotingException;
import com.antrpc.remoting.model.RemotingTransporter;
import com.antrpc.remoting.netty.NettyClientConfig;
import com.antrpc.remoting.netty.NettyRemotingClient;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-31
 * Time: 12:58
 */
public class NettyClientTest {

    public static final byte TEST = -1;

    public static void main(String[] args) throws InterruptedException, RemotingException {
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        NettyRemotingClient client = new NettyRemotingClient(nettyClientConfig);
        client.start();

        TestCommonCustomBody.ComplexTestObj complexTestObj = new TestCommonCustomBody.ComplexTestObj("attr1", 2);
        TestCommonCustomBody commonCustomHeader = new TestCommonCustomBody(1, "test",complexTestObj);

        RemotingTransporter remotingTransporter = RemotingTransporter.createRequestTransporter(TEST, commonCustomHeader);
        RemotingTransporter request = client.invokeSync("127.0.0.1:18001", remotingTransporter, 3000);
        System.out.println(request);
    }

}
