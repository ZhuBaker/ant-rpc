package com.antrpc.remoting.netty.encode;

import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.common.serialization.SerializerHolder;
import com.antrpc.remoting.model.RemotingTransporter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description: Netty 对{@link RemotingTransporter}的编码器
 * User: zhubo
 * Date: 2018-03-31
 * Time: 10:44
 */
public class RemotingTransporterEncoder extends MessageToByteEncoder<RemotingTransporter>{

    @Override
    protected void encode(ChannelHandlerContext ctx, RemotingTransporter msg, ByteBuf out) throws Exception {
        doEncodeRemotingTransporter(msg,out);
    }

    private void doEncodeRemotingTransporter(RemotingTransporter msg , ByteBuf buf) throws IOException {
        byte[] body = SerializerHolder.serializerImpl().writeObject(msg.getCustomHead());
        byte isCompress = AntProtocal.UNCOMPRESS;
        buf.writeShort(AntProtocal.MAGIC)               // 协议头
                .writeByte(msg.getTransporterType())    // 传输类型 sign是请求还是响应
                .writeByte(msg.getCode())               // 请求类型 requestcode 表明主体信息的类型，也代表请求类型
                .writeLong(msg.getOpaque())             // requestId
                .writeInt(body.length)                  // length
                .writeByte(isCompress)                  // 是否压缩
                .writeBytes(body);

    }


}
