package com.antrpc.example.netty;

import com.antrpc.remoting.model.Heartbeats;
import io.netty.buffer.ByteBuf;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-04-01
 * Time: 11:33
 */
public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf buf = Heartbeats.heartbeatContent();
        ByteBuf buf2 = Heartbeats.heartbeatContent();
    }
}
