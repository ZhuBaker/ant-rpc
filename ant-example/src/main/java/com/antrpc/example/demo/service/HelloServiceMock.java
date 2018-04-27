package com.antrpc.example.demo.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年04月24日
 * @modifytime:
 */
public class HelloServiceMock implements HelloService {

    @Override
    public String sayHello(String str) {
        //直接给出默认的返回值
        return "hello";
    }
}
