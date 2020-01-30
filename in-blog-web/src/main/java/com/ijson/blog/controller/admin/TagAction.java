package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.dao.query.TopicQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.V2Result;
import com.ijson.blog.service.model.info.TopicInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:32 PM
 */
@Slf4j
@RestController
@RequestMapping("/tag")
public class TagAction extends BaseController {


    @PostMapping(value = "/addup")
    public Result editBase(HttpServletRequest request, @RequestBody TopicEntity myEntity) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);


        if (Strings.isNullOrEmpty(myEntity.getTopicName())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.LABEL_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myEntity.getId())) {
            return create(context, myEntity);
        }

        TopicEntity entity = topicService.find(myEntity.getId());
        entity.setTopicName(myEntity.getTopicName());
        topicService.edit(context, entity);
        return Result.ok("更新成功!");
    }

    private Result create(AuthContext context, TopicEntity myEntity) {
        topicService.create(context, myEntity);
        return Result.ok("创建成功!");
    }

    @PostMapping(value = "/enable/{id}")
    public Result enable(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        TopicEntity entity = topicService.find(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.TAGS_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }
        topicService.enable(context, id, !entity.isEnable());
        return Result.ok(entity.isEnable() ? "禁用成功!" : "启用成功!");
    }


    @PostMapping(value = "/delete/{id}")
    public Result delete(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        TopicEntity entity = topicService.find(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.TAGS_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        if (entity.isEnable()) {
            log.info("启用状态无法删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ENABLED_STATE_CANNOT_BE_DELETED);
        }

        topicService.delete(id);
        return Result.ok("删除成功!");
    }


    @RequestMapping("/list")
    @ResponseBody
    public V2Result<TopicInfo> list(Integer page, Integer limit, HttpServletRequest request) {

        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        if (Objects.isNull(context)) {
            return new V2Result<>();
        }

        String keyWord = request.getParameter("title");

        Page pageEntity = new Page();
        if (Objects.nonNull(page)) {
            pageEntity.setPageNumber(page);
        }
        if (Objects.nonNull(limit)) {
            pageEntity.setPageSize(limit);
        }


        TopicQuery query = new TopicQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setTopicName(keyWord);
        }

        PageResult<TopicEntity> result = topicService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<TopicEntity> dataList = result.getDataList();
        List<TopicInfo> topicInfos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            topicInfos = TopicInfo.createList(dataList);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(topicInfos);
        v2Result.setMsg("");
        return v2Result;
    }
}