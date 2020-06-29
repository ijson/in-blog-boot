package com.ijson.blog.controller.view;

import com.google.common.base.Strings;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.model.ReplyType;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.CommentService;
import com.ijson.blog.service.ReplyService;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.info.CommentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/25 1:58 AM
 */
@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {


    @Autowired
    private ReplyService replyService;

    @Autowired
    private CommentService commentService;

    private String commentCodeKey = "commentCode";
    private String commentCodeTime = "commentCodeTime";


    /**
     * 保存文章评论
     *
     * @param session
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public Result save(@RequestBody CommentInfo commentInfo, HttpSession session, HttpServletRequest request) throws Exception {

//        String verCode = (String) session.getAttribute(replyCodeKey);
//        Result result = VerifyCodeUtils.validImage(commentInfo.getReplyCode(), verCode, request, session, replyCodeKey, replyCodeTime);
//        if (result.getCode() != 0) {
//            throw new ReplyCreateException(BlogBusinessExceptionCode.CAPTCHA_ERROR_OR_NOT_PRESENT, result.getMessage());
//        }

        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("创建评论时,未获取到用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }

        if (Strings.isNullOrEmpty(commentInfo.getContent())) {
            log.info("评论内容为空");
            throw new ReplyCreateException(BlogBusinessExceptionCode.INFORMATION_IS_INCOMPLETE);
        }
        CommentEntity entity = commentService.create(context, CommentInfo.formCommentEntity(commentInfo, request, context));
        return Result.ok("评论保存成功", entity.getId());
    }


    @RequestMapping("/save/reply")
    @ResponseBody
    public Result saveReply(@RequestBody CommentInfo commentInfo, HttpSession session, HttpServletRequest request) throws Exception {

//        String verCode = (String) session.getAttribute(replyCodeKey);
//        Result result = VerifyCodeUtils.validImage(commentInfo.getReplyCode(), verCode, request, session, replyCodeKey, replyCodeTime);
//        if (result.getCode() != 0) {
//            throw new ReplyCreateException(BlogBusinessExceptionCode.CAPTCHA_ERROR_OR_NOT_PRESENT, result.getMessage());
//        }

        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("创建评论时,未获取到用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }

        if (Strings.isNullOrEmpty(commentInfo.getContent())) {
            log.info("评论内容为空");
            throw new ReplyCreateException(BlogBusinessExceptionCode.INFORMATION_IS_INCOMPLETE);
        }
        CommentEntity entity = commentService.create(context, CommentInfo.formReplyEntity(commentInfo, request, context));
        return Result.ok("回复保存成功", entity.getId());
    }


    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody CommentInfo commentInfo, HttpSession session, HttpServletRequest request) throws Exception {
        if (Strings.isNullOrEmpty(commentInfo.getId())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.INFORMATION_IS_INCOMPLETE);
        }
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("删除评论时,未获取到用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        CommentEntity entity = commentService.find(context, commentInfo.getId());
        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.REPLY_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        //只有自己能删除自己的
        if (!entity.getUserId().equals(context.getId())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.YOU_ARE_NOT_AUTHORIZED_TO_DELETE_THE_CURRENT_COMMENT);
        }

        //删除
        commentService.delete(context, commentInfo.getId());

        //如果是文章评论  删除则需要将子评论一同删除
        if (entity.getReplyType().equals(ReplyType.comment)) {
            commentService.deleteReplyByCommentId(context, commentInfo.getId());
        }

        String message = entity.getReplyType().equals(ReplyType.comment) ? "删除文章评论和回复内容成功" : "删除回复内容成功";
        return Result.ok(message);
    }


    @RequestMapping("/praise")
    @ResponseBody
    public Result praise(@RequestBody CommentInfo commentInfo, HttpSession session, HttpServletRequest request) throws Exception {
        if (Strings.isNullOrEmpty(commentInfo.getId())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.INFORMATION_IS_INCOMPLETE);
        }
//        AuthContext context = getContext(request);
//        if (Objects.isNull(context)) {
//            log.info("点赞未获取到用户信息");
//            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
//        }
        CommentEntity entity = commentService.find(null, commentInfo.getId());
        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.REPLY_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        commentService.praise(null, commentInfo.getId());
        return Result.ok("点赞成功");
    }


    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void authImage(HttpServletResponse response, HttpSession session) throws IOException {
        generateVerification(response, session, commentCodeKey, commentCodeTime);
    }
}
