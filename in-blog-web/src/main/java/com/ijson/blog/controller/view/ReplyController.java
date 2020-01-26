package com.ijson.blog.controller.view;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.ReplyEntity;
import com.ijson.blog.dao.query.ReplyQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.info.ReplyInfo;
import com.ijson.blog.service.model.result.ReplyResult;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.ReplyService;
import com.ijson.blog.util.Pageable;
import com.ijson.blog.util.VerifyCodeUtils;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/25 1:58 AM
 */
@Slf4j
@Controller
@RequestMapping("/reply")
public class ReplyController extends BaseController {


    @Autowired
    private ReplyService replyService;

    private String replyCodeKey = "replyCode";
    private String replyCodeTime = "replyCodeTime";

    @PostMapping("/{ename}/replys/{shamId}")
    @ResponseBody
    public ReplyResult getReplyList(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, Integer index) throws Exception {


        Page page = new Page();
        if (Objects.isNull(index)) {
            index = 1;
        }
        page.setPageNumber(index);

        ReplyQuery replyQuery = new ReplyQuery();
        replyQuery.setEname(ename);
        replyQuery.setShamId(shamId);

        PageResult<ReplyEntity> result = replyService.find(replyQuery, page);
        List<ReplyInfo> replies = result.getDataList().stream().map(ReplyInfo::formReply).collect(Collectors.toList());

        return new ReplyResult(replies, new Pageable(((Long) result.getTotal()).intValue(), index));
    }


    @RequestMapping("/{ename}/{shamId}/save")
    @ResponseBody
    public ReplyInfo getReplyList(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, @RequestBody ReplyInfo reply, HttpSession session, HttpServletRequest request) throws Exception {

        String verCode = (String) session.getAttribute(replyCodeKey);
        Result result = VerifyCodeUtils.validImage(reply.getReplyCode(), verCode, request, session, replyCodeKey, replyCodeTime);
        if (result.getCode() != 0) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.CAPTCHA_ERROR_OR_NOT_PRESENT, result.getMessage());
        }

        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("创建评论时,未获取到用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        return ReplyInfo.formReply(replyService.save(reply.getContent(), shamId, ename, ReplyInfo.formReplyEntity(reply, request, context)));
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void authImage(HttpServletResponse response, HttpSession session) throws IOException {
        generateVerification(response, session, replyCodeKey, replyCodeTime);
    }

}
