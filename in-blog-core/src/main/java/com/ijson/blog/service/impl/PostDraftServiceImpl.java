package com.ijson.blog.service.impl;

import com.ijson.blog.dao.PostDao;
import com.ijson.blog.dao.PostDraftDao;
import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.PostDraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2020/1/15 12:12 PM
 */
@Service
public class PostDraftServiceImpl implements PostDraftService {

    @Autowired
    private PostDraftDao postDraftDao;

    @Autowired
    private PostDao postDao;

    @Override
    public PostDraftEntity createPostDraft(AuthContext context, PostDraftEntity entity) {
        PostDraftEntity orUpdate = postDraftDao.createOrUpdate(entity);
        //如果不为空,添加数据信息
        if (Objects.nonNull(orUpdate)) {
            PostEntity postEntity = postDao.find(orUpdate.getId());
            if(Objects.nonNull(postEntity)){
                postEntity.setDraftId(orUpdate.getId());
                postDao.createOrUpdate(postEntity);
            }
        }
        return orUpdate;
    }

    @Override
    public PostDraftEntity find(String id) {
        return postDraftDao.find(id);
    }

    @Override
    public PostDraftEntity find(String ename, String shamId) {
        return postDraftDao.findByShamIdInternal(ename, shamId);
    }
}
