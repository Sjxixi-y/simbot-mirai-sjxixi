package com.sjxixi.simbotmiraisjxixi.controller;

import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    //
    @ExceptionHandler(Exception.class)
    public ResultVo exception(Exception e) {
        return ResultVo.fail(e.getMessage(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResultVo exception(RuntimeException e) {
        return ResultVo.fail(e.getMessage(), null);
    }
}
