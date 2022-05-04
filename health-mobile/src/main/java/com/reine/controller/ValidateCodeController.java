package com.reine.controller;

import com.reine.constant.MessageConstant;
import com.reine.constant.RedisMessageConstant;
import com.reine.entity.Result;
import com.reine.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作
 *
 * @author reine
 * @since 2022/5/1 20:01
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 模拟发送用户在线体检预约验证码
     *
     * @param telephone 手机号
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 生成6位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        // 保存验证码到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, validateCode.toString());
        // 模拟给用户发送验证码
        // 方法1：直接在控制台打印验证码，返回成功信息
        System.out.println("手机号：" + telephone + "---在线体检预约验证码是：" + validateCode);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        // 方法2：将验证码发送给前端，前端将验证码打印到浏览器控制台或者直接对验证码输入框进行填充
        // return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS, validateCode);
    }

    /**
     * 模拟发送用户快速登录验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        // 生成6位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        // 保存验证码到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, validateCode.toString());
        // 模拟给用户发送验证码
        // 方法1：直接在控制台打印验证码，返回成功信息
        System.out.println("手机号：" + telephone + "---快速登录验证码是：" + validateCode);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


}
