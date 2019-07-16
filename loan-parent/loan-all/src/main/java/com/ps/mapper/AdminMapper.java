package com.ps.mapper;

import com.ps.domain.Borrow;
import com.ps.domain.Repayment;
import com.ps.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


/**
 * 管理员mapper
 *
 * @author zzz
 * @date 2019/07/08
 */

@Mapper
@Repository
public interface AdminMapper {

    /**
     * 修改借款申请的审核状态
     * @param id
     * @param audit_status
     */
    void updateBorrowAuditStatus(Integer id, Integer audit_status);


    /**
     * 查询借款信息
     * @param id
     * @return
     */
    Borrow queryBorrowInfo(Integer id);


    /**
     * 将还款计划添加到数据库中
     * @param list
     */
    void addRepaymentInfo(List<Repayment> list);


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
     * 查询还款信息
     * @param id
     * @return
     */
    Repayment queryRepaymentInfoById(Integer id);


    /**
     * 根据借款id查询用户信息
     * @param id
     * @return
     */
    User queryUserInfoByBorrowId(Integer id);


    /**
     * 添加崔手表
     * @param id
     */
    void addCollectionInfo(Integer id);


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
     * 查询收款明细
     * @return
     */
    List<Repayment> queryDetailReceipts();
}
