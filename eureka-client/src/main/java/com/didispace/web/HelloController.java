package com.didispace.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping("/hello")
    public String index() {

        ServiceInstance serviceInstance = client.getLocalServiceInstance();
        logger.info("/hello, host:" + serviceInstance.getHost() + ", serviceId:" + serviceInstance.getServiceId());
        return "Hello World";
    }

}