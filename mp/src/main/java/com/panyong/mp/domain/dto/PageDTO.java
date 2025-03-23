package com.panyong.mp.domain.dto;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panyong.mp.domain.po.User;
import com.panyong.mp.domain.vo.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Schema(description = "分页结果")
public class PageDTO<T> {
    
    @Schema(description = "总条数")
    private Long pageTotal;
    
    @Schema(description = "总页数")
    private Long pages;
    
    @Schema(description = "当前页结果")
    private List<T> list;
    
    /**
     * 在PageDTO中定义方法，将MyBatisPlus中的Page结果转为PageDTO结果
     * @param page
     * @return
     * @param <PO>
     * @param <VO>
     */
    public static <PO, VO> PageDTO<VO> of(Page<PO> page, Class<VO> voClass) {
        // 3. 封装VO
        PageDTO<VO> dto = new PageDTO<>();
        
        // 3.1 总条数
        dto.setPageTotal(page.getTotal());
        
        // 3.2 总页数
        dto.setPages(page.getPages());
        
        // 3.4 处理当前页结果集合
        List<PO> records = page.getRecords();
        
        // 泛型字节码 , 参数解决
        List<VO> vos = BeanUtil.copyToList(records, voClass);
        dto.setList(vos);
        
        // 4. 返回
        
        return dto;
    }
    
    public static <PO, VO> PageDTO<VO> of(Page<PO> page, Function<PO, VO> convertor) {
        // 3. 封装VO
        PageDTO<VO> dto = new PageDTO<>();
        
        // 3.1 总条数
        dto.setPageTotal(page.getTotal());
        
        // 3.2 总页数
        dto.setPages(page.getPages());
        
        // 3.4 处理当前页结果集合
        List<PO> records = page.getRecords();
        
        // 泛型字节码 , 参数解决
        List<VO> collect = records.stream().map(convertor).collect(Collectors.toList());
        dto.setList(collect);
        
        // 4. 返回
        
        return dto;
    }
}
