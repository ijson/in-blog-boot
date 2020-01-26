package com.ijson.blog.controller.admin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.controller.admin.model.AuthKey;
import com.ijson.blog.dao.entity.*;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.model.SystemInfo;
import com.ijson.blog.service.model.*;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * https://www.bootcdn.cn/
 * desc: 用户控制台
 * version: 6.7
 * Created by cuiyongxu on 2019/7/27 12:28 AM
 */
@Controller
@RequestMapping("/admin")
public class ConsoleAction extends BaseController {

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

    @RequestMapping("/edit/user/{id}/page")
    public ModelAndView skipUserEdit(@PathVariable("id") String id) {
        UserEntity internalById = userService.findInternalById(id);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-user.html");
        addAdminModelAndView(view);
        view.addObject("editData", UserInfo.create(internalById));
        return view;
    }


    @RequestMapping("/add/user")
    public ModelAndView skipUserAdd() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-user.html");
        addAdminModelAndView(view);
        view.addObject("editData", null);
        return view;
    }

    @RequestMapping("/add/blogroll")
    public ModelAndView skipBlogrollAdd() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-blogroll.html");
        addAdminModelAndView(view);
        view.addObject("editData", null);
        return view;
    }


    @RequestMapping("/add/auth")
    public ModelAndView skipAuthAdd() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-auth.html");
        addAdminModelAndView(view);
        List<AuthEntity> fathers = authService.findFathers("0");
        view.addObject("editData", null);
        view.addObject("fathers", AuthInfo.createAuthList(fathers));

        return view;
    }

    @RequestMapping("/add/role")
    public ModelAndView skipRoleAdd() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-role.html");
        addAdminModelAndView(view);
        List<AuthEntity> allAuth = authService.findAll();
        if (CollectionUtils.isEmpty(allAuth)) {
            view.addObject("auths", Maps.newHashMap());
        } else {
            view.addObject("auths", getAuthMap(allAuth, Lists.newArrayList()));
        }
        view.addObject("editData", null);
        return view;
    }

    public Map<AuthKey, List<AuthInfo>> getAuthMap(List<AuthEntity> allAuth, List<String> authIds) {
        List<AuthEntity> fatherEntity = allAuth.stream().filter(k -> {
            return k.getFatherId().equals("0");
        }).collect(Collectors.toList());

        return fatherEntity.stream().collect(Collectors.toMap(key -> {
            return new AuthKey(key.getId(), key.getEname(), key.getCname(), key.getPath(), authIds.contains(key.getId()));
        }, value -> {
            return allAuth.stream().filter(vs -> {
                return vs.getFatherId().equals(value.getId());
            }).collect(Collectors.toList()).stream().map(k -> {
                return AuthInfo.create(k, authIds.contains(k.getId()));
            }).collect(Collectors.toList());
        }));
    }

    @RequestMapping("/edit/blogroll/{id}/page")
    public ModelAndView skipBlogrollEdit(@PathVariable("id") String id) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-blogroll.html");
        addAdminModelAndView(view);
        BlogrollEntity internalById = blogrollService.findInternalById(id);
        view.addObject("editData", BlogrollInfo.create(internalById));
        return view;
    }

    @RequestMapping("/edit/auth/{id}/page")
    public ModelAndView skipAuthEdit(@PathVariable("id") String id) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-auth.html");
        addAdminModelAndView(view);
        AuthEntity internalById = authService.findInternalById(id);
        List<AuthEntity> fathers = authService.findFathers("0");
        fathers = fathers.stream().filter(k -> !k.getId().equals(id)).collect(Collectors.toList());
        view.addObject("editData", AuthInfo.create(internalById));
        view.addObject("fathers", AuthInfo.createAuthList(fathers));
        return view;
    }

    @RequestMapping("/edit/role/{id}/page")
    public ModelAndView skipRoleEdit(@PathVariable("id") String id) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-role.html");
        addAdminModelAndView(view);
        RoleEntity internalById = roleService.findInternalById(id);
        List<String> authIds = internalById.getAuthIds();
        if (CollectionUtils.isEmpty(authIds)) {
            view.addObject("auths", getAuthMap(authService.findAll(), Lists.newArrayList()));
        } else {
            view.addObject("auths", getAuthMap(authService.findAll(), authIds));
        }
        view.addObject("editData", RoleInfo.create(internalById));
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

    @RequestMapping("/blogroll/settings")
    public ModelAndView blogrollSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/blogroll-settings.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view);
        return view;
    }

    @RequestMapping("/auth/settings")
    public ModelAndView authSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/auth-settings.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view);
        return view;
    }

    @RequestMapping("/role/settings")
    public ModelAndView roleSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/role-settings.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
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
