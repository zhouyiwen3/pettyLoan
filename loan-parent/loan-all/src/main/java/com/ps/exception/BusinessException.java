package com.ps.exception;

import lombok.Data;

/**
 * 异常
 *
 * @author zzz
 * @date 2019/07/08
 */

@Data
public class BusinessException extends RuntimeException {

    private Integer code;

    private String msg;

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
