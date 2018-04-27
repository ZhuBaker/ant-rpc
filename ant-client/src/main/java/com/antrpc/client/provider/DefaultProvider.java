package com.antrpc.client.provider;

import com.antrpc.common.exception.remoting.RemotingException;
import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.common.transport.body.PublishServiceCustomBody;
import com.antrpc.common.util.NamedThreadFactory;
import com.antrpc.remoting.model.RemotingTransporter;
import com.antrpc.remoting.netty.NettyClientConfig;
import com.antrpc.remoting.netty.NettyRemotingClient;
import com.antrpc.remoting.netty.NettyRemotingServer;
import com.antrpc.remoting.netty.NettyServerConfig;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * @author: zhubo
 * @description: provider端的接口
 * @time: 2018-04-23
 * @modifytime:
 */
public class DefaultProvider implements Provider {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProvider.class);

    private NettyClientConfig clientConfig;               // 向注册中心连接的netty client配置
    private NettyServerConfig serverConfig; 			  // 等待服务提供者连接的netty server的配置
    private NettyRemotingClient nettyRemotingClient; 	  // 连接monitor和注册中心
    private NettyRemotingServer nettyRemotingServer;      // 等待被Consumer连接
    private NettyRemotingServer nettyRemotingVipServer;   // 等待被Consumer VIP连接
    private ProviderRegistryController providerController;// provider端向注册中心连接的业务逻辑的控制器
    private ProviderRPCController providerRPCController;  // consumer端远程调用的核心控制器
    private ExecutorService remotingExecutor;             // RPC调用的核心线程执行器
    private ExecutorService remotingVipExecutor; 		  // RPC调用VIP的核心线程执行器
    private Channel monitorChannel; 					  // 连接monitor端的channel
    /********* 要发布的服务的信息 ***********/
    private List<RemotingTransporter> publishRemotingTransporters;
    /************ 全局发布的信息 ************/
    private ConcurrentMap<String, PublishServiceCustomBody> globalPublishService = new ConcurrentHashMap<String, PublishServiceCustomBody>();
    /***** 注册中心的地址 ******/
    private String registryAddress;
    /******* 服务暴露给consumer的地址 ********/
    private int exposePort;
    /************* 监控中心的monitor的地址 *****************/
    private String monitorAddress;
    /*********** 要提供的服务 ***************/
    private Object[] obj;

    // 当前provider端状态是否健康，也就是说如果注册宕机后，该provider端的实例信息是失效，这是需要重新发送注册信息,因为默认状态下start就是发送，只有channel
    // inactive的时候说明短线了，需要重新发布信息
    private boolean ProviderStateIsHealthy = true;

    // 定时任务执行器
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("provider-timer"));


    public DefaultProvider() {
        this.clientConfig = new NettyClientConfig();
        this.serverConfig = new NettyServerConfig();
        providerController = new ProviderRegistryController(this);
        providerRPCController = new ProviderRPCController(this);
        initialize();
    }

    public DefaultProvider(NettyClientConfig clientConfig, NettyServerConfig serverConfig) {
        this.clientConfig = clientConfig;
        this.serverConfig = serverConfig;
        providerController = new ProviderRegistryController(this);
        providerRPCController = new ProviderRPCController(this);
        initialize();
    }


    private void initialize() {

        this.nettyRemotingServer = new NettyRemotingServer(this.serverConfig);
        this.nettyRemotingClient = new NettyRemotingClient(this.clientConfig);
        this.nettyRemotingVipServer = new NettyRemotingServer(this.serverConfig);

        this.remotingExecutor = Executors.newFixedThreadPool(serverConfig.getServerWorkerThreads(), new NamedThreadFactory("providerExecutorThread_"));
        this.remotingVipExecutor = Executors.newFixedThreadPool(serverConfig.getServerWorkerThreads() / 2, new NamedThreadFactory("providerExecutorThread_"));
        // 注册处理器
        this.registerProcessor();

        this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // 延迟5秒，每隔60秒开始 像其发送注册服务信息
                try {
                    logger.info("schedule check publish service");
                    if (!ProviderStateIsHealthy) {
                        logger.info("channel which connected to registry,has been inactived,need to republish service");
                        DefaultProvider.this.publishedAndStartProvider();
                    }
                } catch (Exception e) {
                    logger.warn("schedule publish failed [{}]", e.getMessage());
                }
            }
        }, 60, 60, TimeUnit.SECONDS);

    }



    private void registerProcessor() {
        // DefaultProviderRegistryProcessor defaultProviderRegistryProcessor = new DefaultProviderRegistryProcessor(this);
        // provider端作为client端去连接registry注册中心的处理器
        // this.nettyRemotingClient.registerProcessor(AntProtocal.DEGRADE_SERVICE, defaultProviderRegistryProcessor, null);
        // this.nettyRemotingClient.registerProcessor(AntProtocal.AUTO_DEGRADE_SERVICE, defaultProviderRegistryProcessor, null);
        // provider端连接registry链接inactive的时候要进行的操作(设置registry的状态为不健康，告之registry重新发送服务注册信息)
        // this.nettyRemotingClient.registerChannelInactiveProcessor(new DefaultProviderInactiveProcessor(this), null);
        // provider端作为netty的server端去等待调用者连接的处理器，此处理器只处理RPC请求
        this.nettyRemotingServer.registerDefaultProcessor(new DefaultProviderRPCProcessor(this), this.remotingExecutor);
        this.nettyRemotingVipServer.registerDefaultProcessor(new DefaultProviderRPCProcessor(this), this.remotingVipExecutor);
    }

    public List<RemotingTransporter> getPublishRemotingTransporters() {
        return publishRemotingTransporters;
    }

    /**
     * 启动provider的实例
     *
     * @throws RemotingException
     * @throws InterruptedException
     */
    @Override
    public void start() throws InterruptedException, RemotingException {

        logger.info("######### provider starting..... ########");
        // 编织服务
        this.publishRemotingTransporters = providerController.getLocalServerWrapperManager().wrapperRegisterInfo(this.getExposePort(), this.obj);

    }

    /**
     * 发布服务
     *
     * @throws InterruptedException
     * @throws RemotingException
     */
    @Override
    public void publishedAndStartProvider() throws InterruptedException, RemotingException {
        logger.info("publish service....");
        providerController.getRegistryController().publishedAndStartProvider();
        // 发布之后再次将服务状态改成true
        ProviderStateIsHealthy = true;
    }

    /**
     * 暴露服务的地址
     *
     * @param exposePort
     * @return
     */
    @Override
    public Provider serviceListenPort(int exposePort) {
        this.exposePort = exposePort;
        return this;
    }

    /**
     * 设置注册中心的地址  host:port,host1:port1
     *
     * @param registryAddress
     * @return
     */
    @Override
    public Provider registryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
        return this;
    }

    /**
     * 监控中心的地址，不是强依赖，不设置也没有关系
     *
     * @param monitorAddress
     * @return
     */
    @Override
    public Provider monitorAddress(String monitorAddress) {
        this.monitorAddress = monitorAddress;
        return this;
    }

    /**
     * 需要暴露的实例
     *
     * @param obj
     */
    @Override
    public Provider publishService(Object... obj) {
        this.obj = obj;
        return this;
    }

    /**
     * 处理消费者的rpc请求
     *
     * @param request
     * @param channel
     * @return
     */
    @Override
    public void handlerRPCRequest(RemotingTransporter request, Channel channel) {

    }

    public NettyRemotingClient getNettyRemotingClient() {
        return nettyRemotingClient;
    }

    public ProviderRegistryController getProviderController() {
        return providerController;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public int getExposePort() {
        return exposePort;
    }

    public void setExposePort(int exposePort) {
        this.exposePort = exposePort;
    }

    public ProviderRPCController getProviderRPCController() {
        return providerRPCController;
    }

    public boolean isProviderStateIsHealthy() {
        return ProviderStateIsHealthy;
    }

    public void setProviderStateIsHealthy(boolean providerStateIsHealthy) {
        ProviderStateIsHealthy = providerStateIsHealthy;
    }

    public Channel getMonitorChannel() {
        return monitorChannel;
    }

    public void setMonitorChannel(Channel monitorChannel) {
        this.monitorChannel = monitorChannel;
    }

    public String getMonitorAddress() {
        return monitorAddress;
    }

    public void setMonitorAddress(String monitorAddress) {
        this.monitorAddress = monitorAddress;
    }

    public ConcurrentMap<String, PublishServiceCustomBody> getGlobalPublishService() {
        return globalPublishService;
    }

    public void setGlobalPublishService(ConcurrentMap<String, PublishServiceCustomBody> globalPublishService) {
        this.globalPublishService = globalPublishService;
    }


}
