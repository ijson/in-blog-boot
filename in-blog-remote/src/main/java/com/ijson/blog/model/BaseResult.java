package com.ijson.blog.model;

import lombok.Data;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 7:53 PM
 */
@Data
public class BaseResult<T> {

    protected int code;//错误码0 || 200是正常的
    protected String message;
    protected T data;
}
