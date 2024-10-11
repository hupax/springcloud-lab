package com.panyong.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panyong.mp.domain.dto.PageDTO;
import com.panyong.mp.domain.po.User;
import com.panyong.mp.domain.query.UserQuery;
import com.panyong.mp.domain.vo.UserVO;

import java.util.List;

//@Service
public interface IUserService extends IService<User> {
    
    void deductUserBalanceById(Long id, Integer money);
    
    List<User> queryUsersByConditions(String name, Integer status, Integer minBalance, Integer maxBalance);
    
    void updateUserById(Long id, Integer money);
    
    UserVO queryUserAndAddressesById(Long id);
    
    List<UserVO> queryUsersAndAddressesByIds(List<Long> ids);
    
    PageDTO<UserVO> queryUsersPage(UserQuery query);
}
