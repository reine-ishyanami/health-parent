package com.reine.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.reine.dao.PermissionDao;
import com.reine.dao.RoleDao;
import com.reine.dao.UserDao;
import com.reine.pojo.Permission;
import com.reine.pojo.Role;
import com.reine.pojo.User;
import com.reine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户服务
 *
 * @author reine
 * @since 2022/5/2 20:18
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据用户名查询指定用户信息和角色信息以及角色关联的权限信息
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        // 查询用户基本信息，不包含用户角色
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        Integer userId = user.getId();
        // 根据用户id查询用户角色
        Set<Role> roles = roleDao.findByUserId(userId);
        roles.forEach(role -> {
            Integer roleId = role.getId();
            // 根据角色id查询关联的权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            // 角色关联权限
            role.setPermissions(permissions);
        });
        user.setRoles(roles);
        return user;
    }
}
