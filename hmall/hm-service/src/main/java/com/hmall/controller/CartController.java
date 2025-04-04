package com.hmall.controller;


import com.hmall.domain.dto.CartFormDTO;
import com.hmall.domain.po.Cart;
import com.hmall.domain.vo.CartVO;
import com.hmall.service.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Schema(description = "购物车相关接口")
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public void addItem2Cart(@Valid @RequestBody CartFormDTO cartFormDTO){
        cartService.addItem2Cart(cartFormDTO);
    }

    @Operation(summary = "更新购物车数据")
    @PutMapping
    public void updateCart(@RequestBody Cart cart){
        cartService.updateById(cart);
    }

    @Operation(summary = "删除购物车中商品")
    @DeleteMapping("{id}")
    public void deleteCartItem(@Param ("购物车条目id")@PathVariable Long id){
        cartService.removeById(id);
    }

    @Operation(summary = "查询购物车列表")
    @GetMapping
    public List<CartVO> queryMyCarts(){
        return cartService.queryMyCarts();
    }
    @Operation(summary = "批量删除购物车中商品")
    @Parameter(name = "ids", description = "购物车条目id集合")
    @DeleteMapping
    public void deleteCartItemByIds(@RequestParam List<Long> ids){
        cartService.removeByItemIds(ids);
    }
}
