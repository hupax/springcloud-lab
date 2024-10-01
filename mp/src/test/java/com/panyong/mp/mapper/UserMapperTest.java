package com.panyong.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.panyong.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 查询出名字中带o的，存款大于等于1000元的人的id、username、info、balance字段
     */
    @Test
    void testQueryWrapper() {
        Wrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        userMapper.selectList(wrapper).forEach(System.out::println);
    }
    
    /**
     * 查询出名字中带o的，存款大于等于1000元的人的id、username、info、balance字段
     */
    @Test
    void testLambdaQueryWrapper() {
        Wrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        userMapper.selectList(wrapper).forEach(System.out::println);
    }
    
    /**
     * 更新用户名为jack的用户的余额为2000
     */
    @Test
    void testUpdateByQueryWrapper() {
        // 1. 更新的数据
        User user = new User();
        user.setBalance(2001);
        
        // 2. 更新的条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", "jack");
        
        // 3. 执行更新
        userMapper.update(user, wrapper);
        
    }
    
    /**
     * 需求：更新id为1,2,4的用户的余额，扣200
     */
    @Test
    void testUpdateWrapper() {
        
        List<Long> ids = List.of(1L, 2L, 3L, 4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance + 300")
                .in("id", ids);
        userMapper.update(wrapper);
    }
    
    /**
     * 使用lambda反射
     * 需求：更新id为1,2,4的用户的余额，扣200
     */
    @Test
    void testLambdaUpdateWrapper() {
        
        List<Long> ids = List.of(1L, 2L, 3L, 4L);
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .setSql("balance = balance + 300")
                .in(User::getId, ids);
        userMapper.update(wrapper);
    }
    
    /**
     * 自定义sql
     */
    @Test
    void testCustomSqlUpdate() {
        // 1.条件
        List<Long> ids = List.of(1L, 2L, 3L, 4L);
        // 2.定义条件
        Integer amount = 300;
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>().in(User::getId, ids);
        // 3.调用自定义的sql方法
        userMapper.updateBalanceByIds(wrapper, amount);
        
    }
    
    @Test
    void testInsert() {
        User user = new User();
//        user.setId(7L);
        user.setUsername("uog");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(2000);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }
    
    @Test
    void name() {
    }
    
    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }
    
    
    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }
    
    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }
    
    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }
}