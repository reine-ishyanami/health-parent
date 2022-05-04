package com.reine.dao;

import com.github.pagehelper.Page;
import com.reine.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author reine
 * @since 2022/4/23 13:13
 */
public interface SetmealDao {
    /**
     * 设置套餐信息
     * @param setmeal 套餐信息
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐包含的检查组信息
     * @param map 套餐id和检查组id
     */
    void setSetmealAndCheckGroup(Map map);

    /**
     * 根据条件查询套餐信息
     * @param queryString 分页条件
     * @return 套餐信息
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 根据套餐id查询对应套餐信息
     * @param id 套餐id
     * @return 套餐信息
     */
    Setmeal findById4Detail(Integer id);

    /**
     * 根据套餐id查询关联的检查组id
     * @param id 套餐id
     * @return 关联的检查组
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 根据id删除套餐
     * @param id 套餐id
     */
    void deleteById(Integer id);

    /**
     * 删除指定套餐id关联的检查组信息
     * @param id 套餐id
     */
    void deleteAssociation(Integer id);

    /**
     * 编辑套餐
     * @param setmeal 套餐信息
     */
    void edit(Setmeal setmeal);

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
