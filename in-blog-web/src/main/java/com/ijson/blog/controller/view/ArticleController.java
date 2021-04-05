package com.ijson.blog.controller.view;

import com.ijson.blog.bus.IEventBus;
import com.ijson.blog.bus.event.CreateTagEvent;
import com.ijson.blog.bus.event.ViewArticleEvent;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.model.ReplyType;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.exception.BlogNotFoundException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.CommentService;
import com.ijson.blog.service.model.info.CommentInfo;
import com.ijson.blog.service.model.info.PostInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/10 11:19 PM
 */
@Slf4j
@Controller()
@RequestMapping("/article")
public class ArticleController extends BaseController {
    //http://localhost:8876/article/cuiyongxu/details/1575744843

    @Autowired
    private CommentService commentService;

    /**
     * @param ename
     * @param shamId
     * @return
     */
    @RequestMapping("/{ename}/details/{shamId}")
    public ModelAndView details(HttpServletRequest request, @PathVariable("ename") String ename, @PathVariable("shamId") String shamId) {
        ModelAndView view = new ModelAndView(getViewTheme() + "/index-article.html");
        try {
            PostEntity entity = postService.findByShamId(ename, shamId);
            if (entity == null || entity.getStatus() != Constant.PostStatus.pass) {
                view.setViewName("error/404.html");
                return view;
            }
            view.addObject("data", PostInfo.create(entity));
            view.addObject("path", "/");
            //获取所有的文章评论
            PageResult<CommentInfo> comments = commentService.find(CommentQuery.builder().ename(ename).shamId(shamId).build(), new Page(1000, 1, null, false));

            view.addObject("replys", comments.getDataList());
            addViewModelAndView(request, view);

            IEventBus.post(ViewArticleEvent.view(AuthContext.systemAuthContext(), entity.getId()));
            return view;
        } catch (BlogNotFoundException e) {
            view.setViewName("error/404.html");
            return view;
        }
    }
}
