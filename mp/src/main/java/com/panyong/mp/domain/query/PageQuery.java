package com.panyong.mp.domain.query;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panyong.mp.domain.po.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Schema(description = "分页查询实体")
public class PageQuery {
    
    @Schema(description = "页码")
    private Integer pageNum = 1;
    
    @Schema(description = "页码条数")
    private Integer pageSize = 5;
    
    @Schema(description = "排序字段")
    private String sortBy;
    
    @Schema(description = "是否升序")
    private Boolean isAsc = true;
    
    
    /**
     * 在PageQuery中定义方法，将PageQuery对象转为MyBatisPlus中的Page对象
     * @param items
     * @return
     * @param <T>
     */
    public <T> Page<T> toMpPage(OrderItem ... items) {
        
        // 1. 构建分页条件
        Page<T> page = Page.of(pageNum, pageSize);
        
        if (StrUtil.isNotBlank(sortBy)) {
            page.addOrder(isAsc ? OrderItem.asc(sortBy) : OrderItem.desc(sortBy));
        } else if (items != null){
            page.addOrder(items);
        }
        
        return page;
    }
    
    public <T> Page<T> toMpPage(String defaultSortBy, Boolean defaultAsc) {
        return toMpPage(defaultAsc ? OrderItem.asc(defaultSortBy) : OrderItem.desc(defaultSortBy));
    }
    
    public <T> Page<T> toMpPageDefaultSortByCreateTime() {
        return toMpPage(OrderItem.desc("create_time"));
    }
    
    public <T> Page<T> toMpPageDefaultSortByUpdateTime() {
        return toMpPage(OrderItem.desc("update_time"));
    }
}
