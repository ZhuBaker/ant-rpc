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
public class ProviderMonitorController {

    private static final Logger logger = LoggerFactory.getLogger(ProviderMonitorController.class);

    private DefaultProvider defaultProvider;

    public ProviderMonitorController(DefaultProvider defaultProvider) {
        this.defaultProvider = defaultProvider;
    }


}
