package com.ps.advice;

import com.ps.domain.Message;
import com.ps.exception.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常通知
 *
 * @author zzzz
 * @date 2019/07/04
 */

@ControllerAdvice
public class ExceptionAdvice {

    /**
     *
     * @param e
     * @return Message 消息封装类
     * BusinessException  异常类
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Message businessExceotion(BusinessException e){
        //当捕获到此异常时,返回Message对象到前台-------所以加上@ResponseBody注解
        return new Message(e.getCode(), e.getMsg());
    }

}
