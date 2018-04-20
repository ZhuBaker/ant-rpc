package com.antrpc.remoting.model;

import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.common.transport.body.CommonCustomBody;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-29
 * Time: 15:20
 */
public class RemotingTransporter extends ByteHolder {

    private static final AtomicLong requestId = new AtomicLong(0L);

    /**
     * 请求的类型
     * 例如该请求时用来订阅服务的，该请求是用来发布服务的等等
     * 假设 code == 1 代表是消费者订阅服务，则接收方注册中心接收到该对象的时候就会先获取该code,判断如果该code == 1 则走订阅服务的处理分支代码
     * 假设 code == 2 代表是提供者发布服务，则接收方注册中心接收到该对象的时候也会先获取该code，判断如果该code == 2 则走发布服务的处理分支代码
     */
    private byte code;

    /**
     * 请求的主体信息 是一个接口
     * 假如code == 1 则 CommonCustomBody中则是一些订阅服务的具体信息
     * 假如code == 2 则 CommonCustomBody中则是一些发布服务的具体信息
     */
    private transient CommonCustomBody customHead;

    /**
     * 请求的时间戳
     */
    private transient long timestamp;

    /**
     * 请求ID
     */
    private long opaque = requestId.getAndIncrement();

    /**
     * 定义该传输对象是请求信息还是响应信息
     */
    private byte transporterType ;

    public RemotingTransporter() {
    }

    /**
     * 创建一个请求传输对象
     * @param code 请求的类型
     * @param commonCustomHeader 请求的正文
     * @return
     */
    public static RemotingTransporter createRequestTransporter(byte code , CommonCustomBody commonCustomHeader){
        RemotingTransporter remotingTransporter = new RemotingTransporter();
        remotingTransporter.setCode(code);
        remotingTransporter.setCustomHead(commonCustomHeader);
        remotingTransporter.transporterType = AntProtocal.REQUEST_REMOTING;
        return remotingTransporter;
    }

    /**
     * 创建一个响应对象
     * @param code 响应对象的类型
     * @param commonCustomHeader 响应对象的正文
     * @param opaque 此响应对象对应的请求对象的id
     * @return
     */
    public static RemotingTransporter createResponseTransporter(byte code,CommonCustomBody commonCustomHeader , long opaque){
        RemotingTransporter remotingTransporter = new RemotingTransporter();
        remotingTransporter.setCode(code);
        remotingTransporter.customHead = commonCustomHeader;
        remotingTransporter.setOpaque(opaque);
        remotingTransporter.transporterType = AntProtocal.RESPONSE_REMOTING;
        return remotingTransporter;
    }


    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public CommonCustomBody getCustomHead() {
        return customHead;
    }

    public void setCustomHead(CommonCustomBody customHead) {
        this.customHead = customHead;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getOpaque() {
        return opaque;
    }

    public void setOpaque(long opaque) {
        this.opaque = opaque;
    }

    public byte getTransporterType() {
        return transporterType;
    }

    public void setTransporterType(byte transporterType) {
        this.transporterType = transporterType;
    }

    /**
     *
     * @param id 请求消息的唯一标识
     * @param code
     * @param type 设置该消息是 请求信息还是响应信息
     * @param bytes
     * @return
     */
    public static RemotingTransporter newInstance(long id , byte code , byte type , byte[] bytes){
        RemotingTransporter remotingTransporter = new RemotingTransporter();
        remotingTransporter.setCode(code);
        remotingTransporter.setTransporterType(type);
        remotingTransporter.setOpaque(id);
        remotingTransporter.setBytes(bytes);
        return remotingTransporter;

    }


    @Override
    public String toString() {
        return "RemotingTransporter [code=" + code + ", customHeader=" + customHead + ", timestamp=" + timestamp + ", opaque=" + opaque
                + ", transporterType=" + transporterType + "]";
    }



}
