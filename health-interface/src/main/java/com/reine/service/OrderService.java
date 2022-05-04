package com.reine.service;

import com.reine.entity.Result;

import java.util.Map;

/**
 * @author reine
 * @since 2022/5/1 21:32
 */
public interface OrderService {
    /**
     * 添加订单信息
     * @param map
     * @return
     */
    Result order(Map map) throws Exception;

    /**
     * 根据id查询预约信息
     * @param id
     * @return
     */
    Map findById(Integer id) throws Exception;
}
