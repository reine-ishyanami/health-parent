package com.reine.service;

import com.reine.entity.PageResult;
import com.reine.entity.QueryPageBean;
import com.reine.pojo.CheckGroup;

import java.util.List;

/**
 * @author reine
 * @since 2022/4/21 19:39
 */
public interface CheckGroupService {
    /**
     * 添加检查组，同时让检查组关联检查项
     *
     * @param checkGroup   检查组
     * @param checkItemIds 包含的检查项
     */
    void add(CheckGroup checkGroup, Integer[] checkItemIds);

    /**
     * 根据分页条件查询某页
     *
     * @param queryPageBean 分页条件
     * @return 分页结果
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询检查组
     *
     * @param id 检查组id
     * @return 检查组
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询对应关联的检查项id
     *
     * @param id 检查组id
     * @return 检查项id列表
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 编辑检查组
     * @param checkGroup 检查组信息
     * @param checkItemIds 包含的检查项id
     */
    void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    /**
     * 查询所有检查组
     * @return 所有检查组
     */
    List<CheckGroup> findAll();

    /**
     * 根据id删除检查组和其关联的检查项
     * @param id 检查组id
     */
    void deleteById(Integer id);
}
