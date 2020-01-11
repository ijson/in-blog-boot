package com.ijson.blog.exception;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/7 3:06 PM
 */
public class BlogCreateException extends BlogBusinessException {

    public BlogCreateException(BlogBusinessExceptionCode code) {
        super(code);
    }
}