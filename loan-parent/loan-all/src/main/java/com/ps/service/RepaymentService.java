package com.ps.service;

import com.ps.domain.Repayment;

import java.io.IOException;
import java.util.List;

/**
 * 还款Service
 *
 * @author zzz
 * @date 2019/07/08
 */
public interface RepaymentService {

    /**
     * 根据用户id查询还款计划
     * @param id
     * @return
     */
    List<Repayment> queryRepaymentsByUserId(Integer id);


    /**
     * 还款
     * @param id
     * @return
     * @throws IOException
     */
    List<String> repaymentMoney(Integer id) throws IOException;


    /**
     * 修改还款表状态
     * @param id
     */
    void updateAuditStatus(Integer id);


    /**
     * 根据还款id查询还款信息
     * @param id
     * @return
     */
    Repayment queryRepaymentTypeById(Integer id);
}
