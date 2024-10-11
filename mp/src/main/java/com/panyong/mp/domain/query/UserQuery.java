package com.panyong.mp.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户查询条件实体")
public class UserQuery extends PageQuery {
    @Schema(description = "用户名关键字")
    private String name;
    @Schema(description = "用户状态：1-正常，2-冻结")
    private Integer status;
    @Schema(description = "余额最小值")
    private Integer minBalance;
    @Schema(description = "余额最大值")
    private Integer maxBalance;
}
