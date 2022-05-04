package com.reine.controller;

import com.reine.constant.MessageConstant;
import com.reine.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关操作
 *
 * @author reine
 * @since 2022/5/3 7:41
 */
@RequestMapping("/user")
@RestController
public class UserController {

    /**
     * 获得当前登录用户的用户名
     *
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername() {
        // 当spring security完成认证后，会将用户信息保存到框架提供的上下文中
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
        String username = user.getUsername();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

}
