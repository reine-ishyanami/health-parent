package com.reine.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.reine.constant.MessageConstant;
import com.reine.constant.RedisMessageConstant;
import com.reine.entity.Result;
import com.reine.pojo.Member;
import com.reine.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author reine
 * @since 2022/5/2 13:11
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 用户登录
     * <p/>
     * TODO 加密手机号
     * @param map
     * @param response
     * @return
     */
    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        // 从redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCodeInRedis.equals("")) {
            return new Result(false, MessageConstant.VALIDATECODE_ISNULL);
        }
        if (!validateCodeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        Member member = memberService.findByTelephone(telephone);
        if (member == null) {
            // 不是会员，自动注册
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        // 向客户端写入cookie
        Cookie cookie = new Cookie("login-member-telephone", telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
        String json = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone, 60 * 30, json);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

}
