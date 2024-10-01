package com.panyong.mp.sercice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panyong.mp.domain.po.User;
import com.panyong.mp.mapper.UserMapper;
import com.panyong.mp.sercice.IUserService;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    
    @Override
    public void deductUserBalanceById(Long id, Integer money) {
        
        // 1. 查询用户
        // 调用自己的
        User user = getById(id);
        
        // 2. 检查用户状态
        // 使用状态（1正常 2冻结）
        if (user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常");
        }
        
        // 3. 校验余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }
        
        // 4. 扣减 update uer set balance = balance - ? where id = ?
        // 使用mp自定义
        baseMapper.deductUserBalanceById(id, money);
        
    }
}
