package com.reine.dao;

import com.reine.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author reine
 * @since 2022/4/29 10:03
 */
public interface OrderSettingDao {

    /**
     * 添加预约设置信息
     * @param orderSetting 预约设置信息
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据预约日期更改预约人数
     * @param orderSetting 预约设置信息
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 根据预约日期查询数据条数
     * @param orderDate 预约日期
     * @return 数据条数
     */
    long findCountByOrderDate(Date orderDate);

    /**
     * 查询范围内的预约设置数据
     * @param map 查询范围
     * @return 预约设置数据
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    /**
     * 根据日期查询预约设置
     * @param date 预约日期
     */
    OrderSetting findByOrderDate(Date date);

    /**
     * 根据预约设置更新预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
