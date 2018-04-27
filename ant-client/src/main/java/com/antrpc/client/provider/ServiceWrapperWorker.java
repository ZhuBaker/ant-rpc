package com.antrpc.client.provider;

import com.antrpc.client.provider.interceptor.ProviderProxyHandler;
import com.antrpc.client.provider.model.ServiceWrapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年04月24日
 * @modifytime:
 */
public interface ServiceWrapperWorker {

    ServiceWrapperWorker provider(Object serviceProvider);

    ServiceWrapperWorker provider(ProviderProxyHandler proxyHandler, Object serviceProvider);

    List<ServiceWrapper> create();

}
