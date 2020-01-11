package com.ijson.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * desc: 文档注释
 * version: 6.7
 * Created by cuiyongxu on 2019/9/8 9:04 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface DocDocument {

    String name() default "";

    String desc() default "";

    boolean required() default false;
}
