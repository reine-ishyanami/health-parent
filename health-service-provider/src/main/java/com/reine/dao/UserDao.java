package com.reine.dao;

import com.reine.pojo.User;

/**
 * @author reine
 * @since 2022/5/2 20:20
 */
public interface UserDao {
    /**
     * 根据用户名查询用户基本信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}
