package com.reine.service;

import com.reine.entity.PageResult;
import com.reine.entity.QueryPageBean;
import com.reine.pojo.CheckItem;

import java.util.List;

/**
 * 检查项服务
 * @author reine
 * @since 2022/4/21 12:05
 */
public interface CheckItemService {
    /**
     * 添加检查项
     * @param checkItem 检查项
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页查询
     * @param queryPageBean 查询条件
     * @return 查询分页结果
     */
    PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 根据id删除检查项
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
