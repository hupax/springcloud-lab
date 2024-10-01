package com.panyong.mp.sercice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panyong.mp.domain.po.User;

//@Service
public interface IUserService extends IService<User> {
    
    void deductUserBalanceById(Long id, Integer money);
}
