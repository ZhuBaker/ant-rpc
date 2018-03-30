package com.antrpc.remoting.model;

import com.antrpc.common.protocal.AntProtocal;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-30
 * Time: 9:19
 */
public class Heartbeats {

    private static final ByteBuf HEARTBEAT_BUF;

    static {
        ByteBuf buf = Unpooled.buffer(AntProtocal.HEAD_LENGTH);
        buf.writeShort(AntProtocal.MAGIC);//2
        buf.writeByte(AntProtocal.HEARTBEAT);//1
        buf.writeByte(0);//1
        buf.writeLong(0);//8
        buf.writeInt(0);//4
        HEARTBEAT_BUF = buf.asReadOnly();
    }


    /**
     * Returns the shared heartbeat content.
     * @return
     */
    public static ByteBuf heartbeatContent(){
        //复制当前对象，复制后的对象与前对象共享缓冲区，且维护自己的独立索引
        return HEARTBEAT_BUF.duplicate();
    }
}
