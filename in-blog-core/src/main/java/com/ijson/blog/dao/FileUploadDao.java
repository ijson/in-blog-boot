package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.FileUploadEntity;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/9/1 12:33 AM
 */
public interface FileUploadDao {


    FileUploadEntity createOrUpdate(FileUploadEntity entity);

    FileUploadEntity findDataByMd5(String md5);

}
