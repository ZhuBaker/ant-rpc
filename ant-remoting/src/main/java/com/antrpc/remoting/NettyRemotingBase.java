package com.antrpc.remoting;

import com.antrpc.common.exception.remoting.RemotingSendRequestException;
import com.antrpc.common.exception.remoting.RemotingTimeoutException;
import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.common.util.Pair;
import com.antrpc.remoting.model.NettyChannelInactiveProcessor;
import com.antrpc.remoting.model.NettyRequestProcessor;
import com.antrpc.remoting.model.RemotingResponse;
import com.antrpc.remoting.model.RemotingTransporter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * Description: netty C/S 端的客户端提取，子类去完全netty的一些创建的事情，该抽象类则取完成使用子类创建好的channel去与远程端交互
 * User: zhubo
 * Date: 2018-03-29
 * Time: 17:18
 */
public abstract class NettyRemotingBase {

    private static final Logger logger = LoggerFactory.getLogger(NettyRemotingBase.class);

    /******key为请求的opaque value是远程返回的结果封装类******/
    protected final ConcurrentHashMap<Long, RemotingResponse> responseTable = new ConcurrentHashMap<Long, RemotingResponse>(256);


    //如果使用者没有对创建的Netty网络段注入某个特定请求的处理器的时候，默认使用该默认的处理器
    protected Pair<NettyRequestProcessor, ExecutorService> defaultRequestProcessor;

    //netty网络段channelInactive事件发生的处理器
    protected Pair<NettyChannelInactiveProcessor, ExecutorService> defaultChannelInactiveProcessor;


