package com.reine.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.reine.dao.OrderSettingDao;
import com.reine.pojo.OrderSetting;
import com.reine.service.OrderSettingService;
import com.reine.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预约设置服务
 *
 * @author reine
 * @since 2022/4/29 9:59
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 将预约设置数据批量插入数据库
     *
     * @param data 预约数据
     */
    @Override
    public void add(List<OrderSetting> data) {
        if (data != null && data.size() > 0) {
            for (OrderSetting orderSetting : data) {
                // 判断当前日期是否已经进行预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0) {
                    // 已进行预约设置，更新数据
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    // 未进行预约设置，插入数据
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 根据年月查询预约设置信息
     *
     * @param date 年月信息
     * @return 预约设置信息
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String[] yNm = date.split("-");
        String begin = date + "-1";
        String end = date + "-" + DateUtils.getDayInMonth(yNm[0], yNm[1]);
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        // 根据日期范围查询预约设置数据
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String, Object> m = new HashMap<>();
                // 获取日期（几号）
                m.put("date", orderSetting.getOrderDate().getDate());
                // 总人数
                m.put("number", orderSetting.getNumber());
                // 已预约人数
                m.put("reservations", orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }

    /**
     * 修改指定日期的可预约人数
     *
     * @param orderSetting 预约设置信息
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        // 根据日期查询是否已经进行预约设置
        Date orderDate = orderSetting.getOrderDate();
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        if (count > 0) {
            // 当前日期已经进行预约设置，执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            // 当前日期没有进行预约设置，执行插入操作
            orderSettingDao.add(orderSetting);
        }
    }

}
