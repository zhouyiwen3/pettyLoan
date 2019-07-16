package com.ps.domain;

import lombok.Data;

import java.io.Serializable;


/**
 * 还款类
 *
 * @author zzz
 * @date 2019/07/08
 */

@Data
public class Repayment implements Serializable {

    private Integer id;

    private Integer repayment_money;

    private String repayment_time;

    private String actual_repayment_time;

    private Integer repayment_type;

    private Borrow borrow;

}
