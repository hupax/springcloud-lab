package com.panyong.mp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IAddressServiceTest {
    
    @Autowired
    private IAddressService addressService;
    
    /**
     * 逻辑删除测试, 测试是否sql语句是否会加上 where deleted = ?
     */
    @Test
    void testLogicDelete() {
        addressService.removeById(59L);
        
        // 结果: ==>  Preparing: UPDATE address SET deleted=1 WHERE id=? AND deleted=0
    }
    
    /**
     * 逻辑查询测试
     */
    @Test
    void testLogicQuery() {
        addressService.getById(59L);
    }
}