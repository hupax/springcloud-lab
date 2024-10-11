package com.panyong.mp.controller;


import cn.hutool.core.bean.BeanUtil;
import com.panyong.mp.domain.dto.PageDTO;
import com.panyong.mp.domain.dto.UserFormDTO;
import com.panyong.mp.domain.po.User;
import com.panyong.mp.domain.query.UserQuery;
import com.panyong.mp.domain.vo.UserVO;
import com.panyong.mp.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    /**
     * @Autowired spring不推荐注入, 可以使用lombok注解 @RequiredArgsConstructor + final 实现
     */
    private final IUserService userService;
    
    
    @Operation(description = "新增用户")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {
        // 1. 把DTO拷贝到PO
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        // 2. 新增
        userService.save(user);
    }
    
    @Operation(description = "删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUserById(@Parameter(description = "用户id") @PathVariable("id") Long id) {
        userService.removeById(id);
    }
    
    @Operation(description = "根据id查询用户接口")
    @GetMapping("{id}")
    public UserVO queryUserById(@Parameter(description = "用户id") @PathVariable("id") Long id) {
        User user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }
    
    /**
     * 改造根据id查询用户的接口，查询用户的同时，查询出用户对应的所有地址
     * @return
     */
    @Operation(description = "查询用户和地址接口")
    @GetMapping("{id}/address")
    public UserVO queryUserAndAddressesById(@Parameter(description = "用户id") @PathVariable("id") Long id) {
        return userService.queryUserAndAddressesById(id);
    }
    
    @Operation(description = "根据id批量查询用户接口")
    @GetMapping
    public List<UserVO> queryUserListByIds(@Parameter(description = "用户id集合") @RequestParam("ids") List<Long> ids) {
        List<User> users = userService.listByIds(ids);
//        return Collections.singletonList(BeanUtil.copyProperties(users, User.class));
        return BeanUtil.copyToList(users, UserVO.class);
    }
    
    /**
     * 改造根据id批量查询用户的接口，查询用户的同时，查询出用户对应的所有地址
     * @param ids
     * @return
     */
    @Operation(description = "批量查询用户以及地址接口")
    @GetMapping("ids/addresses")
    public List<UserVO> queryUsersAndAddressesByIds(@Parameter(description = "用户id集合") @RequestParam("ids") List<Long> ids) {
        return userService.queryUsersAndAddressesByIds(ids);
    }
    
    
    // 复杂接口
    @Operation(description = "根据id扣减用户余额")
    @PutMapping("{id}/deduction/{money}")
    public void deductUserBalanceById(@PathVariable("id") Long id, @PathVariable("money") Integer money) {
        userService.deductUserBalanceById(id, money);
    }
    
    /**
     * 完成对用户状态校验
     * 完成对用户余额校验
     * 如果扣减后余额为0，则将用户status修改为冻结状态（2）
     *
     * @param id
     * @param money
     */
    @Operation(description = "根据id修改用户信息复杂接口")
    @PutMapping
    public void updateUserById(@Parameter(description = "用户id") Long id, Integer money) {
        userService.updateUserById(id, money);
    }
    
    
    // Lambda和IService
    
    /**
     * 复杂条件查询
     * name：用户名关键字，可以为空
     * status：用户状态，可以为空
     * minBalance：最小余额，可以为空
     * maxBalance：最大余额，可以为空
     *
     * @param query
     */
    @Operation(description = "根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUsersByConditions(@Parameter(description = "复杂条件")
                                               @ParameterObject UserQuery query) {
        List<User> users = userService.queryUsersByConditions(query.getName(), query.getStatus(), query.getMinBalance(),
                query.getMaxBalance());
        return BeanUtil.copyToList(users, UserVO.class);
    }
    
    @Operation(description = "根据条件分页查询用户接口")
    @GetMapping("/page")
    public PageDTO<UserVO> queryUsersPage(@Parameter(description = "分页条件") @ParameterObject UserQuery query) {
         return userService.queryUsersPage(query);
    }

    

    
    
}









