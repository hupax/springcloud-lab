package com.panyong.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.panyong.mp.domain.dto.PageDTO;
import com.panyong.mp.domain.po.Address;
import com.panyong.mp.domain.po.User;
import com.panyong.mp.domain.query.UserQuery;
import com.panyong.mp.domain.vo.AddressVO;
import com.panyong.mp.domain.vo.UserVO;
import com.panyong.mp.enums.UserStatus;
import com.panyong.mp.mapper.UserMapper;
import com.panyong.mp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    
    @Override
    public void deductUserBalanceById(Long id, Integer money) {
        
        // 1. 查询用户
        // 调用自己的
        User user = getById(id);
        
        // 2. 检查用户状态
        // 使用状态（1正常 2冻结）
//        log.info("{}", user.toString());
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户状态异常");
        }
        
        // 3. 校验余额是否充足~
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }
        
        // 4. 扣减 update uer set balance = balance - ? where id = ?
        // 使用mp自定义
        baseMapper.deductUserBalanceById(id, money);
        
    }
    
    @Override
    public List<User> queryUsersByConditions(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .gt(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }
    
    @Override
    @Transactional  // 事务
    public void updateUserById(Long id, Integer money) {
        // 1. 查询用户
        // 调用自己的
        User user = getById(id);
        
        // 2. 检查用户状态
        // 使用状态（1正常 2冻结）
//        log.info("{}", user.toString());
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户状态异常");
        }
        
        // 3. 校验余额是否充足~
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }
        
        lambdaUpdate()
                .set(User::getBalance, user.getBalance() - money)
                .set(user.getBalance() - money == 0, User::getStatus, UserStatus.FROZEN)
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance())  // 乐观🔐
                .update();
    }
    
    @Override
    public UserVO queryUserAndAddressesById(Long id) {
        // 1. 根据id查询用户
        User user = getById(id);
        
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户不存在或状态异常");
        }
        
        // 2. 查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class).eq(Address::getUserId, id).list();
        
        // 3. 封装VO, 封装地址VO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        
        if (CollUtil.isNotEmpty(addresses)) {
            userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        }
        
        return userVO;
    }
    
    @Override
    public List<UserVO> queryUsersAndAddressesByIds(List<Long> ids) {
        // 1. 根据id查询用户
        List<User> users = listByIds(ids);
        
        // 1.1 使用hutool判断users
        if (CollUtil.isEmpty(users)) {
            // 返回空集合
            return Collections.emptyList();
        }
        
        // 2. 查询地址集合使用stream流
        // 2.1 获取用户id集合
        List<Long> userIds = users.stream().map(User::getId).toList();
        // 2.2 根据id查询地址集合
        List<Address> addresses = Db.lambdaQuery(Address.class).in(Address::getUserId, userIds).list();
        
        // 2.3地址转VO
        List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
        
        // 2.4 将地址集合分类
        Map<Long, List<AddressVO>> addressesMap = new HashMap<>(0);
        
        if (CollUtil.isNotEmpty(addresses)) {
            
            addressesMap =
                    addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
            
        }
        
        // 3. 封装, User转VO
        List<UserVO> list = new ArrayList<>(users.size());
        for (User user : users) {
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            list.add(vo);
            vo.setAddresses(addressesMap.get(user.getId()));
            
        }
        
        return list;
    }
    
    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery query) {
        
/*        // 1. 构建分页条件
        Page<User> page = Page.of(query.getPageNum(), query.getPageSize());

        // 1.1 排序条件
        *//*if (query.getIsAsc()) {
            page.addOrder(OrderItem.asc(query.getSortBy()));
        } else {
            page.addOrder(OrderItem.desc(query.getSortBy()));
        }*//*
        String sortBy = query.getSortBy() == null ? "update_time" : query.getSortBy();
        page.addOrder(query.getIsAsc() ?
                OrderItem.asc(sortBy) : OrderItem.desc(sortBy));*/

        // 2. 分页查询
        
        Page<User> page = query.toMpPageDefaultSortByCreateTime();
        
        Page<User> userPage = lambdaQuery()
                .like(query.getName() != null, User::getUsername, query.getName())
                .eq(query.getStatus() != null, User::getStatus, query.getStatus())
                .page(page);
        
        
/*        // 3. 封装VO
        PageDTO<UserVO> dto = new PageDTO<>();
        
        // 3.1 总条数
        dto.setPageTotal(userPage.getTotal());
        
        // 3.2 总页数
        dto.setPages(userPage.getPages());
        
        // 3.4 处理当前页结果集合
        List<User> users = userPage.getRecords();
        
        if (CollUtil.isEmpty(users)) {
             dto.setList(Collections.emptyList());
            return dto;
        }
        List<UserVO> userVOS = BeanUtil.copyToList(users, UserVO.class);
        dto.setList(userVOS);
        
        // 4. 返回
        
        return dto;*/
        
//        return PageDTO.of(userPage, UserVO.class);
//        return PageDTO.of(userPage, user -> BeanUtil.copyProperties(user, UserVO.class));
        return PageDTO.of(userPage, user -> {
            // 1. 转VO
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            // 2. 其他处理
            userVO.setUsername(userVO.getUsername().substring(0,
                    userVO.getUsername().length() - 2) + "**");
            
//            userVO.setAddresses(userVO.getAddresses().stream().map(uvo -> {
//                uvo.setStreet("*" + uvo.getStreet().substring(1, uvo.getStreet().length() - 1));
//                return uvo;
//            }).collect(Collectors.toList()));
            
            return userVO;
        });
        
    }
    
    
}