    protected final ExecutorService publicExecutor = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
        private AtomicInteger threadIndex = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"NettyClientPublicExecutor_" + this.threadIndex.incrementAndGet());
        }
    });
    //注入的某个requestCode对应的处理器放入到HashMap中，键值对一一匹配
    protected final HashMap<Byte/* request code */, Pair<NettyRequestProcessor, ExecutorService>> processorTable =
            new HashMap<Byte, Pair<NettyRequestProcessor, ExecutorService>>(64);


    //远程端的调用具体实现（通用）
    public RemotingTransporter invokeSyncImpl(final Channel channel,final RemotingTransporter request , final long timeoutMillis)
            throws RemotingTimeoutException , RemotingSendRequestException, InterruptedException {
        try{
            //构造一个请求结果的封装体，请求Id和请求结果一一对应
            final RemotingResponse remotingResponse = new RemotingResponse(request.getOpaque(),null,timeoutMillis);
            //将请求结果放入一个"篮子"中，等远程端填充该篮子中嗷嗷待哺的每一个结果集
            this.responseTable.put(request.getOpaque(),remotingResponse);

            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        //如果发送对象成功，则设置成功
                        remotingResponse.setSendRequestOK(true);
                        return ;
                    }else{
                        remotingResponse.setSendRequestOK(false);
                    }
                    // 如果请求发送失败，则默认从responseTable这个篮子中移除
                    responseTable.remove(request.getOpaque());
                    // 失败的异常信息
                    remotingResponse.setCause(future.cause());
                    // 设置当前请求的返回体返回体是null （失败的情况下返回的结果肯定是null）
                    remotingResponse.putResponse(null);
                    logger.warn("use channel [{}] send msg [{}] failed and failed reason is [{}]",channel,request,future.cause().getMessage());
                }
            });

            RemotingTransporter remotingTransporter = remotingResponse.waitResponse();
            if(null == remotingTransporter) {
                // 如果发是成功的，则说明远程端处理超时 (因为处理失败了就会将其设置为false)
                if(remotingResponse.isSendRequestOK()){
                    throw new RemotingTimeoutException(ConnectionUtils.parseChannelRemoteAddr(channel)
                            ,timeoutMillis,remotingResponse.getCause());
                } else {
                    throw new RemotingSendRequestException(ConnectionUtils.parseChannelRemoteAddr(channel)
                            ,remotingResponse.getCause());
                }
            }
            return remotingTransporter;

        }finally {
            this.responseTable.remove(request.getOpaque());
        }

    }

    protected void processMessageReceived(ChannelHandlerContext ctx , RemotingTransporter msg) {
        if(logger.isDebugEnabled()){
            logger.debug("channel [] received RemotingTransporter is [{}]",ctx.channel(),msg);
        }
        final RemotingTransporter remotingTransporter = msg ;

        if(remotingTransporter != null){
            switch (remotingTransporter.getTransporterType()) {
                case AntProtocal.REQUEST_REMOTING:
                    processRemotingRequest(ctx,msg);
                    break;
                case AntProtocal.RESPONSE_REMOTING:
                    processRemotingResponse(ctx,msg);
                    break;
                default:
                    break;
            }
        }

    }

    protected void processChannelInactive(final ChannelHandlerContext ctx){
        final Pair<NettyChannelInactiveProcessor , ExecutorService > pair = this.defaultChannelInactiveProcessor;
        if(pair != null){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try{
                        pair.getKey().processChannelInactive(ctx);
                    }catch (RemotingSendRequestException | RemotingTimeoutException | InterruptedException e){
                        logger.error("server occor exception [{}]",e.getMessage());
                    }
                }
            };
            try{
                pair.getValue().submit(runnable);
            }catch (Exception e){
                logger.error("server is busy,[{}]",e.getMessage());
            }
        }
    }

    /**
     * 对于远程的请求消息做处理操作(发送请求)  这个方法未在responseTable中加入数据
     * @param ctx
     * @param remotingTransporter
     */
    protected void processRemotingRequest(final ChannelHandlerContext ctx , final RemotingTransporter remotingTransporter) {

        //从处理器中获取指定请求的处理器
        final Pair<NettyRequestProcessor,ExecutorService> matchedPair = this.processorTable.get(remotingTransporter.getCode());
        final Pair<NettyRequestProcessor,ExecutorService> pair =
                null == matchedPair ? this.defaultRequestProcessor : matchedPair;

        if(pair != null){
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try{
                        //TODO 调用指定对象
                        RPCHook rpcHook = NettyRemotingBase.this.getRPCHook();
                        if(rpcHook != null){
                            rpcHook.doBeforeRequest(ConnectionUtils.parseChannelRemoteAddr(ctx.channel()),remotingTransporter);
                        }
                        final RemotingTransporter response = pair.getKey().processRequest(ctx,remotingTransporter);

                        if(rpcHook != null) {
                            rpcHook.doAfterResponse(ConnectionUtils.parseChannelRemoteAddr(ctx.channel()),remotingTransporter,response);
                        }

                        if(null != response) {
                            ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    if(!future.isSuccess()){
                                        logger.error("fail send response ,exception is [{}]",future.cause().getMessage());
                                    }
                                }
                            });
                        }
                    }catch (Exception e){
                        logger.error("processor occur exception [{}]",e.getMessage());
                        final RemotingTransporter response = RemotingTransporter.newInstance(remotingTransporter.getOpaque(), AntProtocal.RESPONSE_REMOTING, AntProtocal.HANDLER_ERROR, null);
                        ctx.writeAndFlush(response);
                    }
                }
            };
            try{
                pair.getValue().submit(run);
            }catch (Exception e){
                logger.error("server is busy,[{}]",e.getMessage());
                //TODO ...
                final RemotingTransporter response = RemotingTransporter
                        .newInstance(remotingTransporter.getOpaque(), AntProtocal.RESPONSE_REMOTING, AntProtocal.HANDLER_BUSY, null);
                ctx.writeAndFlush(response);
            }
        }
    }

    protected abstract RPCHook getRPCHook();


    /**
     * 对于请求响应 回来的信息做处理操作 (对接收多来的数据做数据处理)
     * @param ctx
     * @param remotingTransporter
     */
    protected void processRemotingResponse(ChannelHandlerContext ctx, RemotingTransporter remotingTransporter) {
        //从篮子中拿出对应请求的对应响应载体
        final RemotingResponse remotingResponse = responseTable.get(remotingTransporter.getOpaque());
        // 不超时的情况下
        if(null != remotingResponse) {
            //先设置值 ,在 countdown之前把值设置上
            remotingResponse.setRemotingTransporter(remotingTransporter);
            //可以直接countdown
            remotingResponse.putResponse(remotingTransporter);
            // 从篮子中移除
            responseTable.remove(remotingTransporter.getOpaque());
        }else{
            logger.warn("received response but matched Id is removed from responseTable maybe timeout");
            logger.warn(remotingTransporter.toString());
        }
    }


}
