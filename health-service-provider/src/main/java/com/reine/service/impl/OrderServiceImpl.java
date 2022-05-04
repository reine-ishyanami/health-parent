package com.reine.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.reine.constant.MessageConstant;
import com.reine.dao.MemberDao;
import com.reine.dao.OrderDao;
import com.reine.dao.OrderSettingDao;
import com.reine.entity.Result;
import com.reine.pojo.Member;
import com.reine.pojo.Order;
import com.reine.pojo.OrderSetting;
import com.reine.service.OrderService;
import com.reine.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 *
 * @author reine
 * @since 2022/5/2 10:15
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 体检预约
     *
     * @param map
     * @return
     */
    @Override
    public Result order(Map map) throws Exception {
        // 检测用户选择的预约日期是否进行了预约设置，如果没有进行预约设置则无法预约
        String orderDate = (String) map.get("orderDate");
        // 预约日期
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            // 指定日期没有进行预约设置，无法进行预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        // 可预约人数
        int number = orderSetting.getNumber();
        // 已预约人数
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            // 约满
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        // 判断是否同一个用户
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            // 判断是否重复预约
            Integer memberId = member.getId();
            Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId, date, setmealId);
            // 条件查询
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                // 用户重复预约，预约失败
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            // 用户不是会员，注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        // 预约成功，增加当日预约人数
        Order order = new Order();
        // 设置会员id
        order.setMemberId(member.getId());
        // 预约日期
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        // 预约类型
        order.setOrderType((String) map.get("orderType"));
        // 到诊状态
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        // 套餐id
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        // 更新当日预约人数
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    /**
     * 根据id查询预约信息
     * 体检人姓名
     * 预约日期
     * 套餐名称
     * 预约类型
     *
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) throws Exception {
        Map detail = orderDao.findById4Detail(id);
        if (detail != null) {
            Date orderDate = (Date) detail.get("orderDate");
            detail.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return detail;
    }
}
