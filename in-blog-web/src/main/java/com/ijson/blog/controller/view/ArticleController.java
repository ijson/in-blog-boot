package com.ijson.blog.controller.view;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.exception.BlogNotFoundException;
import com.ijson.blog.service.model.info.PostInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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


    /**
     * @param ename
     * @param shamId
     * @return
     */
    @RequestMapping("/{ename}/details/{shamId}")
    public ModelAndView details(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId) {
        ModelAndView view = new ModelAndView("view/index-article.html");
        try {
            PostEntity entity = postService.findByShamId(ename, shamId);
            view.addObject("data", PostInfo.create(entity));
            addViewModelAndView(view);
            return view;
        } catch (BlogNotFoundException e) {
            view.setViewName("error/404.html");
            return view;
        }
    }
}
