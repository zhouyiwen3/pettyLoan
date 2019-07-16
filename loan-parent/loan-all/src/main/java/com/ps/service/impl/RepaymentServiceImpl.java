package com.ps.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ps.domain.Borrow;
import com.ps.domain.Repayment;
import com.ps.exception.BusinessException;
import com.ps.mapper.BorrowMapper;
import com.ps.mapper.RepaymentMapper;
import com.ps.service.RepaymentService;
import com.ps.util.MD5Util;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * repayment实现类
 *
 * @author zzz
 * @date 2019/07/08
 */

@Service
public class RepaymentServiceImpl implements RepaymentService {

    @Autowired
    private RepaymentMapper repaymentMapper;

    @Autowired
    private BorrowMapper borrowMapper;


    @Value("$(repay_url)")
    private String repayUrl;

    public static final String KEY = "11111";


    @Override
    public List<Repayment> queryRepaymentsByUserId(Integer id) {
        // 查询用户最后一次的借款
        Borrow borrow = borrowMapper.queryBorrowInfoByTime(id);

        // 如果为空，则没有还款计划
        if (borrow == null) {
            throw new BusinessException(401, "没有还款计划");
        }

        // 判断该申请是否审核通过
        if (borrow.getAudit_status() != 1) {
            throw new BusinessException(402, "正在审核中...");
        }

        // 查询还款计划
        return repaymentMapper.queryRepayments(borrow.getId());
    }


    @Override
    public List<String> repaymentMoney(Integer id) throws IOException {
        // 获取最早未还款的订单
        Repayment repayment = repaymentMapper.queryRepaymentByBorrowId(id);

        if (repayment == null) {
            throw new BusinessException(400, "没有要还款的订单");
        }

        // 返回url
        String noticeUrl = "http://localhost:9091/repayments/notice/"+repayment.getId();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key", KEY);
        map.put("orderId", repayment.getId());
        map.put("notice_url", noticeUrl);
        map.put("money", repayment.getRepayment_money());
        map.put("timeMillis", System.currentTimeMillis());
        map.put("secret_key", "dskfjwerqe24");

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

        Content content = Request.Get(repayUrl+"?key="
                + KEY
                + "&orderId=" + newMap.get("orderId")
                + "&notice_url=" + noticeUrl
                + "&money=" + newMap.get("money")
                + "&timeMillis=" + newMap.get("timeMillis")
                + "&md5=" + md5Code)
                .execute().returnContent();

        System.out.println(content);
        System.out.println(content.asString());

        JSONObject jsonObject = JSONObject.parseObject(content.asString());
        JSONObject url = JSONObject.parseObject(jsonObject.getString("result"));

        List<String> list = new ArrayList<>();
        list.add(String.valueOf(repayment.getId()));
        list.add(url.getString("url"));

        return list;
    }


    @Override
    public void updateAuditStatus(Integer id) {
        repaymentMapper.updateAuditStatus(id);
    }


    @Override
    public Repayment queryRepaymentTypeById(Integer id) {
        return repaymentMapper.queryRepaymentTypeById(id);
    }
}
