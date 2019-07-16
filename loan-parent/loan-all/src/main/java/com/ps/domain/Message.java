package com.ps.domain;

import lombok.Data;

import java.io.Serializable;


/**
 * Message
 *
 * @author zzz
 * @date 2019/07/08
 */

@Data
public class Message<T> implements Serializable {

    private String msg;

    private Integer code;

    private T data;

    public Message(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public Message() {
    }
}
