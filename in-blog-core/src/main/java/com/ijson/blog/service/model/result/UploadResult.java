package com.ijson.blog.service.model.result;

import com.google.common.base.Strings;
import lombok.Data;

@Data
public class UploadResult {
    private String fileName;
    private Integer uploaded = 1;
    private String url;
    private UploadErrorResult error;


    public static UploadResult create(String fileName, String url) {
        return create(fileName, url, null);
    }

    public static UploadResult create(String fileName, String url, String message) {
        UploadResult uploadResult = new UploadResult();
        uploadResult.setFileName(fileName);
        uploadResult.setUrl(url);
        if (!Strings.isNullOrEmpty(message)) {
            UploadErrorResult uploadErrorRst = new UploadErrorResult();
            uploadErrorRst.setMessage(message);
            uploadResult.setError(uploadErrorRst);
        }
        return uploadResult;
    }
}