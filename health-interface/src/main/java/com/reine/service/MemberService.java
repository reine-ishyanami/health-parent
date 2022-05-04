package com.reine.service;

import com.reine.pojo.Member;

import java.util.List;

/**
 * @author reine
 * @since 2022/5/2 13:18
 */
public interface MemberService {
    /**
     * 根据手机号查询用户
     *
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 注册用户
     *
     * @param member
     */
    void add(Member member);

    /**
     * 根据月份查询会员数量
     * @param months
     * @return
     */
    List<Integer> findMemberCountByMonths(List<String> months);
}
