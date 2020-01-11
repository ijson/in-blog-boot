package com.ijson.blog.exception;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/7 3:06 PM
 */
public class ReplyCreateException extends BlogBusinessException {

    public ReplyCreateException(BlogBusinessExceptionCode code) {
        super(code);
    }

    public ReplyCreateException(BlogBusinessExceptionCode code, String message) {
        super(code.getCode(), message);
    }
}