package com.antrpc.remoting;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 20:46
 */
public class ConnectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);

    public static String parseChannelRemoteAddr(final Channel channel) {
        if(null == channel){
            return null;
        }
        SocketAddress socketAddress = channel.remoteAddress();
        final String addr = socketAddress != null ? socketAddress.toString() : "";

        if(addr.length() > 0){
            int index = addr.lastIndexOf("/");
            if(index >= 0){
                return addr.substring(index + 1);
            }
            return addr;
        }
        return "";
    }


    public static SocketAddress string2SocketAddress(String addr) {
        String[] s = addr.split(":");
        InetSocketAddress isa = new InetSocketAddress(s[0], Integer.valueOf(s[1]));
        return isa;
    }

    public static void closeChannel(Channel channel) {
        final String addrRemote = parseChannelRemoteAddr(channel);
        channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.info("closeChannel: close the connection to remote address[{}] result: {}", addrRemote,
                        future.isSuccess());
            }
        });
    }




}
