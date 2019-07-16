package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Result
 *
 * @author zzz
 * @date 2019/07/08
 */

@Data
public class Result<T> implements Serializable {

    private Integer code;

    private String msg;

    private T rows;

}
