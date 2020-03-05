package com.ijson.blog.controller.view;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.info.Comment;
import com.ijson.blog.util.Pageable;
import com.ijson.blog.util.VerifyCodeUtils;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/3/5 8:42 PM
 */
@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {


    private String commentCodeKey = "commentCodeKey";
    private String commentCodeTime = "commentCodeTime";


    @RequestMapping("/save")
    @ResponseBody
    public Comment.CommentResult save(@RequestBody Comment.Arg arg,
                                      HttpSession session,
                                      HttpServletRequest request) {

        String verCode = (String) session.getAttribute(commentCodeKey);
        Result result = VerifyCodeUtils.validImage(arg.getReplyCode(), verCode, request, session, commentCodeKey, commentCodeTime);
        if (result.getCode() != 0) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.CAPTCHA_ERROR_OR_NOT_PRESENT, result.getMessage());
        }

        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("创建评论时,未获取到用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        CommentEntity entity = arg.getCreateEntity(context, postService, userService, request);

        entity = commentService.create(context, entity);

        return Comment.CommentResult.create(entity.getId(),
                entity.getFromAvatar(),
                entity.getFromCname(),
                entity.getContent(),
                entity.getCreateTime(),
                entity.getHost(),
                entity.getOs(),
                entity.getBrowse());
    }


    @PostMapping("/list")
    @ResponseBody
    public Comment.ListResult getReplyList(@RequestBody Comment.ListArg arg) {


        Page page = new Page();
        if (Objects.isNull(arg.getIndex())) {
            page.setPageNumber(1);
        }


        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setEname(arg.getEname());
        commentQuery.setShamId(arg.getShamId());


        PageResult<CommentEntity> pageResult = commentService.find(commentQuery, page);

        //Function<Set<String>, Map<String, String>> userNames = userIds -> userService.findCnameByIds(userIds);

        List<Comment.CommentResult> result = Comment.CommentResult.transform(pageResult);

        return new Comment.ListResult(result, new Pageable(((Long) pageResult.getTotal()).intValue(), arg.getIndex()));
    }


    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void getCommentCode(HttpServletResponse response, HttpSession session) throws IOException {
        generateVerification(response, session, commentCodeKey, commentCodeTime);
    }


}
