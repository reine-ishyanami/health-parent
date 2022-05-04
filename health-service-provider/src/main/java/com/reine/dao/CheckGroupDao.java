package com.reine.dao;

import com.github.pagehelper.Page;
import com.reine.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @author reine
 * @since 2022/4/21 20:17
 */
public interface CheckGroupDao {
    /**
     * 添加检查组
     * @param checkGroup 检查组
     */
    void add(CheckGroup checkGroup);

    /**
     * 添加检查组检查项关系
     * @param map 检查组id和检查项的id
     */
    void setCheckGroupAndCheckIItem(Map map);

    /**
     * 根据查询条件查询检查组
     * @param queryString 查询条件
     * @return 查询结果
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 根据id查询检查组
     * @param id 检查组id
     * @return 检查组对象
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询对应关联的检查项id
     * @param id 检查组id
     * @return 检查项id列表
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 编辑检查组
     * @param checkGroup 检查组信息
     */
    void edit(CheckGroup checkGroup);

    /**
     * 删除检查组对应的检查项
     * @param id 检查组id
     */
    void deleteAssociation(Integer id);

    /**
     * 查询所有检查组
     * @return 所有检查组
     */
    List<CheckGroup> findAll();

    /**
     * 通过检查组是否被包含于套餐
     * @param id 检查组id
     * @return 关联的套餐数量
     */
    long findCountByCheckGroupId(Integer id);

    /**
     * 根据检查组id删除检查组
     * @param id 检查组id
     */
    void deleteById(Integer id);
}
