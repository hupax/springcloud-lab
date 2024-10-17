package com.ml.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("user-service")
public interface UserClient {
    
    @PutMapping("/users/money/deduct")
    void deductMoney();
    
}
