package com.ijson.blog.service;

import com.ijson.blog.dao.entity.FileUploadEntity;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/9/1 12:42 AM
 */
public interface FileUploadService {

    FileUploadEntity create(FileUploadEntity entity);

    FileUploadEntity findDataByMd5(String md5);
}
