package com.reine.service;

import com.reine.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author reine
 * @since 2022/4/29 9:51
 */
public interface OrderSettingService {

    /**
     * 将预约设置数据批量插入数据库
     * @param data 预约数据
     */
    void add(List<OrderSetting> data);

    /**
     * 根据年月查询预约设置信息
     * @param date 年月信息
     * @return 预约设置信息
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 修改指定日期的可预约人数
     * @param orderSetting 预约设置信息
     */
    void editNumberByDate(OrderSetting orderSetting);
}
