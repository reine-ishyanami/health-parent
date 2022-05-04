package com.reine.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.reine.constant.MessageConstant;
import com.reine.constant.RedisMessageConstant;
import com.reine.entity.Result;
import com.reine.pojo.Order;
import com.reine.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 处理体检预约
 *
 * @author reine
 * @since 2022/5/1 21:19
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 预约
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");

        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if (validateCodeInRedis.equals("")) {
            return new Result(false, MessageConstant.VALIDATECODE_ISNULL);
        }
        if (!validateCodeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        jedisPool.getResource().del(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        // 设置预约类型，微信预约，电话预约
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        Result result;
        try {
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_NETWORK_FAIL);
        }
        if (result.isFlag()) {
            System.out.println("手机号" + telephone + "预约成功，时间为" + map.get("orderDate"));
        }
        return result;
    }

    /**
     * 根据id查询预约信息
     *
     * @param id 预约id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
