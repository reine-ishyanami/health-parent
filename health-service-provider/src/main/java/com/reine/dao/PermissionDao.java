package com.reine.dao;

import com.reine.pojo.Permission;

import java.util.Set;

/**
 * @author reine
 * @since 2022/5/2 20:26
 */
public interface PermissionDao {
    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     */
    Set<Permission> findByRoleId(Integer roleId);
}
