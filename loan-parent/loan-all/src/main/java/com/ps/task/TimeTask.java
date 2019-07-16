package com.ps.task;

import com.ps.domain.Repayment;
import com.ps.domain.User;
import com.ps.mapper.RepaymentMapper;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务
 *
 * @author zzz
 * @date 2019/07/08
 */

@Component
public class TimeTask {

    @Autowired
    private RepaymentMapper repaymentMapper;

    /**
     * 收费5元
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void shoufei() {
        System.out.println("执行");
        // 查询所有已逾期的还款记录
        List<Repayment> list = repaymentMapper.queryRepaymentsAll();

        for (Repayment repayment : list) {
            repaymentMapper.updateRepaymentMoney(repayment.getId());
        }
    }


    /**
     * 还款通知
     * @throws ParseException
     * @throws IOException
     */
    @Scheduled(cron = "0 0 12 * * ?")
    // @Scheduled(cron = "*/5 * * * * ?")
    public void repaymentNotice() throws ParseException, IOException {
        // 查询所有待还款的还款记录
        List<Repayment> list = repaymentMapper.queryRepaymentByType(2);

        for (Repayment repayment : list) {
            // 查询用户信息
            User userInfo = repaymentMapper.queryUserInfoById(repayment.getBorrow().getId());

            // 当前时间
            Date beginDate = new Date();

            // 还款时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endDate = df.parse(repayment.getRepayment_time());

            long beginTime = beginDate.getTime();
            long endTime2 = endDate.getTime();

            // 间隔天数
            long betweenDays = (long)((endTime2 - beginTime) / (1000 * 60 * 60 *24) + 0.5);

            // 提前五天
            if (betweenDays == 4 || betweenDays == 2 || betweenDays == 0) {
                System.out.println("短信通知");

                Request.Post("http://v.juhe.cn/sms/send")
                        .bodyForm(Form.form().add("mobile", userInfo.getIphone())
                                .add("tpl_id", "170202")
                                .add("tpl_value", "")
                                .add("key", "e0ffec79b9acbb6ff4d7c09df89a6fdf")
                                .build()).execute().returnContent();

            }
        }

    }
}
