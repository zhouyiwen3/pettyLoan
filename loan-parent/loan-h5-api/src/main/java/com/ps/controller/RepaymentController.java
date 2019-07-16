package com.ps.controller;

import com.alibaba.fastjson.JSONObject;
import com.ps.domain.Message;
import com.ps.domain.Repayment;
import com.ps.service.RepaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * 还款controller
 *
 * @author zzz
 * @date 2019/07/08
 */
@RestController
@RequestMapping("/repayments")
public class RepaymentController {

    private Logger log = LoggerFactory.getLogger(RepaymentController.class);

    @Autowired
    private RepaymentService repaymentService;

    /**
     * 查询还款计划
     * @param id   用户id
     * @return
     */
    @GetMapping("/queryRepay/{id}")
    public Message<List<Repayment>> queryRepayments(@PathVariable Integer id) {
        Message<List<Repayment>> message = new Message<>();

        message.setCode(200);
        message.setMsg("成功");
        message.setData(repaymentService.queryRepaymentsByUserId(id));

        return message;
    }


    /**
     * 还款
     * @param id
     * @throws IOException
     */
    @PostMapping("/repaymentMoney/{id}")
    public Message repaymentMoney(@PathVariable Integer id) throws IOException {
        Message message = new Message();

        message.setCode(200);
        message.setMsg("成功");
        message.setData(repaymentService.repaymentMoney(id));

        return message;
    }


    @GetMapping("/notice/{id}")
    public void notice(@PathVariable Integer id, String result) {
        log.debug("result:"+result);

        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer error_code = jsonObject.getInteger("error_code");

        if (error_code == 0) {
            log.debug("支付成功");

            repaymentService.updateAuditStatus(id);

        } else {
            log.debug("支付失败");
        }

    }


    /**
     * 查询还款信息
     * @param id
     * @return
     */
    @GetMapping("/queryRepaymentType/{id}")
    public Message queryRepaymentTypeById(@PathVariable Integer id) {
        Message message = new Message();

        Repayment repayment = repaymentService.queryRepaymentTypeById(id);
        if (repayment.getRepayment_type() == 3) {
            message.setCode(200);
            message.setMsg("成功");
            return message;
        }

        message.setCode(400);
        message.setMsg("未还清");

        return message;
    }



}
