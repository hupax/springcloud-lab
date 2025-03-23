package com.ml.item.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.domain.PageDTO;
import com.hmall.common.domain.PageQuery;
import com.hmall.common.utils.BeanUtils;
import com.ml.item.domain.dot.ItemDTO;
import com.ml.item.domain.dot.OrderDetailDTO;
import com.ml.item.domain.po.Item;
import com.ml.item.service.IItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Schema(description = "商品管理相关接口")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    
    private final IItemService itemService;

    @Operation(summary = "分页查询商品")
    @GetMapping("/page")
    public PageDTO<ItemDTO> queryItemByPage(@ParameterObject PageQuery query) {
        // 1.分页查询
        Page<Item> result = itemService.page(query.toMpPage("update_time", false));
        // 2.封装并返回
        return PageDTO.of(result, ItemDTO.class);
    }

    @Operation(summary = "根据id批量查询商品")
    @GetMapping
    public List<ItemDTO> queryItemByIds(@RequestParam List<Long> ids){
        return itemService.queryItemByIds(ids);
    }

    @Operation(summary = "根据id查询商品")
    @GetMapping("{id}")
    public ItemDTO queryItemById(@PathVariable Long id) {
        return BeanUtils.copyBean(itemService.getById(id), ItemDTO.class);
    }

    @Operation(summary = "新增商品")
    @PostMapping
    public void saveItem(@RequestBody ItemDTO item) {
        // 新增
        itemService.save(BeanUtils.copyBean(item, Item.class));
    }

    @Operation(summary = "更新商品状态")
    @PutMapping("/status/{id}/{status}")
    public void updateItemStatus(@PathVariable Long id, @PathVariable Integer status){
        Item item = new Item();
        item.setId(id);
        item.setStatus(status);
        itemService.updateById(item);
    }

    @Operation(summary = "更新商品")
    @PutMapping
    public void updateItem(@RequestBody ItemDTO item) {
        // 不允许修改商品状态，所以强制设置为null，更新时，就会忽略该字段
        item.setStatus(null);
        // 更新
        itemService.updateById(BeanUtils.copyBean(item, Item.class));
    }

    @Operation(summary = "根据id删除商品")
    @DeleteMapping("{id}")
    public void deleteItemById(@PathVariable Long id) {
        itemService.removeById(id);
    }

    @Operation(summary = "批量扣减库存")
    @PutMapping("/stock/deduct")
    public void deductStock(@RequestBody List<OrderDetailDTO> items){
        itemService.deductStock(items);
    }
}
