package com.microservices.flash.bloop.client.services;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "censorship-failover")
public interface MessageFailoverFeignClient {
    
}
