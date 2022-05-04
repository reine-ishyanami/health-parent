package com.reine.dao;

import com.github.pagehelper.Page;
import com.reine.pojo.CheckItem;

import java.util.List;

/**
 * @author reine
 * @since 2022/4/21 13:04
 */
public interface CheckItemDao {
    /**
     * 添加检查项
     * @param checkItem 检查项
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页查询
     * @param queryString 查询条件
     * @return 查询结果
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 通过检查项是否被包含于检查组
     * @param id
     * @return
     */
    long findCountByCheckItemId(Integer id);

    /**
     * 通过id删除检查项
     * @param id 检查项id
     */
    void deleteById(Integer id);

    /**
     * 编辑检查项
     * @param checkItem 检查项
     */
    void edit(CheckItem checkItem);

    /**
     * 根据id查询检查项
     * @param id 检查项id
     * @return 检查项
     */
    CheckItem findById(Integer id);

    /**
     * 查询所有检查项
     * @return 所有检查项
     */
    List<CheckItem> findAll();

}
