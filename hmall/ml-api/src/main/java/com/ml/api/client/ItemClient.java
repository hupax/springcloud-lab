package com.ml.api.client;

import com.ml.api.dto.ItemDTO;
import com.ml.api.dto.OrderDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient("item-service")
public interface ItemClient {
    /**
     * 根据ids批量查询商品
     * @author
     * @date 2024-10-14
     * @param ids
     * @return List<ItemDTO>
     */
    @GetMapping("/items")
    List<ItemDTO> queryItemByTds(@RequestParam("ids") Collection<Long> ids);
    
    /**
     * 批量扣减库存
     * @author
     * @date 2024-10-14
     * @param items
     */
    @PutMapping("/items/stock/deduct")
    void deductStock(@RequestBody List<OrderDetailDTO> items);
}
