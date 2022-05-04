package com.reine.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.reine.constant.MessageConstant;
import com.reine.constant.RedisConstant;
import com.reine.entity.PageResult;
import com.reine.entity.QueryPageBean;
import com.reine.entity.Result;
import com.reine.pojo.Setmeal;
import com.reine.service.SetmealService;
import com.reine.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author reine
 * @since 2022/4/22 20:52
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 文件上传
     *
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        // 原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String extensionName = originalFilename.substring(index - 1);
        String fileName = UUID.randomUUID() + extensionName;
        try {
            // 上传文件
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            // 上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    /**
     * 新增套餐
     *
     * @param setmeal       套餐
     * @param checkGroupIds 包含的检查组id
     * @return 成功或失败信息
     */
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        try {
            setmealService.add(setmeal, checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     *
     * @param queryPageBean 分页查询条件
     * @return 查询结果
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.pageQuery(queryPageBean);
    }

    /**
     * 根据套餐id查询对应套餐信息
     *
     * @param id 套餐id
     * @return 套餐信息
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据套餐id查询关联的检查组
     *
     * @param id 套餐id
     * @return 关联的检查组列表
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(Integer id) {
        try {
            List<Integer> checkGroupIds = setmealService.findCheckGroupIdsBySetmealId(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 删除套餐及其关联的检查组
     *
     * @param id 套餐id
     * @return 成功或失败信息
     */
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")
    @RequestMapping("/deleteSetmeal")
    public Result deleteSetmeal(Integer id) {
        try {
            setmealService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    /**
     * 编辑套餐
     * @param setmeal
     * @param checkGroupIds
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        try {
            setmealService.edit(setmeal, checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }
}
