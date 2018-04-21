package com.antrpc.example.netty;

import com.antrpc.remoting.model.Heartbeats;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @Author: BakerZhu
 * @Date: 2018/4/21 18:15
 * @Description:
 */
public class HeartbeatsTest {

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(1);
        ByteBuf byteBuf1 = byteBuf.asReadOnly();// 返回一个不可写的bytebuf
        System.out.println(byteBuf1.readerIndex());
        System.out.println(byteBuf1.writerIndex());
        ByteBuf duplicate = byteBuf1.duplicate();
        System.out.println(duplicate.readerIndex());
        System.out.println(duplicate.writerIndex());

        System.out.println(duplicate.readByte());

        System.out.println(duplicate.readerIndex());
        System.out.println(duplicate.writerIndex());

        System.out.println("=================");
        ByteBuf duplicate2 = byteBuf1.duplicate();
        System.out.println(duplicate2.readByte());

    }
}
