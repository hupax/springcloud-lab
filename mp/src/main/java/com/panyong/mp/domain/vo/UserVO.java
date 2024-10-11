package com.panyong.mp.domain.vo;

import com.panyong.mp.domain.po.UserInfo;
import com.panyong.mp.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户VO实体")
public class UserVO {
    
    @Schema(description = "用户id")
    private Long id;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "详细信息")
    private UserInfo info;
    
    @Schema(description = "使用状态（1正常 2冻结）")
    private UserStatus status;
    
    @Schema(description = "账户余额")
    private Integer balance;
    
    @Schema(description = "用户的收货地址s")
    private List<AddressVO> addresses;
    
}
