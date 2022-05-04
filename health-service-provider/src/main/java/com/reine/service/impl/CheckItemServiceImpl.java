package com.reine.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.reine.dao.CheckItemDao;
import com.reine.entity.PageResult;
import com.reine.entity.QueryPageBean;
import com.reine.pojo.CheckItem;
import com.reine.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 *
 * @author reine
 * @since 2022/4/21 12:14
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    /**
     * 注入对象
     */
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 添加检查项
     *
     * @param checkItem 检查项
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项分页查询
     *
     * @param queryPageBean 查询条件
     * @return 查询分页结果
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        // 完成分页查询，基于mybatis的分页助手
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total, rows);
    }

    /**
     * 根据id删除检查项
     *
     * @param id 检查项id
     */
    @Override
    public void deleteById(Integer id) {
        // 检查当前检查项是否已经关联检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            // 说明该检查项已被关联进检查组，不允许删除
            throw new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }

    /**
     * 编辑检查项
     *
     * @param checkItem 检查项
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 根据id查询检查项
     *
     * @param id 检查项id
     * @return 检查项
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 查询所有检查项
     *
     * @return 所有检查项
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
