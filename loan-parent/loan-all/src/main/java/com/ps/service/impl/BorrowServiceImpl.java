package com.ps.service.impl;

import com.ps.domain.Borrow;
import com.ps.domain.Repayment;
import com.ps.domain.User;
import com.ps.exception.BusinessException;
import com.ps.mapper.BorrowMapper;
import com.ps.mapper.UserMapper;
import com.ps.service.BorrowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * borrow实现类
 *
 * @author zzz
 * @date 2019/07/08
 */

@Service
public class BorrowServiceImpl implements BorrowService {

    private Logger log = LoggerFactory.getLogger(BorrowServiceImpl.class);

    @Autowired
    private BorrowMapper borrowMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserInfoById(Integer id) {
        return borrowMapper.queryUserInfoById(id);
    }

    @Override
    public Borrow queryBorrowMoneyById(Integer id) {
        // 获取当前月份
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String month = df.format(new Date());
        log.info("当月月份："+month);

        return borrowMapper.queryBorrowMoneyById(id, month);
    }


    @Override
    public void askForBorrow(Integer id, Borrow borrow) {
        // 查询用户信息
        User user = borrowMapper.queryUserInfoById(borrow.getUser().getId());
        log.debug("user:" + user);

        // 判断个人信息是否完善
        if (user.getName() == null && user.getIdentity_card() == null && user.getBank_card() == null) {
            throw new BusinessException(401, "未完善个人信息");
        }

        // 判断用户是否审核通过
        if (user.getAudit_status() != 1) {
            throw new BusinessException(402, "用户未审核");
        }

        // 判断验证码是否正确
        String code = userMapper.isTrueByCode(id);
        if (!code.equals(borrow.getCode())) {
            throw new BusinessException(403, "验证码错误");
        }

        // 获取当前月份
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String month = df.format(new Date());
        log.info("当月月份："+month);

        // 获取用户当前月借款信息
        Borrow borrowInfo = borrowMapper.queryBorrowMoneyById(borrow.getUser().getId(), month);
        log.debug("借款信息：" + borrowInfo);

        // 如果为空，则该用户本月未借钱
        if (borrowInfo == null) {
            // 判断用户上次借款是否已还清
            Borrow borrowInfoPrevious = borrowMapper.queryBorrowInfoByTime(borrow.getUser().getId());

            if (borrowInfoPrevious != null) {
                List<Repayment> repayment = borrowMapper.queryNotRepaymentInfo(borrowInfoPrevious.getId());

                // 不为空，则未还清，不能借款
                if (repayment.size() != 0) {
                    throw new BusinessException(405, "请先还清所有账单");
                }

                // 为空，已还清所有账单
                // 判断借款金额是否超出最大借款金额
                if (user.getQuota() < borrow.getBorrow_money()) {
                    throw new BusinessException(403, "您的最大借款金额为" + user.getQuota() + "，请重新输入");
                }

                // 添加借款信息到数据库
                borrow.setId(borrowMapper.queryMaxId()+1);
                borrowMapper.addBorrowInfo(borrow);
                return;
            }

            // 判断借款金额是否超出最大借款金额
            if (user.getQuota() < borrow.getBorrow_money()) {
                throw new BusinessException(403, "您的最大借款金额为" + user.getQuota() + "，请重新输入");
            }

            // 添加借款信息到数据库
            borrowMapper.addBorrowInfo(borrow);
            return;
        }


        if (borrowInfo.getAudit_status() == 0) {
            throw new BusinessException(406, "您还有借款申请正在审核");
        }

        // 否则该用户已借钱，判断该用户的是否已还清
        List<Repayment> repayment = borrowMapper.queryNotRepaymentInfo(borrowInfo.getId());

        // 不为空，则未还清，不能借款
        if (repayment.size() != 0) {
            throw new BusinessException(405, "请先还清所有账单");
        }

        // 为空，已还清所有账单
        // 判断借款金额是否超出最大借款金额
        if (user.getQuota() < borrow.getBorrow_money()) {
            throw new BusinessException(403, "您的最大借款金额为" + user.getQuota() + "，请重新输入");
        }

        // 添加借款信息到数据库
        borrowMapper.addBorrowInfo(borrow);
    }
}
