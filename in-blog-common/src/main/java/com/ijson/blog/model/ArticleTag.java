package com.ijson.blog.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleTag implements Serializable {
    private String id;
    private Constant.PostStatus status;
}
