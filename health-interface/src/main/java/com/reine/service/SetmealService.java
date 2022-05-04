package com.reine.service;

import com.reine.entity.PageResult;
import com.reine.entity.QueryPageBean;
import com.reine.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author reine
 * @since 2022/4/23 13:07
 */
public interface SetmealService {
    /**
     * 新增套餐
     * @param setmeal 套餐信息
     * @param checkGroupIds 包含的检查组id
     */
    void add(Setmeal setmeal, Integer[] checkGroupIds);

    /**
     * 分页查询
     * @param queryPageBean 查询条件
     * @return 查询结果
     */
    PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 根据套餐id查询对应套餐信息
     * @param id 套餐id
     * @return 套餐信息
     */
    Setmeal findById(Integer id);

    /**
     * 根据套餐id查询关联的检查组
     * @param id 套餐id
     * @return 关联的检查组列表
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 根据id删除套餐
     * @param id 套餐id
     */
    void deleteById(Integer id);

    /**
     * 编辑套餐及其对应的检查组信息
     * @param setmeal 套餐信息
     * @param checkGroupIds 检查组列表
     */
    void edit(Setmeal setmeal, Integer[] checkGroupIds);

    /**
     * 查询所有套餐
     * @return 所有套餐信息
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐预约占比数量
     * @return
     */
    List<Map<String, Object>> findSetmealCount();
}