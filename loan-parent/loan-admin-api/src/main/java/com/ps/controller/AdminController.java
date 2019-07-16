package com.ps.controller;

import com.ps.domain.*;
import com.ps.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;


/**
 * 管理员controller
 *
 * @author zzz
 * @date 2019/07/08
 */


@RestController
@RequestMapping("/admin")
public class AdminController {

    private Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;


    /**
     * 审核贷款
     * @param id   借款id
     * @param audit_status   审核状态
     * @return
     */
    @PutMapping("/auditLoans/{id}/{audit_status}")
    public Message auditOfLoans(@PathVariable Integer id, @PathVariable Integer audit_status) throws ParseException {
        Message message = new Message();

        adminService.auditOfLoans(id, audit_status);

        message.setCode(200);
        message.setMsg("成功");

        return message;
    }


    /**
     * 审核用户资料
     * @param id
     * @param audit_status
     * @return
     */
    @PutMapping("/auditOfUser/{id}/{audit_status}")
    public Message auditOfUser(@PathVariable Integer id, @PathVariable Integer audit_status) {
        Message message = new Message();

        adminService.updateUserAuditStatus(id, audit_status);

        message.setCode(200);
        message.setMsg("成功");
        return message;
    }


    /**
     * 设置利息
     * @param interest
     * @return
     */
    @PostMapping("/setInterest")
    public Message setUpInterest(@RequestParam BigDecimal interest) {
        Message message = new Message();

        adminService.setUpInterest(interest);

        message.setCode(200);
        message.setMsg("成功");

        return message;
    }


    /**
     * 催收
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/collectionMoney/{id}")
    public Message collectionMoney(@PathVariable Integer id) throws IOException {
        adminService.collectionMoney(id);

        return new Message(200, "成功");
    }


    /**
     * 查询所有用户信息
     * @return
     */
    @GetMapping("/queryUserInfo")
    public Result<List<User>> queryUserInfos() {
        Result<List<User>> result = new Result<>();

        List<User> list = adminService.queryUserInfos();

        result.setCode(0);
        result.setMsg("成功");
        result.setRows(list);

        return result;
    }


    /**
     * 查询所有借款信息
     * @return
     */
    @GetMapping("/queryBorrowInfos")
    public Result<List<Borrow>> queryBorrowInfo() {
        Result<List<Borrow>> result = new Result<>();

        List<Borrow> list = adminService.queryBorrowInfoAll();

        result.setCode(0);
        result.setMsg("成功");
        result.setRows(list);

        return result;
    }


    /**
     * 查询所有还款信息
     * @return
     */
    @GetMapping("/queryRepaymentInfosAll")
    public Result<List<Repayment>> queryRepaymentInfosAll() {
        Result<List<Repayment>> result = new Result<>();

        List<Repayment> list = adminService.queryRepaymentInfosAll();

        result.setCode(0);
        result.setMsg("成功");
        result.setRows(list);

        return result;
    }


    /**
     * 查询所有待还款、已逾期的还款计划
     * @return
     */
    @GetMapping("/queryRepaymentInfosByType")
    public Result<List<Repayment>> queryRepaymentInfosByType() {
        Result<List<Repayment>> result = new Result<>();

        List<Repayment> list = adminService.queryRepaymentInfosByType();

        result.setCode(0);
        result.setMsg("成功");
        result.setRows(list);

        return result;
    }


    /**
     * 查询利息
     * @return
     */
    @GetMapping("/queryInterestConfig")
    public Result<BigDecimal> queryInterestConfig() {
        Result<BigDecimal> result = new Result<>();

        BigDecimal interest = adminService.queryInterestConfig();

        result.setCode(0);
        result.setMsg("成功");
        result.setRows(interest);

        return result;
    }


    /**
     * 查询收款明细
     * @return
     */
    @GetMapping("/queryDetailReceipts")
    public Result<List<Repayment>> queryDetailReceipts() {
        Result<List<Repayment>> result = new Result<>();

        result.setCode(0);
        result.setMsg("成功");
        result.setRows(adminService.queryDetailReceipts());

        return result;
    }

}
