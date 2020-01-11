package com.ijson.blog.exception;

public class BlogBusinessException extends BlogRuntimeException {

    public BlogBusinessException(BlogBusinessExceptionCode code, String extensionMessage) {
        super(code.getCode(), extensionMessage);
    }

    public BlogBusinessException(Integer code, String extensionMessage) {
        super(code, extensionMessage);
    }

    public BlogBusinessException(BlogBusinessExceptionCode code) {
        super(code.getCode(), code.getMessage());
    }

}