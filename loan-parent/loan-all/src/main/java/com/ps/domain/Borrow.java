package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 借款类
 *
 * @author zzz
 * @date 2019/07/08
 */


@Data
public class Borrow implements Serializable {

    private Integer id;

    private Integer borrow_money;

    private Integer borrow_number;

    private String borrow_time;

    private Integer total_interest;

    private Integer audit_status;

    private String code;

    private User user;

}
