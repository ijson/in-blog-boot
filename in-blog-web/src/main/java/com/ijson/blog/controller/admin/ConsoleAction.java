package com.ijson.blog.controller.admin;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.model.SystemInfo;
import com.ijson.blog.service.PostDraftService;
import com.ijson.blog.service.PostService;
import com.ijson.blog.service.UserService;
import com.ijson.blog.service.WebSiteService;
import com.ijson.blog.service.model.ConsoleData;
import com.ijson.blog.service.model.Post;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * https://www.bootcdn.cn/
 * desc: 用户控制台
 * version: 6.7
 * Created by cuiyongxu on 2019/7/27 12:28 AM
 */
@Controller
@RequestMapping("/admin")
public class ConsoleAction extends BaseController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostDraftService postDraftService;

    @Autowired
    private WebSiteService webSiteService;


    @RequestMapping("/v2/console/page")
    public ModelAndView indexv2() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/index.html");
        addAdminModelAndView(view);
        return view;
    }

    @RequestMapping("/welcome/page")
    public ModelAndView welcome() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/welcome.html");
        addAdminModelAndView(view);
        view.addObject("systemInfo", SystemInfo.getSystemInfo());
        ConsoleData consoleData = postService.getConsoleData();
        view.addObject("consoleData", consoleData);
        return view;
    }

    @RequestMapping("/v2/article/add/page")
    public ModelAndView articleAdd() {
        ModelAndView view = new ModelAndView();
        addAdminModelAndView(view);
        view.setViewName("admin/save-article.html");
        return view;
    }

    @RequestMapping("/v2/post/list/page")
    public ModelAndView postV2List() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/article-list.html");
        addAdminModelAndView(view);
        return view;
    }


    @RequestMapping("/v2/draft/list/page")
    public ModelAndView postDriftV2List() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/article-draft-list.html");
        addAdminModelAndView(view);
        return view;
    }


    /**
     * 跳转到编辑博客页面
     *
     * @return
     */
    @RequestMapping("/edit/v2/{ename}/{shamId}/page")
    public ModelAndView skipV2Edit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId) {
        PostEntity entity = postService.findByShamIdInternal(ename, shamId, true);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-article.html");
        addAdminModelAndView(view);
        view.addObject("editData", Post.create(entity));
        view.addObject("topic", Post.create(entity).getTopicName());
        return view;
    }


    @RequestMapping("/draft/v2/edit/{ename}/{shamId}/page")
    public ModelAndView skipV2PostDriftEdit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId) {
        PostDraftEntity entity = postDraftService.find(ename, shamId);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-article.html");
        addAdminModelAndView(view);
        if (Objects.nonNull(entity)) {
            view.addObject("editData", Post.create(entity));
            view.addObject("topic", Post.create(entity).getTopicName());
        }
        return view;
    }


    @RequestMapping("/site/settings")
    public ModelAndView siteSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/site-settings.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }

        view.addObject("site", getConfig());
        addAdminModelAndView(view);
        return view;
    }

    @RequestMapping("/user/settings")
    public ModelAndView userSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/user-settings.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view);
        view.addObject("user", userService.findUserByEname(context.getEname(), null, null));
        return view;
    }


    @RequestMapping("/user/list/page")
    public ModelAndView userList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/user-list.html");
        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view);
        return view;
    }


    @RequestMapping("/user/delete/list/page")
    public ModelAndView userDelList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/user-del-list.html");
        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view);
        return view;
    }

}
