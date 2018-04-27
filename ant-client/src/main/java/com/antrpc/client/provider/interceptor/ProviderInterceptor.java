package com.antrpc.client.provider.interceptor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年04月24日
 * @modifytime:
 */
public interface ProviderInterceptor {

    void beforeInvoke(String methodName, Object[] args);

    void afterInvoke(String methodName, Object[] args, Object result);
}
