package com.panyong.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panyong.mp.domain.po.User;
import com.panyong.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IUserServiceTest {
    
    @Autowired
    private IUserService userService;
    
    @Test
    void testSaveUser() {
        User user = new User();
//        user.setId(7L);
        user.setUsername("uidi");
        user.setPassword("123d");
        user.setPhone("18688990016");
        user.setBalance(2000);
//        user.setInfo("{\"age\": 18, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(18, "妹妹", "female"));
        
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        userService.save(user);
    }
    
    @Test
    void testQuery() {
        userService.listByIds(List.of(1L, 2L, 3L)).forEach(System.out::println);
    }
    
    
    private User buildUser(int i) {
        User user = new User();
        
        user.setUsername("user-" + i);
        user.setPassword("123");
        user.setPhone("" + 1888888888L + i);
        user.setBalance(2999);
//        user.setInfo("{\"age\": 18, \"intro\": \"妹妹\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(18, "妹妹", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        return user;
        
    }
    
    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        
        for (int i = 0; i < 100000; i++) {
            userService.save(buildUser(i));
        }
        
        long e = System.currentTimeMillis();
        
        System.out.println("耗时: " + (e - b));
        
        // 耗时: 18995
        
    }
    
    @Test
    void testSaveByBatch() {
        // 每次批量插入1000条数据
        
        List<User> list = new ArrayList<>(1000);
        
        long b = System.currentTimeMillis();
        
        for (int i = 0; i < 100000; i++) {
            list.add(buildUser(i));
            
            if (i % 1000 == 0) {
                userService.saveBatch(list);
                list.clear();
            }
        }
        
        
        long e = System.currentTimeMillis();
        
        System.out.println("耗时: " + (e - b));
        
        // 开启?rewriteBatchedStatements=true前
        // 耗时: 6868
        // 开启后
        // 耗时: 3661
        
    }
    
    /**
     * 分页查询
     */
    @Test
    void testPageQuery() {
        
        // 1. 准备条件
        // 1.1 分页条件
        int pageNum = 1, pageSize = 2;
        Page<User> page = Page.of(pageNum, pageSize);
        
        // 1.2 排序条件
        page.addOrder(OrderItem.descs("balance", "id"));
//        page.addOrder(OrderItem.desc("balance"));
        // 2. 分页查询
        // page实际与上page相同
        Page<User> p = userService.page(page);
        
        // 3. 解析
        long total = p.getTotal();
        System.out.println("total = " + total);
        // 页数
        long pages = p.getPages();
        System.out.println("pages = " + pages);
        // 当前页
        List<User> users = p.getRecords();
        users.forEach(System.out::println);
        
    }
}