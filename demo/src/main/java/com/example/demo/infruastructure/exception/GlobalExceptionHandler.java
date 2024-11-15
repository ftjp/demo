/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.infruastructure.exception;


import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.enums.BaseResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *
 * @author yinw
 * @date 2024/6/17
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的异常
     */
    @ExceptionHandler(BaseCustomException.class)
    @ResponseBody
    public BaseResult<Void> customHandler(BaseCustomException e) {
        log.error("自定义的异常:", e);
        return new BaseResult<>(e);
    }

    /**
     * 捕获手动抛出的异常
     *
     * @param request   request
     * @param response  response
     * @param exception 异常信息
     * @return 返回提示信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult<Void> exceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                             Exception exception) {
        log.error("系统错误:", exception);
        return new BaseResult<>(BaseResultEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(
            value = {MethodArgumentNotValidException.class, BindException.class, HttpMessageNotReadableException.class})
    @ResponseBody
    public BaseResult<Void> dealMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("this is controller MethodArgumentNotValidException,param valid failed", e);
        BaseResult<Void> baseResult = new BaseResult<>(BaseResultEnum.PARAM_ERROR);
//        baseResult.setMsg(baseResult.getMsg() + e.getMessage());
        StringBuffer msg = new StringBuffer();
        // 优化报错信息，不然一堆代码错误信息，不够简介
        e.getBindingResult().getFieldErrors().forEach(error ->
                msg.append(error.getField()).append(":").append(error.getDefaultMessage()).append(";"));
        baseResult.setMsg(baseResult.getMsg() + msg);

        return baseResult;
    }
}
