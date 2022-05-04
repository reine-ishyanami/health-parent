package com.reine.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.reine.constant.MessageConstant;
import com.reine.entity.Result;
import com.reine.pojo.Setmeal;
import com.reine.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐
 *
 * @author reine
 * @since 2022/4/30 7:48
 */
@RequestMapping("/setmeal")
@RestController
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐
     *
     * @return 套餐信息
     */
    @RequestMapping("/getAllSetmeal")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 查询套餐信息
     * @param id 套餐id
     * @return 套餐信息
     */
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
}