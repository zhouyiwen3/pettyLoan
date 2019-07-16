package com.ps.service.impl;

import com.ps.domain.Borrow;
import com.ps.domain.Repayment;
import com.ps.domain.User;
import com.ps.mapper.AdminMapper;
import com.ps.service.AdminService;
import com.ps.util.MD5Util;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * admin实现类
 *
 * @author zzz
 * @date 2019/07/08
 */

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;


    /**
     * 催收url
     */
    @Value("$(collection_url)")
    private String collectionUrl;


    @Override
    public void updateUserAuditStatus(Integer id, Integer audit_status) {
        adminMapper.updateUserAuditStatus(id, audit_status);
    }


    @Override
    public void setUpInterest(BigDecimal interest) {
        adminMapper.setUpInterest(interest);
    }


    @Override
    public void auditOfLoans(Integer id, Integer audit_status) throws ParseException {
        // 修改借款申请的审核状态
        adminMapper.updateBorrowAuditStatus(id, audit_status);

        // 生成还款计划
        // 查询借款信息
        Borrow borrowInfo = adminMapper.queryBorrowInfo(id);

        List<Repayment> list = new ArrayList<>();

        Integer money = (borrowInfo.getBorrow_money()+borrowInfo.getTotal_interest())/borrowInfo.getBorrow_number();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(borrowInfo.getBorrow_time()));


        for (int i = 0; i < borrowInfo.getBorrow_number(); i++) {
            cal.add(Calendar.MONTH, 1);

            if (cal.get(Calendar.MONTH) > 12) {
                cal.add(Calendar.YEAR, 1);
                cal.set(Calendar.MONTH, 1);
            }

            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));

            Date date = cal.getTime();

            Repayment repayment = new Repayment();
            repayment.setRepayment_money(money);
            repayment.setRepayment_time(df.format(date));
            Borrow borrow = new Borrow();
            borrow.setId(id);
            repayment.setBorrow(borrow);

            list.add(repayment);
        }

        // 将还款计划添加到数据库中
        adminMapper.addRepaymentInfo(list);
    }


    @Override
    public void collectionMoney(Integer id) throws IOException {
        // 根据还款id查询还款信息
        Repayment repayment = adminMapper.queryRepaymentInfoById(id);

        // 根据借款id查询用户信息
        User user = adminMapper.queryUserInfoByBorrowId(repayment.getBorrow().getId());

        // 加入崔手表
        adminMapper.addCollectionInfo(id);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key", 22222);
        map.put("orderId", id);
        map.put("idcard", user.getIdentity_card());
        map.put("money", repayment.getRepayment_money());
        map.put("phone", user.getIphone());
        map.put("secret_key", "gwewerer1er3g");

        // 对map的key进行排序
        List<String> keySet = new ArrayList<>(map.keySet());
        Collections.sort(keySet, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });

        // 对key进行倒叙
        Map<String, Object> newMap = new LinkedHashMap<>();
        for(int i = keySet.size()-1; i >= 0; i--) {
            for (String s : map.keySet()) {
                if (keySet.get(i).equals(s)) {
                    newMap.put(keySet.get(i), map.get(s));
                }
            }
        }

        // 将值拼接
        StringBuilder sb = new StringBuilder();
        for (String key : newMap.keySet()) {
            sb.append(newMap.get(key));
        }


        // 用md5进行加密
        String md5Code = MD5Util.getMd5Code(sb.toString());

        Request.Get(collectionUrl+"?key="
                + newMap.get("key")
                + "&orderId=" + newMap.get("orderId")
                + "&idcard=" + newMap.get("idcard")
                + "&money=" + newMap.get("money")
                + "&phone=" + newMap.get("phone")
                + "&md5=" + md5Code)
                .execute().returnContent();
    }



    @Override
    public List<User> queryUserInfos() {
        return adminMapper.queryUserInfos();
    }


    @Override
    public List<Borrow> queryBorrowInfoAll() {
        return adminMapper.queryBorrowInfoAll();
    }


    @Override
    public List<Repayment> queryRepaymentInfosAll() {
        return adminMapper.queryRepaymentInfosAll();
    }


    @Override
    public List<Repayment> queryRepaymentInfosByType() {
        return adminMapper.queryRepaymentInfosByType();
    }


    @Override
    public BigDecimal queryInterestConfig() {
        return adminMapper.queryInterestConfig();
    }


    @Override
    public List<Repayment> queryDetailReceipts() {
        return adminMapper.queryDetailReceipts();
    }
}
