package com.antrpc.example.netty;

import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.common.serialization.SerializerHolder;
import com.antrpc.remoting.model.NettyRequestProcessor;
import com.antrpc.remoting.model.RemotingTransporter;
import com.antrpc.remoting.netty.NettyServerConfig;
import com.antrpc.remoting.netty.idle.NettyRemotingServer;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-31
 * Time: 12:58
 */
public class NettyServerTest {

    public static final byte TEST = -1;

    public static void main(String[] args) {
        NettyServerConfig config = new NettyServerConfig();
        config.setListenPort(18001);
        NettyRemotingServer server = new NettyRemotingServer(config);
        server.registerProecessor(TEST, new NettyRequestProcessor() {

            @Override
            public RemotingTransporter processRequest(ChannelHandlerContext ctx, RemotingTransporter transporter) throws Exception {
                transporter.setCustomHead(SerializerHolder.serializerImpl().readObject(transporter.getBytes(), TestCommonCustomBody.class));
                System.out.println(transporter);
                transporter.setTransporterType(AntProtocal.RESPONSE_REMOTING);
                return transporter;
            }
        }, Executors.newCachedThreadPool());
        server.start();
    }

}
