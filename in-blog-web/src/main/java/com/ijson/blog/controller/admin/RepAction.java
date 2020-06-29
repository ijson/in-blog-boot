package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.ReplyEntity;
import com.ijson.blog.dao.query.ReplyQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.info.Rep;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/29 5:26 PM
 */
@Slf4j
@RestController
@RequestMapping("/rep")
public class RepAction extends BaseController {


    @RequestMapping("/all/list")
    @ResponseBody
    public Rep.Result list(Integer page, Integer limit, HttpServletRequest request) {

        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        if (Objects.isNull(context)) {
            return new Rep.Result(0, "为获取到权限信息");
        }

        String keyWord = request.getParameter("title");
        String replyByUserId = request.getParameter("replyByUserId");

        Page pageEntity = new Page();
        if (Objects.nonNull(page)) {
            pageEntity.setPageNumber(page);
        }
        if (Objects.nonNull(limit)) {
            pageEntity.setPageSize(limit);
        }


        ReplyQuery query = new ReplyQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setContent(keyWord);
        }
        if (!Strings.isNullOrEmpty(replyByUserId)) {
            query.setUserId(replyByUserId);
        }

        PageResult<ReplyEntity> result = replyService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new Rep.Result(0, "无消息通知");
        }

        List<ReplyEntity> dataList = result.getDataList();
        List<Rep.RepInfo> infos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            infos = Rep.RepInfo.createList(dataList);
        }

        Rep.Result repResult = new Rep.Result();
        repResult.setCode(0);
        repResult.setCount(result.getTotal());
        repResult.setData(infos);

        return repResult;
    }


}
