package com.antrpc.remoting.netty.decode;

import com.antrpc.common.exception.remoting.RemotingContextException;
import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.remoting.model.RemotingTransporter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-31
 * Time: 11:18
 */
public class RemotingTransporterDecoder extends ReplayingDecoder<RemotingTransporterDecoder.State> {

    private static final Logger logger = LoggerFactory.getLogger(RemotingTransporterDecoder.class);

    private static final int MAX_BODY_SIZE = 1024 * 1024 * 5;

    private final AntProtocal  header = new AntProtocal();

    public RemotingTransporterDecoder() {
        super(State.HEADER_MAGIC);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case HEADER_MAGIC:
                checkMagic(in.readShort()); // MAGIC
                checkpoint(State.HEADER_TYPE);
            case HEADER_TYPE :
                header.setType(in.readByte());
                checkpoint(State.HEADER_SIGN);
            case HEADER_SIGN:
                header.setSign(in.readByte()); // 消息标志位
                checkpoint(State.HEADER_ID);
            case HEADER_ID:
                header.setId(in.readLong()); // 消息id
                checkpoint(State.HEADER_BODY_LENGTH);
            case HEADER_BODY_LENGTH:
                header.setBodyLength(in.readInt()); // 消息体长度
                checkpoint(State.HEADER_COMPRESS);
            case HEADER_COMPRESS:
                header.setCompress(in.readByte()); // 消息是否压缩
                checkpoint(State.BODY);
            case BODY:
                int bodyLength = checkBodyLength(header.getBodyLength());
                byte[] bytes = new byte[bodyLength];
                in.readBytes(bytes);
                if(header.getCompress() == AntProtocal.COMPRESS){
                    bytes = Snappy.uncompress(bytes);
                }
                out.add(RemotingTransporter.newInstance(header.getId(), header.getSign(),header.getType(), bytes));
                break;
            default:
                break;
        }
        checkpoint(State.HEADER_MAGIC);
    }

    private int checkBodyLength(int bodyLength) throws RemotingContextException {
        if (bodyLength > MAX_BODY_SIZE) {
            throw new RemotingContextException("body of request is bigger than limit value "+ MAX_BODY_SIZE);
        }
        return bodyLength;
    }

    private void checkMagic(short magic) throws RemotingContextException {
        if (AntProtocal.MAGIC != magic) {
            logger.error("Magic is not match");
            throw new RemotingContextException("magic value is not equal "+AntProtocal.MAGIC);
        }
    }

    enum State {
        HEADER_MAGIC, HEADER_TYPE, HEADER_SIGN, HEADER_ID, HEADER_BODY_LENGTH,HEADER_COMPRESS, BODY
    }
}
