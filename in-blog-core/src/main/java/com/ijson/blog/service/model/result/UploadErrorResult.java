package com.ijson.blog.service.model.result;

import lombok.Data;

@Data
public class UploadErrorResult {
    private String message;
    private Integer number;

    public Integer getNumber() {
        return message.length();
    }
}