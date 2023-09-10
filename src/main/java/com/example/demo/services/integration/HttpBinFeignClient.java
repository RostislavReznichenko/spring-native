package com.example.demo.services.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "httpBin", url = "https://httpbin.org")
public interface HttpBinFeignClient {

    @GetMapping("/get")
    Object get();

}
