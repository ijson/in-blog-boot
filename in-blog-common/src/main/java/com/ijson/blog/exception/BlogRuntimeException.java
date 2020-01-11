package com.ijson.blog.exception;

public class BlogRuntimeException extends RuntimeException {


    private int errorCode = BlogBusinessExceptionCode.SYSTEM_ERROR.getCode();

    public BlogRuntimeException() {
    }

    public BlogRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public BlogRuntimeException(int errorCode, String message) {
        this(message, false);
        this.errorCode = errorCode;
    }

    private BlogRuntimeException(String message, boolean stackTrace) {
        super(message, null, true, stackTrace);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
