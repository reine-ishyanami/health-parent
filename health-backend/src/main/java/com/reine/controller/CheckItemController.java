package com.reine.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.reine.constant.MessageConstant;
import com.reine.entity.PageResult;
import com.reine.entity.QueryPageBean;
import com.reine.entity.Result;
import com.reine.pojo.CheckItem;
import com.reine.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项管理
 *
 * @author reine
 * @since 2022/4/21 12:01
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    /**
     * 查找服务
     * 注意别导错包
     */
    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     *
     * @param checkItem 检查项
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            // 服务调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 检查项分页查询
     *
     * @param queryPageBean 查询条件
     * @return 成功或失败信息
     */
    @RequestMapping("/pageQuery")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        return pageResult;
    }

    /**
     * 删除检查项
     *
     * @param id 检查项id
     * @return 成功或失败信息
     */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/deleteItem")
    public Result delete(Integer id) {
        try {
            checkItemService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 编辑检查项
     *
     * @param checkItem 检查项
     * @return 成功或失败信息
     */
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @RequestMapping("/editItem")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);

        }
        return new Result(false, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 根据检查项id查询检查项
     *
     * @param id 检查项id
     * @return 检查项
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询所有检查项
     *
     * @return 所有检查项
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);

        }
    }

}
