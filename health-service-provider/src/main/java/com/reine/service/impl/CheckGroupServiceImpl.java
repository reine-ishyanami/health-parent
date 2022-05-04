package com.reine.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.reine.dao.CheckGroupDao;
import com.reine.entity.PageResult;
import com.reine.entity.QueryPageBean;
import com.reine.pojo.CheckGroup;
import com.reine.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author reine
 * @since 2022/4/21 20:13
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组，同时让检查组关联检查项
     *
     * @param checkGroup   检查组
     * @param checkItemIds 包含的检查项
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        // 新增检查组
        checkGroupDao.add(checkGroup);
        // 设置检查组和检查项的关联关系
        Integer checkGroupId = checkGroup.getId();
        setCheckGroupAndCheckItem(checkItemIds, checkGroupId);
    }

    /**
     * 根据分页条件查询某页
     *
     * @param queryPageBean 分页条件
     * @return 分页结果
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询检查组
     *
     * @param id 检查组id
     * @return 检查组
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id查询对应的检查项id
     *
     * @param id 检查组id
     * @return 检查项列表
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑检查组
     *
     * @param checkGroup   检查组信息
     * @param checkItemIds 包含的检查项id
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        // 修改检查组基本信息
        checkGroupDao.edit(checkGroup);
        // 清理当前检查组关联的检查项
        Integer checkGroupId = checkGroup.getId();
        checkGroupDao.deleteAssociation(checkGroupId);
        // 重新建立检查组和检查项的关联关系
        setCheckGroupAndCheckItem(checkItemIds, checkGroupId);
    }

    /**
     * 查询所有检查组
     *
     * @return 所有检查组
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 根据id删除检查组和其关联的检查项
     *
     * @param id 检查组id
     */
    @Override
    public void deleteById(Integer id) {
        // 查询当前检查组是否关联套餐
        long count = checkGroupDao.findCountByCheckGroupId(id);
        if (count > 0) {
            // 已关联套餐，不能删除，抛出异常
            throw new RuntimeException();
        }
        checkGroupDao.deleteAssociation(id);
        checkGroupDao.deleteById(id);
    }

    /**
     * 建立检查组和检查项的多对多关系
     *
     * @param checkItemIds 检查项id
     * @param checkGroupId 检查组id
     */
    private void setCheckGroupAndCheckItem(Integer[] checkItemIds, Integer checkGroupId) {
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemId : checkItemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkItemId);
                checkGroupDao.setCheckGroupAndCheckIItem(map);
            }
        }
    }

}