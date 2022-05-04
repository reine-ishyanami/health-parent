package com.reine.dao;

import com.reine.pojo.Role;

import java.util.Set;

/**
 * @author reine
 * @since 2022/5/2 20:23
 */
public interface RoleDao {

    /**
     * 根据用户id查询用户角色
     * @param userId
     * @return
     */
    Set<Role> findByUserId(Integer userId);
}
