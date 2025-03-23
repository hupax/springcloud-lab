package com.panyong.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.panyong.mp.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

//import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void updateBalanceByIds(@Param(/*"ew"*/Constants.WRAPPER) LambdaQueryWrapper<User> wrapper,
                            @Param("amount") Integer amount);
    
    void deductUserBalanceById(@Param("id") Long id, @Param("money") Integer money);
    

/*    void saveUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User queryUserById(@Param("id") Long id);

    List<User> queryUserByIds(@Param("ids") List<Long> ids);*/
}
