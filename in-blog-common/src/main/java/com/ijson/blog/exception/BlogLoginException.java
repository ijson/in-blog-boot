package com.ijson.blog.exception;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/28 9:20 PM
 */
public class BlogLoginException extends BlogBusinessException {

    public BlogLoginException(BlogBusinessExceptionCode code) {
        super(code);
    }
}
