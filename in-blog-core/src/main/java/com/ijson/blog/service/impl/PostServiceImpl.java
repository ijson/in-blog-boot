package com.ijson.blog.service.impl;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.*;
import com.ijson.blog.dao.entity.*;
import com.ijson.blog.dao.model.AccessType;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogNotFoundException;
import com.ijson.blog.manager.ViewSyncManager;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.PostService;
import com.ijson.blog.service.model.info.WelcomeInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/21 2:28 PM
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private CountDao countDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ViewSyncManager viewSyncManager;

    @Autowired
    private PostDraftDao draftDao;

    @Override
    public PostEntity createPost(AuthContext context, PostEntity entity) {
        //调用此接口 要将草稿置空
        entity.setDraftId("");
        String draftId = entity.getId();
        //如果是草稿转博文,需要将id删除
        if (entity.isCreate()) {
            entity.setId("");
        }
        if (context.isVerify()) {
            entity.setStatus(Constant.PostStatus.in_progress);
        } else {
            entity.setStatus(Constant.PostStatus.pass);
        }
        PostEntity rst = postDao.createOrUpdate(entity, true);
        //创建博文,删除草稿
        if (Objects.nonNull(rst)) {
            if (Strings.isNullOrEmpty(draftId)) {
                draftId = rst.getDraftId();
            }
            PostDraftEntity postDraft = draftDao.find(draftId);
            if (Objects.nonNull(postDraft)) {
                draftDao.removeDraft(draftId);
            }
        }

        return rst;
    }

    @Override
    public PageResult<PostEntity> find(AuthContext context, PostQuery iquery, Page page) {
        String authorId = "";
        if (Objects.nonNull(context)) {
            authorId = context.getId();
        }

        PageResult<PostEntity> postEntityPageResult = postDao.find(iquery, page, authorId);
        Set<String> ids = postEntityPageResult.getDataList().stream().map(PostEntity::getId).collect(Collectors.toSet());
        Map<String, Long> countByIds = countDao.findCountByIds(ids);

        Map<String, Long> replyByIds = replyDao.findCountByIds(ids);

        List<PostEntity> dataList = postEntityPageResult.getDataList();

        Set<String> userIds = dataList.stream().map(PostEntity::getUserId).collect(Collectors.toSet());

        Map<String, String> userIdOrCname = userDao.batchCnameByIds(userIds);

        List<PostEntity> lastEntity = dataList.stream()
                .peek(key -> {
                    Long views = countByIds.get(key.getId());
                    if (Objects.isNull(views)) {
                        views = 0L;
                    }
                    key.setViews(views);

                    Long reply = replyByIds.get(key.getId());
                    if (Objects.isNull(reply)) {
                        reply = 0L;
                    }
                    String cname = userIdOrCname.get(key.getUserId());
                    key.setCname(Strings.isNullOrEmpty(cname) ? Constant.UnknownUser : cname);
                    key.setReply(reply);
                }).collect(Collectors.toList());
        postEntityPageResult.setDataList(lastEntity);
        return postEntityPageResult;
    }

    @Cacheable(value = "hotPosts")
    @Override
    public List<PostEntity> findHotPostBeforeTen() {

        List<CountEntity> hotCount = countDao.findHot();
        List<String> hotIds = hotCount.stream().map(CountEntity::getId).collect(Collectors.toList());
        List<PostEntity> postEntities = postDao.findHotPostByIds(hotIds);
        if (CollectionUtils.isEmpty(postEntities)) {
            return Lists.newArrayList();
        }

        Map<String, Long> collect = hotCount.stream().collect(Collectors.toMap(CountEntity::getId, CountEntity::getViews));
        return postEntities.stream().map(post -> {
            post.setViews(collect.get(post.getId()));
            return post;
        }).collect(Collectors.toList());
    }

    @Override
    public PostEntity findById(String id) {
        PostEntity entity = postDao.find(id);
        if (Objects.isNull(entity)) {
            throw new BlogNotFoundException(BlogBusinessExceptionCode.BLOG_NOT_FOUND);
        }
        CountEntity countById = countDao.findCountById(id);
        if (Objects.nonNull(countById)) {
            entity.setViews(countById.getViews());
        }

        List<ReplyEntity> replyEntitys = replyDao.findCountById(id);
        if (CollectionUtils.isNotEmpty(replyEntitys)) {
            entity.setReply(replyEntitys.size());
        } else {
            entity.setReply(0);
        }

        List<TopicEntity> topicEntitys = topicDao.finds(entity.getTopicId());
        if (CollectionUtils.isNotEmpty(topicEntitys)) {
            entity.setTopicName(topicEntitys);
        }

        return entity;
    }


    @Override
    public PostEntity findInternalById(String id) {
        PostEntity entity = postDao.find(id);
        if (Objects.isNull(entity)) {
            return null;
        }
        CountEntity countById = countDao.findCountById(id);
        if (Objects.nonNull(countById)) {
            entity.setViews(countById.getViews());
        }

        List<ReplyEntity> replyEntitys = replyDao.findCountById(id);
        if (CollectionUtils.isNotEmpty(replyEntitys)) {
            entity.setReply(replyEntitys.size());
        } else {
            entity.setReply(0);
        }

        List<TopicEntity> topicEntitys = topicDao.finds(entity.getTopicId());
        if (CollectionUtils.isNotEmpty(topicEntitys)) {
            entity.setTopicName(topicEntitys);
        }

        return entity;
    }

    @Override
    public PostEntity findByShamId(String ename, String shamId) {

        PostEntity entity = postDao.findByShamId(ename, shamId);
        if (Objects.isNull(entity)) {
            throw new BlogNotFoundException(BlogBusinessExceptionCode.BLOG_NOT_FOUND);
        }
        CountEntity countById = countDao.findCountById(entity.getId());
        if (Objects.nonNull(countById)) {
            entity.setViews(countById.getViews());
        }

        List<ReplyEntity> replyEntitys = replyDao.findCountById(entity.getId());
        if (CollectionUtils.isNotEmpty(replyEntitys)) {
            entity.setReply(replyEntitys.size());
        } else {
            entity.setReply(0);
        }

        List<TopicEntity> topicEntitys = topicDao.finds(entity.getTopicId());
        if (CollectionUtils.isNotEmpty(topicEntitys)) {
            entity.setTopicName(topicEntitys);
        }

        UserEntity userEntity = userDao.findById(entity.getUserId());
        if (Objects.nonNull(userEntity)) {
            entity.setCname(userEntity.getCname());
        }
        viewSyncManager.syncViewBlog(entity);

        return entity;
    }


    @Cacheable(value = "postCount")
    @Override
    public long count() {
        return postDao.count();
    }

    @Override
    public List<PostEntity> findRecentlyPublishedBeforeTen() {
        return postDao.getRecentlyPublished();
    }

    @Override
    public PageResult<PostEntity> findPostByTagId(String id, Page page) {

        return postDao.findPostByTagId(id, page);
    }

    @Override
    public PostEntity incPros(String id) {
        return postDao.incPros(id);
    }

    @Override
    public long getWebSiteCount(AuthContext context) {
        CountEntity countById = countDao.findCountByWebType(AccessType.webSite.name());
        return countById.getViews();
    }

    @Cacheable(value = "consoleData")
    @Override
    public WelcomeInfo getConsoleData() {
        Long publishTotal = postDao.findPublishTotal(null);
        Long readTotal = getWebSiteCount(null);
        Long commentTotal = replyDao.count();
        Long todayPublishTotal = postDao.findTodayPublishTotal(null);
        Long userCount = userDao.count();
        return WelcomeInfo.create(publishTotal, readTotal, commentTotal, todayPublishTotal, userCount);
    }

    @Override
    public WelcomeInfo getUserConsoleData(AuthContext context) {
        Long publishTotal = postDao.findPublishTotal(context);
        Long todayPublishTotal = postDao.findTodayPublishTotal(context);
        return WelcomeInfo.create(publishTotal, 0L, 0L, todayPublishTotal, 0L);
    }

    @Override
    public PostEntity enable(String id, boolean enable, AuthContext context) {
        return postDao.enable(id, enable, context.getId());
    }

    @Override
    public PostEntity delete(String id, AuthContext context) {
        //return postDao.delete(id, context.getId());
        postDao.delete(id);
        draftDao.removeDraft(id);
        return null;
    }

    @Override
    public PostEntity delete(PostEntity entity, AuthContext context) {
        String postId = entity.getId();
        postDao.delete(postId);
        draftDao.removeDraft(postId);
        List<String> topicIds = entity.getTopicId();
        if(CollectionUtils.isNotEmpty(topicIds)){
            //遍历所有tags,如果postCount-1 <=0,则需要将tag删除
            topicIds.forEach(topicId -> {
                TopicEntity topic = topicDao.find(topicId);
                if(Objects.nonNull(topic)){
                    long lastCount = topic.getPostCount() - 1;
                    if (lastCount <= 0) {
                        topicDao.delete(topic.getId());
                    } else {
                        topicDao.subtract(topic);
                    }
                }
            });
        }
        return null;
    }

    @Override
    public List<String> removeTopic(PostEntity postEntity, String id, String topicNames) {
        //库中该文章对应的topic
        List<TopicEntity> oldTopics = postEntity.getTopicName();
        //用户更新后的topic
        List<String> newTopics = Splitter.on(",").splitToList(topicNames);
        List<String> removeTopicId = Lists.newArrayList();
        for (TopicEntity entity : oldTopics) {
            //如果老的topic不再用户更新后的topic中
            if (!newTopics.contains(entity.getTopicName())) {
                removeTopicId.add(entity.getId());
            }
        }
        if (CollectionUtils.isEmpty(removeTopicId)) {
            return Lists.newArrayList();
        }

        topicDao.batchIncTopicCount(removeTopicId);
        return removeTopicId;
    }


    @Override
    public List<String> removeTopic(List<String> removeTopicId) {
        topicDao.batchIncTopicCount(removeTopicId);
        return removeTopicId;
    }

    @Override
    public PostEntity incPros(String ename, String shamId) {
        return postDao.incPros(ename, shamId);
    }

    @Override
    public PostEntity enable(String ename, String shamId, boolean enable, AuthContext context) {
        return postDao.enable(ename, shamId, enable, context.getId());
    }

    @Override
    public PostEntity findByShamIdInternal(String ename, String shamId, boolean includeTopicAncCount) {
        PostEntity entity = postDao.findByShamIdInternal(ename, shamId);

        if (includeTopicAncCount) {
            CountEntity countById = countDao.findCountById(entity.getId());
            if (Objects.nonNull(countById)) {
                entity.setViews(countById.getViews());
            }

            List<ReplyEntity> replyEntitys = replyDao.findCountById(entity.getId());
            if (CollectionUtils.isNotEmpty(replyEntitys)) {
                entity.setReply(replyEntitys.size());
            } else {
                entity.setReply(0);
            }

            List<TopicEntity> topicEntitys = topicDao.finds(entity.getTopicId());
            if (CollectionUtils.isNotEmpty(topicEntitys)) {
                entity.setTopicName(topicEntitys);
            }

            UserEntity userEntity = userDao.findById(entity.getUserId());
            if (Objects.nonNull(userEntity)) {
                entity.setCname(userEntity.getCname());
            }
        }

        return entity;
    }

    @Override
    public PostEntity findByDraftId(String id) {
        return postDao.findByDraftId(id);
    }

    @Override
    public PostEntity audit(String ename, String shamId, Constant.PostStatus status, String reason, AuthContext context) {
        return postDao.audit(ename, shamId, status, reason, context.getId());

    }
}
