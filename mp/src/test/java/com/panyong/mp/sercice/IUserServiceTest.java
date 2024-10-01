package com.panyong.mp.sercice;

import com.panyong.mp.domain.po.User;
import com.panyong.mp.sercice.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
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
        user.setUsername("uii");
        user.setPassword("123d");
        user.setPhone("18688990016");
        user.setBalance(2000);
        user.setInfo("{\"age\": 18, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        userService.save(user);
    }
    
    @Test
    void testQuery() {
        userService.listByIds(List.of(1L, 2L, 3L)).forEach(System.out::println);
    }
}