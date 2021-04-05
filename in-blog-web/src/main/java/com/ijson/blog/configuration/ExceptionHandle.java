package com.ijson.blog.configuration;

import com.ijson.blog.exception.BlogBusinessException;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.service.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/28 9:30 PM
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof BlogBusinessException) {
            BlogBusinessException blogBusinessException = (BlogBusinessException) e;
            return Result.error(blogBusinessException);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return Result.error(BlogBusinessExceptionCode.REQUEST_WAY_ERROR);
        } else {
            log.error("【系统异常】:", e);
            return Result.error(BlogBusinessExceptionCode.SYSTEM_ERROR);
        }
    }

}
