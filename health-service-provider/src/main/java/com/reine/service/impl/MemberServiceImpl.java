package com.reine.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.reine.dao.MemberDao;
import com.reine.pojo.Member;
import com.reine.service.MemberService;
import com.reine.utils.DateUtils;
import com.reine.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author reine
 * @since 2022/5/2 13:48
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 根据手机号查询用户
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 注册用户
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            // 使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    /**
     * 根据月份查询会员数量
     *
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            // 切分字符串中的年月
            String[] yNm = month.split("\\.");
            String day = DateUtils.getDayInMonth(yNm[0], yNm[1]);
            String date = month + "." + day;
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);
        }
        return memberCount;
    }
}
