package com.antrpc.remoting.model;

import com.antrpc.common.protocal.AntProtocal;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created with IntelliJ IDEA.
 * Description: 自定义心跳协议
 * User: zhubo
 * Date: 2018-03-30
 * Time: 9:19
 */
public class Heartbeats {

    private static final ByteBuf HEARTBEAT_BUF;

    static {
        ByteBuf buf = Unpooled.buffer(AntProtocal.HEAD_LENGTH);
        buf.writeShort(AntProtocal.MAGIC);//2  magic
        buf.writeByte(AntProtocal.HEARTBEAT);//1 code
        buf.writeByte(0);//1 type
        buf.writeLong(0);//8 id
        buf.writeInt(0);//4 length
        buf.writeByte(0);//1 compress
        HEARTBEAT_BUF = buf.asReadOnly();
    }
    /**
     * 返回公用的心跳内容.
     * @return
     */
    public static ByteBuf heartbeatContent(){

        // 我们可以调用ByteBuf.retain()将引用计数加1
        HEARTBEAT_BUF.retain();
        //复制当前对象，复制后的对象与前对象共享缓冲区，且维护自己的独立索引
        return HEARTBEAT_BUF.duplicate();
    }
}
