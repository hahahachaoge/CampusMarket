package com.campus.exception;

import com.campus.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //业务异常
    @ExceptionHandler(RuntimeException.class)
    public Result<String> runtimeException(RuntimeException e){
        log.error("业务异常：",e);
        return Result.error(e.getMessage());
    }

    //系统异常
    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception e){
        log.error("系统异常：",e);
        return Result.error("系统异常");
    }
}
