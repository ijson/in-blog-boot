package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.IndexMenuEntity;
import com.ijson.blog.dao.query.IndexMenuQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.V2Result;
import com.ijson.blog.service.model.info.HeaderInfo;
import com.ijson.blog.service.model.info.IndexMenuInfo;
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
@RequestMapping("/index/menu")
public class IndexMenuAction extends BaseController {


    @PostMapping(value = "/addup")
    public Result addOrUpdate(HttpServletRequest request, @RequestBody IndexMenuEntity myEntity) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        if (Strings.isNullOrEmpty(myEntity.getCname())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.THE_DESCRIPTION_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myEntity.getPath())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.PATH_SIGNS_CANNOT_BE_EMPTY);
        }

        if (Objects.isNull(myEntity.getOrder())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.ORDER_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myEntity.getId())) {
            return create(request, myEntity);
        }

        IndexMenuEntity entity = indexMenuService.findInternalById(myEntity.getId());

        entity.setCname(myEntity.getCname());
        entity.setPath(myEntity.getPath());
        entity.setOrder(myEntity.getOrder());
//        entity.setEname(myEntity.getEname());

        indexMenuService.edit(context, entity);
        return Result.ok("更新成功!");
    }

    private Result create(HttpServletRequest request, IndexMenuEntity myEntity) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        List<IndexMenuEntity> all = indexMenuService.findAll();
        if (all.size() > 6) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.HOME_MENU_SUPPORTS_MAX_OF_SIX_CUSTOM);
        }
        indexMenuService.create(context, myEntity);
        return Result.ok("创建成功!");
    }

    @PostMapping(value = "/enable/{id}")
    public Result enable(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        IndexMenuEntity entity = indexMenuService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.THE_HEADER_IS_EMPTY_OR_DOES_NOT_EXIST);
        }

        indexMenuService.enable(id, !entity.isEnable(), context);
        return Result.ok(entity.isEnable() ? "禁用成功!" : "启用成功!");
    }

    @PostMapping(value = "/delete/{id}")
    public Result delete(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        IndexMenuEntity entity = indexMenuService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.THE_HEADER_IS_EMPTY_OR_DOES_NOT_EXIST);
        }

        if (entity.isEnable()) {
            log.info("启用状态无法删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ENABLED_STATE_CANNOT_BE_DELETED);
        }

        indexMenuService.delete(id);
        return Result.ok("删除成功!");
    }


    @RequestMapping("/list")
    @ResponseBody
    public V2Result<HeaderInfo> list(Integer page, Integer limit, HttpServletRequest request) {

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


        IndexMenuQuery query = new IndexMenuQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setCname(keyWord);
        }

        PageResult<IndexMenuEntity> result = indexMenuService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<IndexMenuEntity> dataList = result.getDataList();
        List<IndexMenuInfo> infos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            infos = IndexMenuInfo.createList(dataList);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(infos);
        v2Result.setMsg("");
        return v2Result;
    }
}