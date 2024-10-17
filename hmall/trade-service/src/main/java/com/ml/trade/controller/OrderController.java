package com.ml.trade.controller;

import com.hmall.common.utils.BeanUtils;
import com.ml.trade.domain.dto.OrderFormDTO;
import com.ml.trade.domain.vo.OrderVO;
import com.ml.trade.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Schema(description = "订单管理接口")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @Operation(summary = "根据id查询订单")
    @GetMapping("{id}")
    public OrderVO queryOrderById(@Param ("订单id")@PathVariable("id") Long orderId) {
        return BeanUtils.copyBean(orderService.getById(orderId), OrderVO.class);
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public Long createOrder(@RequestBody OrderFormDTO orderFormDTO){
        return orderService.createOrder(orderFormDTO);
    }

    @Operation(summary = "标记订单已支付")
    @Parameter(name = "orderId", description = "订单id")
    @PutMapping("/{orderId}")
    public void markOrderPaySuccess(@PathVariable Long orderId) {
        orderService.markOrderPaySuccess(orderId);
    }
}
