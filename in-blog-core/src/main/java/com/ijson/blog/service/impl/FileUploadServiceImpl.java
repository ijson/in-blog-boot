package com.ijson.blog.service.impl;

import com.ijson.blog.dao.FileUploadDao;
import com.ijson.blog.dao.entity.FileUploadEntity;
import com.ijson.blog.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/9/1 12:42 AM
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileUploadDao fileUploadDao;


    @Override
    public FileUploadEntity create(FileUploadEntity entity) {
        return fileUploadDao.createOrUpdate(entity);
    }

    @Override
    public FileUploadEntity findDataByMd5(String md5) {
        return fileUploadDao.findDataByMd5(md5);
    }
}
