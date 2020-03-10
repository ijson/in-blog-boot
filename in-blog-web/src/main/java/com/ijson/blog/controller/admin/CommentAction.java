package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.V2Result;
import com.ijson.blog.service.model.info.Comment;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/3/11 12:34 AM
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentAction extends BaseController {


    @RequestMapping("/post/list")
    @ResponseBody
    public V2Result<Comment.Info> list(Integer page, Integer limit, HttpServletRequest request) {

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


        CommentQuery query = new CommentQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setContent(keyWord);
        }


        query.setAuthor(context.getId());

        PageResult<CommentEntity> result = commentService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<CommentEntity> dataList = result.getDataList();
        List<Comment.Info> commentInfo = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            commentInfo = Comment.Info.createList(dataList);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(commentInfo);
        v2Result.setMsg("");
        return v2Result;
    }

}
