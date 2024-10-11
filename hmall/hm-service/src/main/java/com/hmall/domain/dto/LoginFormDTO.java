package com.hmall.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "登录表单实体")
public class LoginFormDTO {
    @Schema(description = "用户名")
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    @Schema(description = "用户名")
    private String password;
    @Schema(description = "是否记住我")
    private Boolean rememberMe = false;
}
