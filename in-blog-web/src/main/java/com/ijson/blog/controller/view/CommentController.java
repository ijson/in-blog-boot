package com.ijson.blog.controller.view;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.ReplyEntity;
import com.ijson.blog.dao.query.ReplyQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.CommentService;
import com.ijson.blog.service.ReplyService;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.info.CommentInfo;
import com.ijson.blog.service.model.info.ReplyInfo;
import com.ijson.blog.service.model.result.ReplyResult;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

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

    private String replyCodeKey = "replyCode";
    private String replyCodeTime = "replyCodeTime";

    /**
     * 查询列表
     *
     * @param ename
     * @param shamId
     * @param index
     * @return
     * @throws Exception
     */
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

        Function<Set<String>, Map<String, String>> userNames = userIds -> userService.findCnameByIds(userIds);
        List<ReplyInfo> replies = ReplyInfo.transform(result, userNames);
        return new ReplyResult(replies, new Pageable(((Long) result.getTotal()).intValue(), index));
    }


    /**
     * 保存文章评论
     *
     * @param ename
     * @param shamId
     * @param reply
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
        commentService.create(context, CommentInfo.formCommentEntity(commentInfo, request, context));
        return Result.ok("评论保存成功");
    }


    @RequestMapping("/{ename}/{shamId}/delete/{id}")
    @ResponseBody
    public ReplyResult delete(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, @PathVariable("id") String id, HttpSession session, HttpServletRequest request) throws Exception {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("删除评论时,未获取到用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        ReplyEntity entity = replyService.find(id);
        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.REPLY_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        if (!entity.getUserId().equals(context.getId())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.YOU_ARE_NOT_AUTHORIZED_TO_DELETE_THE_CURRENT_COMMENT);
        }

        replyService.delete(id);

        return getReplyList(ename, shamId, 0);
    }


    @RequestMapping("/{ename}/{shamId}/resave")
    @ResponseBody
    public ReplyInfo resave(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, @RequestBody ReplyInfo reply, HttpSession session, HttpServletRequest request) throws Exception {
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
