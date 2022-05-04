package com.reine.service;

import com.reine.pojo.User;

/**
 * @author reine
 * @since 2022/5/2 20:08
 */
public interface UserService {

    /**
     * 根据用户名查询指定用户信息和角色信息以及角色关联的权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}
