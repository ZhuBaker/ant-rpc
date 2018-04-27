package com.antrpc.client.provider;

import com.antrpc.client.provider.model.ServiceWrapper;
import com.antrpc.common.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: 编织后的服务容器
 * @time: 2018年04月24日
 * @modifytime:
 */
public class DefaultServiceProviderContainer implements ServiceProviderContainer{


    private final ConcurrentMap<String, Pair<CurrentServiceState, ServiceWrapper>> serviceProviders = new ConcurrentHashMap<String, Pair<CurrentServiceState, ServiceWrapper>>();

    /**
     * 将服务放置在服务容器中，用来进行统一的管理
     *
     * @param serviceName    该服务的名称
     * @param serviceWrapper 该服务的包装编织类
     */
    @Override
    public void registerService(String serviceName, ServiceWrapper serviceWrapper) {
        Pair<CurrentServiceState, ServiceWrapper> pair = new Pair<DefaultServiceProviderContainer.CurrentServiceState, ServiceWrapper>();
        pair.setKey(new CurrentServiceState());
        pair.setValue(serviceWrapper);
        serviceProviders.put(serviceName, pair);
    }

    /**
     * 根据服务的名称来获取对应的服务编织类
     *
     * @param serviceName 服务名
     * @return 服务编织类
     */
    @Override
    public Pair<CurrentServiceState, ServiceWrapper> lookupService(String serviceName) {
        return serviceProviders.get(serviceName);
    }

    /**
     * 获取到所有需要自动降级的服务
     *
     * @return
     */
    @Override
    public List<Pair<String, CurrentServiceState>> getNeedAutoDegradeService() {
        ConcurrentMap<String, Pair<CurrentServiceState, ServiceWrapper>> _serviceProviders = this.serviceProviders;
        List<Pair<String, CurrentServiceState>> list = new ArrayList<Pair<String,CurrentServiceState>>();

        for(String serviceName: _serviceProviders.keySet()){

            Pair<CurrentServiceState, ServiceWrapper> pair = _serviceProviders.get(serviceName);

            //如果已经设置成自动降级的时候
            if(pair != null && pair.getKey().getIsAutoDegrade().get()){
                Pair<String, CurrentServiceState> targetPair = new Pair<String,CurrentServiceState>();
                targetPair.setKey(serviceName);
                targetPair.setValue(pair.getKey());
                list.add(targetPair);
            }

        }
        return list;
    }

    public static class CurrentServiceState {
        private AtomicBoolean hasDegrade = new AtomicBoolean(false);    // 是否已经降级
        private AtomicBoolean hasLimitStream = new AtomicBoolean(true); // 是否已经限流
        private AtomicBoolean isAutoDegrade = new AtomicBoolean(false); // 是否已经开始自动降级
        private Integer minSuccecssRate = 90; 							// 服务最低的成功率，调用成功率低于多少开始自动降级

        public AtomicBoolean getHasDegrade() {
            return hasDegrade;
        }

        public void setHasDegrade(AtomicBoolean hasDegrade) {
            this.hasDegrade = hasDegrade;
        }

        public AtomicBoolean getHasLimitStream() {
            return hasLimitStream;
        }

        public void setHasLimitStream(AtomicBoolean hasLimitStream) {
            this.hasLimitStream = hasLimitStream;
        }

        public AtomicBoolean getIsAutoDegrade() {
            return isAutoDegrade;
        }

        public void setIsAutoDegrade(AtomicBoolean isAutoDegrade) {
            this.isAutoDegrade = isAutoDegrade;
        }

        public Integer getMinSuccecssRate() {
            return minSuccecssRate;
        }

        public void setMinSuccecssRate(Integer minSuccecssRate) {
            this.minSuccecssRate = minSuccecssRate;
        }

    }
}
