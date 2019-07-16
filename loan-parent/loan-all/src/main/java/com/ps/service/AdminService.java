package com.ps.service;

import com.ps.domain.Borrow;
import com.ps.domain.Repayment;
import com.ps.domain.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;


/**
 * 管理员Service
 *
 * @author zzz
 * @date 2019/07/08
 */

public interface AdminService {

    /**
     * 修改用户审核状态
     * @param id
     * @param audit_status
     */
    void updateUserAuditStatus(Integer id, Integer audit_status);


    /**
     * 设置利息
     * @param interest
     */
    void setUpInterest(BigDecimal interest);


    /**
     * 审核贷款
     * @param id
     * @param audit_status
     * @throws ParseException
     */
    void auditOfLoans(Integer id, Integer audit_status) throws ParseException;


    /**
     * 催收
     * @param id
     * @throws IOException
     */
    void collectionMoney(Integer id) throws IOException;


    /**
     * 查询所有用户信息
     * @return
     */
    List<User> queryUserInfos();


    /**
     * 查询所有借款信息
     * @return
     */
    List<Borrow> queryBorrowInfoAll();


    /**
     * 查询所有还款信息
     * @return
     */
    List<Repayment> queryRepaymentInfosAll();


    /**
     * 查询所有待还款、已逾期的还款计划
     * @return
     */
    List<Repayment> queryRepaymentInfosByType();


    /**
     * 查询利息
     * @return
     */
    BigDecimal queryInterestConfig();


    /**
     * 收款明细
     * @return
     */
    List<Repayment> queryDetailReceipts();
}
