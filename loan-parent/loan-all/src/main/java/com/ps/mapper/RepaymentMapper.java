package com.ps.mapper;

import com.ps.domain.Repayment;
import com.ps.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 还款mapper
 *
 * @author zzz
 * @date 2019/07/08
 */

@Mapper
@Repository
public interface RepaymentMapper {

    /**
     * 查询还款计划根据用户id
     * @param id
     * @return
     */
    List<Repayment> queryRepayments(Integer id);


    /**
     * 获取最早未还款的订单
     * @param id
     * @return
     */
    Repayment queryRepaymentByBorrowId(Integer id);


    /**
     * 查询所有已逾期的还款记录
     * @return
     */
    List<Repayment> queryRepaymentsAll();


    /**
     * 修改还款表金额（收费5元）
     * @param id
     */
    void updateRepaymentMoney(Integer id);


    /**
     * 根据还款状态查询还款记录
     * @param type
     * @return
     */
    List<Repayment> queryRepaymentByType(Integer type);


    /**
     * 根据借款表id查询用户信息
     * @param id
     * @return
     */
    User queryUserInfoById(Integer id);


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
