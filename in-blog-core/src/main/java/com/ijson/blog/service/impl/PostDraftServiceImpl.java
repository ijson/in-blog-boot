package com.ijson.blog.service.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.PostDao;
import com.ijson.blog.dao.PostDraftDao;
import com.ijson.blog.dao.UserDao;
import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.PostDraftService;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private UserDao userDao;

    @Override
    public PostDraftEntity createPostDraft(AuthContext context, PostDraftEntity entity) {
        PostDraftEntity orUpdate = postDraftDao.createOrUpdate(entity);
        //如果不为空,添加数据信息
        if (Objects.nonNull(orUpdate)) {
            PostEntity postEntity = postDao.find(orUpdate.getId());
            if (Objects.nonNull(postEntity)) {
                postEntity.setDraftId(orUpdate.getId());
                postDao.createOrUpdate(postEntity, false);
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

    @Override
    public void delete(String id) {
        //需要将文章上的草稿id删除掉
        PostEntity postEntity = postDao.find(id);
        if (Objects.nonNull(postEntity)) {
            postEntity.setDraftId("");
            postDao.createOrUpdate(postEntity, false);
        }
        postDraftDao.removeDraft(id);
    }


    @Override
    public PageResult<PostDraftEntity> find(AuthContext context,PostQuery iquery, Page page) {
        PageResult<PostDraftEntity> postEntityPageResult = postDraftDao.find(iquery, page,context.getId());

        List<PostDraftEntity> dataList = postEntityPageResult.getDataList();

        Set<String> userIds = dataList.stream().map(PostDraftEntity::getUserId).collect(Collectors.toSet());

        Map<String, String> userIdOrCname = userDao.batchCnameByIds(userIds);

        List<PostDraftEntity> lastEntity = dataList.stream()
                .peek(key -> {
                    String cname = userIdOrCname.get(key.getUserId());
                    key.setCname(Strings.isNullOrEmpty(cname) ? Constant.UnknownUser : cname);
                }).collect(Collectors.toList());

        postEntityPageResult.setDataList(lastEntity);
        return postEntityPageResult;
    }
}
