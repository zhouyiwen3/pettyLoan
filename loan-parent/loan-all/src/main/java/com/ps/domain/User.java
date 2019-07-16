package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户类
 *
 * @author zzz
 * @date 2019/07/08
 */

@Data
public class User implements Serializable {

    private Integer id;

    private String name;

    private String iphone;

    private String password;

    private String identity_card;

    private String regist_time;

    private String opening_bank;

    private String bank_card;

    private String identity_card_before_img;

    private String identity_card_after_img;

    private String bank_img;

    private Integer quota;

    private Integer audit_status;

    private String code;

}
