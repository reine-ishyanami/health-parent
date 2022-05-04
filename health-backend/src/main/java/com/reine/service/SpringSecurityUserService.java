package com.reine.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.reine.pojo.Permission;
import com.reine.pojo.Role;
import com.reine.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author reine
 * @since 2022/5/2 20:06
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    /**
     * 使用dubbo通过网络远程调用服务提供方获取数据库中的用户信息
     */
    @Reference
    private UserService userService;

    /**
     * 根据用户名查询数据库获取用户信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }

        List<GrantedAuthority> list = new ArrayList<>();
        // 动态为当前用户授权
        Set<Role> roles = user.getRoles();
        roles.forEach(role -> {
            // 遍历角色集合，为用户授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            // 遍历权限集合，为用户授权
            permissions.forEach(permission -> list.add(new SimpleGrantedAuthority(permission.getKeyword())));
        });

        // 返回授权user对象
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return securityUser;
    }
}
