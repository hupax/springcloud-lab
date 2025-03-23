package com.ml.api.client;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserClient {
    
    @Parameters({
            @Parameter(name = "pw"),
            @Parameter(name = "amount")
    })
    @PutMapping("/users/money/deduct")
    void deductMoney(@RequestParam String pw, @RequestParam Integer amount);
    
}
