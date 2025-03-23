package com.ml.api.client;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("trade-service")
public interface TradeClient {
    
    @Parameter(name = "orderId")
    @PutMapping("/orders/{orderId}")
    void markOrderPaySuccess(@PathVariable Long orderId);
    
}
