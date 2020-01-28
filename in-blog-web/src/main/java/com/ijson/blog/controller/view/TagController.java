package com.ijson.blog.controller.view;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.exception.BlogNotFoundException;
import com.ijson.blog.service.model.info.HotTopicInfo;
import com.ijson.blog.service.model.info.PostInfo;
import com.ijson.blog.util.Pageable;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 11:30 PM
 */
@Slf4j
@Controller
@RequestMapping("/tags")
public class TagController extends BaseController {

    @RequestMapping("/")
    public ModelAndView tags(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("view/index-list-tag.html");
        try {
            view.addObject("tags", HotTopicInfo.getHotTopic(topicService.findAll()));
            view.addObject("tagActive", "active");
            addViewModelAndView(view);
            return view;
        } catch (BlogNotFoundException e) {
            view.setViewName("error/404.html");
            return view;
        }
    }


    @RequestMapping("/{id}")
    public ModelAndView tagById(@PathVariable("id") String id, Integer index, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("view/index-list-post-tag.html");
        try {
            Page page = new Page();

            if (Objects.isNull(index)) {
                index = 1;
            }
            page.setPageNumber(index);

            PageResult<PostEntity> result = postService.findPostByTagId(id, page);

            //view.addObject("tags", HotTopic.getHotTopic(topicService.findUseAll()));
            view.addObject("tagActive", "active");
            view.addObject("tagPostCount", result.getTotal());
            view.addObject("tagPost", PostInfo.indexPost(result));
            view.addObject("id", id);
            TopicEntity topicEntity = topicService.find(id);
            if (Objects.nonNull(topicEntity)) {
                view.addObject("tagName", topicEntity.getTopicName());
            }
            view.addObject("page", new Pageable(((Long) result.getTotal()).intValue(), index));
            addViewModelAndView(view);
            return view;
        } catch (BlogNotFoundException e) {
            view.setViewName("error/404.html");
            return view;
        }
    }


    @RequestMapping("/{ename}/tags/{shamId}")
    public ModelAndView tagByShamId(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, Integer index, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("view/index-list-post-tag.html");
        TopicEntity topicEntity = topicService.findTopicByShamIdAndEname(ename, shamId);
        if (Objects.isNull(topicEntity)) {
            view.setViewName("error/404.html");
            return view;
        }

        String id = topicEntity.getId();
        try {
            Page page = new Page();

            if (Objects.isNull(index)) {
                index = 1;
            }
            page.setPageNumber(index);

            PageResult<PostEntity> result = postService.findPostByTagId(id, page);

            //view.addObject("tags", HotTopic.getHotTopic(topicService.findUseAll()));
            view.addObject("tagActive", "active");
            view.addObject("tagPostCount", result.getTotal());
            view.addObject("tagPost", PostInfo.indexPost(result));
            view.addObject("id", id);
            view.addObject("tagName", topicEntity.getTopicName());
            view.addObject("page", new Pageable(((Long) result.getTotal()).intValue(), index));
            addViewModelAndView(view);
            return view;
        } catch (BlogNotFoundException e) {
            view.setViewName("error/404.html");
            return view;
        }
    }
}
