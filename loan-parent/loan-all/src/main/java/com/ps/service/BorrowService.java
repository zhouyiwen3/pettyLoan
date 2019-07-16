package com.ps.service;

import com.ps.domain.Borrow;
import com.ps.domain.User;

/**
 * 借款Service
 *
 * @author zzz
 * @date 2019/07/08
 */

public interface BorrowService {
    /**
     * 查询用户信息根据用户id
     * @param id
     * @return
     */
    User queryUserInfoById(Integer id);


    /**
     * 查询用户当前月所借金额
     * @param id
     * @return
     */
    Borrow queryBorrowMoneyById(Integer id);


    /**
     * 借款申请
     * @param id
     * @param borrow
     */
    void askForBorrow(Integer id, Borrow borrow);
}
