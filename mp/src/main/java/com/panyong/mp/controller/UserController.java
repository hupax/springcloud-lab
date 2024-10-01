package com.panyong.mp.controller;


import cn.hutool.core.bean.BeanUtil;
import com.panyong.mp.domain.dto.UserFormDTO;
import com.panyong.mp.domain.po.User;
import com.panyong.mp.domain.vo.UserVO;
import com.panyong.mp.sercice.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    /**
     * @Autowired    spring不推荐注入, 可以使用lombok注解 @RequiredArgsConstructor + final 实现
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
    
    @Operation(description = "根据id批量查询用户接口")
    @GetMapping
    public List<UserVO> queryUserListByIds(@Parameter(description = "用户id集合") @RequestParam("ids") List<Long> ids) {
        List<User> users = userService.listByIds(ids);
//        return Collections.singletonList(BeanUtil.copyProperties(users, UserVO.class));
        return BeanUtil.copyToList(users, UserVO.class);
    }
    
    
    // 复杂接口
    @Operation(description = "根据id扣减用户余额")
    @PutMapping("{id}/deduction/{money}")
    public void deductUserBalanceById(@PathVariable("id") Long id, @PathVariable("money") Integer money) {
        userService.deductUserBalanceById(id, money);
    }
    
}
