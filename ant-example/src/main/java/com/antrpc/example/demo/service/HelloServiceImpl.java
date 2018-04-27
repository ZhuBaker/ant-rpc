package com.antrpc.example.demo.service;

import com.antrpc.client.annotation.RPCService;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年04月24日
 * @modifytime:
 */
public class HelloServiceImpl implements HelloService {

    @Override
    @RPCService(responsibilityName="zhuBaker",
            serviceName="ANT.TEST.SAYHELLO",
            isVIPService = false,
            isSupportDegradeService = true,
            degradeServicePath="com.antrpc.example.demo.service.HelloServiceMock",
            degradeServiceDesc="默认返回hello")
    public String sayHello(String str) {
        //真实逻辑可能涉及到查库
        return "hello "+ str;
    }
}
