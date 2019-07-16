package com.ps.controller;

import com.ps.domain.Borrow;
import com.ps.domain.Message;
import com.ps.domain.User;
import com.ps.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 借款controller
 *
 * @author zzz
 * @date 2019/07/08
 */

@RestController
@RequestMapping("/borrows")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    /**
     * 查询最大借款金额
     * @param id    用户id
     * @return
     */
    @GetMapping("/{id}")
    public Message queryMaxBorrowMoney(@PathVariable Integer id) {
        Message message = new Message();
        // 查询用户额度
        User user = borrowService.queryUserInfoById(id);

        // 查询用户本月已借总金额
        Borrow borrowInfo = borrowService.queryBorrowMoneyById(id);

        message.setMsg("成功");
        message.setCode(200);

        // 为空，则该用户本月未借款
        if (borrowInfo == null) {
            message.setData(user.getQuota());
            return message;
        }

        // 计算最大借款金额
        message.setData(user.getQuota() - borrowInfo.getBorrow_money());

        return message;
    }


    /**
     * 借款申请
     * @param id   验证码id
     * @param borrow   借款申请信息
     * @return
     */
    @PostMapping("/askForBorrow/{id}")
    public Message askForBorrow(@PathVariable Integer id, @RequestBody Borrow borrow) {
        Message message = new Message();

        borrowService.askForBorrow(id, borrow);

        message.setCode(200);
        message.setMsg("成功");

        return message;
    }


}
