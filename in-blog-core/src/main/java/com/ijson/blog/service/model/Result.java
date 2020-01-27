package com.ijson.blog.service.model;

import com.ijson.blog.exception.BlogBusinessException;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogRuntimeException;
import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/21 2:18 PM
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T result;


    public Result(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }


    public Result() {

    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result ok() {
        return new Result(0, "");
    }


    public static Result ok(Object t) {
        return new Result(0, "", t);
    }

    public static Result error(BlogBusinessException e) {
        return new Result(e.getErrorCode(), e.getMessage());
    }

    public static Result error(String message) {
        return new Result(-1, message);
    }

    public static Result ok(String message) {
        return new Result(0, message);
    }

    public static Result error(BlogRuntimeException runtimeException) {
        return new Result(runtimeException.getErrorCode(), runtimeException.getMessage());
    }

    public static Result error(int code, String message) {
        return new Result(code, message);
    }

    public static Result error(BlogBusinessExceptionCode requestWayError) {
        return new Result(requestWayError.getCode(), requestWayError.getMessage());
    }
}
