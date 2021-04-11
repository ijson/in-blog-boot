package com.ijson.blog.controller.view;

import com.google.common.base.Strings;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.info.IndexInstall;
import com.ijson.blog.service.model.info.PostInfo;
import com.ijson.blog.util.Pageable;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.ijson.rest.proxy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/10 11:23 PM
 */
@Slf4j
@Controller
public class HomeController extends BaseController {

    @RequestMapping("/")
    public ModelAndView root(HttpServletRequest request) {
        String keyWord = request.getParameter("keyWord");
        String pageNumber = request.getParameter("index");
        return index(request, pageNumber, keyWord);
    }

    @RequestMapping("/install")
    public ModelAndView install(HttpServletRequest request) {
        //查询系统管理员id
        UserEntity adminUser = userService.findInternalById("5d596de34737fb1c7ad99d3f");
        if (Objects.isNull(adminUser)) {
            return new ModelAndView(getViewTheme() + "/index-install.html");
        }
        return root(request);
    }


    @RequestMapping("/install/push")
    @ResponseBody
    public Result install(HttpServletRequest request, HttpSession session, @RequestBody IndexInstall install) {
        log.info(JsonUtil.toJson(install));
        UserEntity adminUser = userService.findInternalById("5d596de34737fb1c7ad99d3f");
        if (Objects.nonNull(adminUser)) {
            return Result.error("系统已成功安装,不允许再次执行安装操作!");
        }
        return Result.ok("安装成功,即将跳转到首页!");
    }


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, String index, String keyWord) {
        return getView(request, index, keyWord, null, "/");
    }


    @RequestMapping("/activity")
    public ModelAndView activity(HttpServletRequest request, String index, String keyWord) {
        return getView(request, index, keyWord, "activity", "/activity");
    }

    @RequestMapping("/software/share")
    public ModelAndView software(HttpServletRequest request, String index, String keyWord) {
        return getView(request, index, keyWord, "software", "/software/share");
    }

    @RequestMapping("/books")
    public ModelAndView books(HttpServletRequest request, String index, String keyWord) {
        ModelAndView view = new ModelAndView(getViewTheme() + "/index-books.html");
        view.addObject("path", "/books");
        addViewModelAndView(request, view);
        return view;
    }

    @RequestMapping("/menu/{item}")
    public ModelAndView software(HttpServletRequest request, @PathVariable("item") String item, String index, String keyWord) {
        ModelAndView view = new ModelAndView(getViewTheme() + "/index-menu-item.html");

        Page page = new Page();
        page.setPageNumber(getPageNumber(index));

        PostQuery query = new PostQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setLikeTitle(true);
            query.setTitle(keyWord);
        }
        if (!Strings.isNullOrEmpty(item)) {
            query.setIndexMenuEname(item);
        }
        query.setEnable(true);
        query.setStatus(Constant.PostStatus.pass);
        PageResult<PostEntity> result = postService.find(null, query, page);

        view.addObject("total", result.getTotal());
        view.addObject("page", new Pageable(((Long) result.getTotal()).intValue(), page.getPageNumber()));
        view.addObject("dataList", PostInfo.indexPost(result, keyWord));
        view.addObject("path", "/menu/" + item);
        view.addObject("keyWord", keyWord);
        addViewModelAndView(request, view);
        return view;
    }


    private int getPageNumber(String pageNumber) {
        try {
            int index = 1;
            if (!Strings.isNullOrEmpty(pageNumber)) {
                boolean number = NumberUtils.isNumber(pageNumber);
                if (number) {
                    index = Integer.parseInt(pageNumber);
                }
            }
            if (index <= 0) {
                index = 1;
            }
            return index;
        } catch (Exception e) {
            log.warn("getPageNumber error", e);
            return 1;
        }
    }

    private ModelAndView getView(HttpServletRequest request, String index, String keyWord, String indexMenuEname, String path) {
        ModelAndView view = new ModelAndView(getViewTheme() + "/index-list.html");

        Page page = new Page();
        page.setPageNumber(getPageNumber(index));

        PostQuery query = new PostQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setLikeTitle(true);
            query.setTitle(keyWord);
        }
        if (!Strings.isNullOrEmpty(indexMenuEname)) {
            query.setIndexMenuEname(indexMenuEname);
        }
        query.setEnable(true);
        query.setStatus(Constant.PostStatus.pass);
        PageResult<PostEntity> result = postService.find(null, query, page);

        view.addObject("total", result.getTotal());
        view.addObject("page", new Pageable(((Long) result.getTotal()).intValue(), page.getPageNumber()));
        view.addObject("dataList", PostInfo.indexPost(result, keyWord));
        view.addObject("path", path);
        view.addObject("keyWord", keyWord);
        addViewModelAndView(request, view);
        return view;
    }

}
