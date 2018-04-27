package com.antrpc.client.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年04月24日
 * @modifytime:
 */
public class ProviderRPCController {

    private static final Logger logger = LoggerFactory.getLogger(ProviderRPCController.class);

    private DefaultProvider defaultProvider;

    public ProviderRPCController(DefaultProvider defaultProvider) {
        this.defaultProvider = defaultProvider;
    }



}
