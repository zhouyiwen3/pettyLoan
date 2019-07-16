package com.ps.mapper;

import com.ps.domain.Borrow;
import com.ps.domain.Repayment;
import com.ps.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 借款mapper
 *
 * @author zzz
 * @date 2019/07/08
 */

@Mapper
@Repository
public interface BorrowMapper {
    /**
     * 查询用户信息
     * @param id
     * @return
     */
    User queryUserInfoById(Integer id);


    /**
     * 查询用户当前月所借金额
     * @param id
     * @param month
     * @return
     */
    Borrow queryBorrowMoneyById(Integer id, String month);


    /**
     * 添加借款信息到数据库
     * @param borrow
     *
     */
    void addBorrowInfo(Borrow borrow);


    /**
     * 查询借款表中最大的ID
     * @return
     */
    Integer queryMaxId();


    /**
     * 修改该用户的已借款金额
     * @param borrow
     */
    void updateBorrowInfoMoney(Borrow borrow);


    /**
     * 判断该用户的是否已还清
     * @param id
     * @return
     */
    List<Repayment> queryNotRepaymentInfo(Integer id);


    /**
     * 查询该用户最后一次借款信息
     * @param id
     * @return
     */
    Borrow queryBorrowInfoByTime(Integer id);
}
