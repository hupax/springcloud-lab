package com.hmall.controller;

import com.hmall.domain.dto.LoginFormDTO;
import com.hmall.domain.vo.UserLoginVO;
import com.hmall.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Schema(description = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @Operation(summary = "用户登录接口")
    @PostMapping("login")
    public UserLoginVO login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        return userService.login(loginFormDTO);
    }

    @Operation(summary = "扣减余额")
    @Parameters({
            @Parameter(name = "pw", description = "支付密码"),
            @Parameter(name = "amount", description = "支付金额")
    })
    @PutMapping("/money/deduct")
    public void deductMoney(@RequestParam String pw,@RequestParam Integer amount){
        userService.deductMoney(pw, amount);
    }
}

