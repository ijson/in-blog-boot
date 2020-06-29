package com.ijson.blog.controller.admin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.*;
import com.ijson.blog.dao.model.ViewOrAdminType;
import com.ijson.blog.model.*;
import com.ijson.blog.service.model.info.*;
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

    @RequestMapping("/console/page")
    public ModelAndView indexv2(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/index.html");
        addAdminModelAndView(view,request);
        view.addObject("menu", getMenu(request));
        return view;
    }

    @RequestMapping("/welcome/page")
    public ModelAndView welcome(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/welcome.html");
        addAdminModelAndView(view,request);
        view.addObject("systemInfo", SystemInfo.getSystemInfo());
        WelcomeInfo consoleData = postService.getConsoleData();
        view.addObject("consoleData", consoleData);
        return view;
    }

    @RequestMapping("/default/welcome/page")
    public ModelAndView defaultWelcome(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/welcome-default.html");
        WelcomeInfo consoleData = postService.getUserConsoleData(getContext(request));
        view.addObject("consoleData", consoleData);
        return view;
    }


    @RequestMapping("/save/article")
    public ModelAndView articleAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        addAdminModelAndView(view,request);
        List<IndexMenuEntity> indexMenu = indexMenuService.findAll();
        view.addObject("indexMenu", indexMenu);
        view.addObject("editData", null);
        view.setViewName("admin/save-article.html");
        return view;
    }

    @RequestMapping("/save/user")
    public ModelAndView skipUserAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-user.html");
        addAdminModelAndView(view,request);
        List<RoleEntity> roleAll = roleService.findAll();
        if (CollectionUtils.isEmpty(roleAll)) {
            view.addObject("roles", Lists.newArrayList());
        } else {
            view.addObject("roles", roleAll.stream().map(RoleInfo::create).collect(Collectors.toList()));
        }
        view.addObject("editData", null);
        return view;
    }

    @RequestMapping("/save/blogroll")
    public ModelAndView skipBlogrollAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-blogroll.html");
        addAdminModelAndView(view,request);
        view.addObject("editData", null);
        return view;
    }


    @RequestMapping("/save/header")
    public ModelAndView skipHeaderAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-header.html");
        addAdminModelAndView(view,request);
        view.addObject("editData", null);
        return view;
    }

    @RequestMapping("/save/index/menu")
    public ModelAndView skipIndexMenuAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-index-menu.html");
        addAdminModelAndView(view,request);
        view.addObject("editData", null);
        return view;
    }

    @RequestMapping("/save/theme")
    public ModelAndView skipThemeAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-theme.html");
        addAdminModelAndView(view,request);
        view.addObject("editData", null);
        return view;
    }

    @RequestMapping("/save/site")
    public ModelAndView siteSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/save-site.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }

        List<RoleEntity> roleAll = roleService.findAll();
        if (CollectionUtils.isEmpty(roleAll)) {
            view.addObject("roles", Lists.newArrayList());
        } else {
            view.addObject("roles", roleAll.stream().map(RoleInfo::create).collect(Collectors.toList()));
        }
        view.addObject("site", getConfig());

        List<ThemeEntity> themeAll = themeService.findAll();

        List<ThemeEntity> adminThemeList = themeAll.stream().filter(k -> k.getType() == ViewOrAdminType.admin).collect(Collectors.toList());
        view.addObject("adminTheme", ThemeInfo.createList(adminThemeList));
        List<ThemeEntity> viewThemeList = themeAll.stream().filter(k -> k.getType() == ViewOrAdminType.view).collect(Collectors.toList());
        view.addObject("viewTheme", ThemeInfo.createList(viewThemeList));
        addAdminModelAndView(view,request);
        return view;
    }


    @RequestMapping("/save/auth")
    public ModelAndView skipAuthAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-auth.html");
        addAdminModelAndView(view,request);
        List<AuthEntity> fathers = authService.findFathers("0");
        view.addObject("editData", null);
        view.addObject("fathers", AuthInfo.createAuthList(fathers));

        return view;
    }

    @RequestMapping("/save/role")
    public ModelAndView skipRoleAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-role.html");
        addAdminModelAndView(view,request);
        List<AuthEntity> allAuth = authService.findAll();
        if (CollectionUtils.isEmpty(allAuth)) {
            view.addObject("auths", Maps.newHashMap());
        } else {
            view.addObject("auths", AuthInfo.getAuthMap(allAuth, Lists.newArrayList(), false));
        }
        view.addObject("editData", null);
        return view;
    }

    /**
     * 跳转到编辑博客页面
     *
     * @return
     */
    @RequestMapping("/edit/article/{ename}/{shamId}")
    public ModelAndView skipV2Edit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId,HttpServletRequest request) {
        PostEntity entity = postService.findByShamIdInternal(ename, shamId, true);
        List<IndexMenuEntity> indexMenu = indexMenuService.findAll();
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-article.html");
        addAdminModelAndView(view,request);
        view.addObject("indexMenu", indexMenu);
        view.addObject("editData", PostInfo.create(entity));
        view.addObject("topic", PostInfo.create(entity).getTopicName());
        return view;
    }

    @RequestMapping("/edit/user/{id}")
    public ModelAndView skipUserEdit(@PathVariable("id") String id,HttpServletRequest request) {
        UserEntity internalById = userService.findInternalById(id);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-user.html");
        addAdminModelAndView(view,request);
        List<RoleEntity> roleAll = roleService.findAll();
        if (CollectionUtils.isEmpty(roleAll)) {
            view.addObject("roles", Lists.newArrayList());
        } else {
            view.addObject("roles", roleAll.stream().map(RoleInfo::create).collect(Collectors.toList()));
        }
        view.addObject("editData", UserInfo.create(internalById));
        return view;
    }


    @RequestMapping("/edit/blogroll/{id}")
    public ModelAndView skipBlogrollEdit(@PathVariable("id") String id,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-blogroll.html");
        addAdminModelAndView(view,request);
        BlogrollEntity internalById = blogrollService.findInternalById(id);
        view.addObject("editData", BlogrollInfo.create(internalById));
        return view;
    }

    @RequestMapping("/edit/header/{id}")
    public ModelAndView skipHeaderEdit(@PathVariable("id") String id,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-header.html");
        addAdminModelAndView(view,request);
        HeaderEntity internalById = headerService.findInternalById(id);
        view.addObject("editData", HeaderInfo.create(internalById));
        return view;
    }

    @RequestMapping("/edit/index/menu/{id}")
    public ModelAndView skipIndexMenuEdit(@PathVariable("id") String id,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-index-menu.html");
        addAdminModelAndView(view,request);
        IndexMenuEntity internalById = indexMenuService.findInternalById(id);
        view.addObject("editData", IndexMenuInfo.create(internalById));
        return view;
    }

    @RequestMapping("/edit/theme/{id}")
    public ModelAndView skipThemeEdit(@PathVariable("id") String id,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-theme.html");
        addAdminModelAndView(view,request);
        ThemeEntity internalById = themeService.findInternalById(id);
        view.addObject("editData", ThemeInfo.create(internalById));
        return view;
    }

    @RequestMapping("/edit/auth/{id}")
    public ModelAndView skipAuthEdit(@PathVariable("id") String id,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-auth.html");
        addAdminModelAndView(view,request);
        AuthEntity internalById = authService.findInternalById(id);
        List<AuthEntity> fathers = authService.findFathers("0");
        fathers = fathers.stream().filter(k -> !k.getId().equals(id)).collect(Collectors.toList());
        view.addObject("editData", AuthInfo.create(internalById));
        view.addObject("fathers", AuthInfo.createAuthList(fathers));
        return view;
    }

    @RequestMapping("/edit/role/{id}")
    public ModelAndView skipRoleEdit(@PathVariable("id") String id,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-role.html");
        addAdminModelAndView(view,request);
        RoleEntity internalById = roleService.findInternalById(id);
        List<String> authIds = internalById.getAuthIds();
        boolean disabled = false;
        if ("system".equals(internalById.getEname())) {
            disabled = true;
        }
        if (CollectionUtils.isEmpty(authIds)) {
            view.addObject("auths", AuthInfo.getAuthMap(authService.findAll(), Lists.newArrayList(), disabled));
        } else {
            view.addObject("auths", AuthInfo.getAuthMap(authService.findAll(), authIds, disabled));
        }
        view.addObject("editData", RoleInfo.create(internalById));
        return view;
    }

    @RequestMapping("/edit/draft/{ename}/{shamId}")
    public ModelAndView skipV2PostDriftEdit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId,HttpServletRequest request) {
        PostDraftEntity entity = postDraftService.find(ename, shamId);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-article.html");
        addAdminModelAndView(view,request);
        List<IndexMenuEntity> indexMenu = indexMenuService.findAll();
        view.addObject("indexMenu", indexMenu);
        if (Objects.nonNull(entity)) {
            view.addObject("editData", PostInfo.create(entity));
            view.addObject("topic", PostInfo.create(entity).getTopicName());
        } else {
            view.addObject("editData", null);
            view.addObject("topic", null);
        }
        return view;
    }


    @RequestMapping("/edit/tag/{ename}/{shamId}")
    public ModelAndView skipTagEdit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId,HttpServletRequest request) {
        TopicEntity entity = topicService.findTopicByShamIdAndEname(ename, shamId);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-tag.html");
        addAdminModelAndView(view,request);
        if (Objects.nonNull(entity)) {
            view.addObject("editData", TopicInfo.create(entity));
        }
        return view;
    }

    @RequestMapping("/view/tag/post/{ename}/{shamId}")
    public ModelAndView viewTagPOst(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId,HttpServletRequest request) {
        TopicEntity entity = topicService.findTopicByShamIdAndEname(ename, shamId);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-tag-article.html");
        addAdminModelAndView(view,request);
        if (Objects.nonNull(entity)) {
            view.addObject("viewData", TopicInfo.create(entity));
        }
        return view;
    }


    @RequestMapping("/settings/personal")
    public ModelAndView userSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/settings-personal.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view,request);
        view.addObject("user", userService.findUserByEname(context.getEname(), null, null));
        return view;
    }


    @RequestMapping("/settings/ext/user")
    public ModelAndView extUserSetting(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/settings-ext-user.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view,request);
        if (context.getRegSourceType() == RegSourceType.qqReg) {
            view.addObject("user", userService.findByQQOpenId(context.getQqOpenId()));
        } else {
            view.addObject("user", userService.findUserByEname(context.getEname(), null, null));
        }

        return view;
    }

    @RequestMapping("/settings/article/detail/{ename}/{shamId}")
    public ModelAndView articleDetailSettings(HttpServletRequest request,
                                              @PathVariable("ename") String ename,
                                              @PathVariable("shamId") String shamId) {
        ModelAndView view = new ModelAndView("admin/settings-article-detail.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        PostEntity entity = postService.findByShamIdInternal(ename, shamId, true);
        view.addObject("viewData", PostInfo.create(entity));

        addAdminModelAndView(view,request);
        view.addObject("user", userService.findUserByEname(context.getEname(), null, null));
        return view;
    }

    @RequestMapping("/list/blogroll")
    public ModelAndView blogrollSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-blogroll.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view,request);
        return view;
    }

    @RequestMapping("/list/auth")
    public ModelAndView authSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-auth.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view,request);
        return view;
    }


    @RequestMapping("/list/role")
    public ModelAndView roleSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-role.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view,request);
        return view;
    }


    @RequestMapping("/list/user")
    public ModelAndView userList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-user.html");
        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view,request);
        return view;
    }


    @RequestMapping("/list/user/delete")
    public ModelAndView userDelList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-user-del.html");
        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view,request);
        return view;
    }

    @RequestMapping("/list/post")
    public ModelAndView postV2List(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-article.html");
        addAdminModelAndView(view,request);
        return view;
    }


    @RequestMapping("/list/draft")
    public ModelAndView postDriftList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-article-draft.html");
        addAdminModelAndView(view,request);
        return view;
    }

    /**
     * 当前人草稿列表
     *
     * @return
     */
    @RequestMapping("/list/user/draft")
    public ModelAndView postUserDriftList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-user-article-draft.html");
        addAdminModelAndView(view,request);
        return view;
    }


    @RequestMapping("/list/user/post")
    public ModelAndView postUserList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-user-article.html");
        addAdminModelAndView(view,request);
        return view;
    }

    @RequestMapping("/list/audit/post")
    public ModelAndView postAuditList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-audit-article.html");
        addAdminModelAndView(view,request);
        return view;
    }


    @RequestMapping("/list/tag")
    public ModelAndView tagList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-tags.html");
        addAdminModelAndView(view,request);
        return view;
    }

    @RequestMapping("/list/header")
    public ModelAndView headerList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-header.html");
        addAdminModelAndView(view,request);
        return view;
    }

    @RequestMapping("/list/index/menu")
    public ModelAndView indexMenuList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-index-menu.html");
        addAdminModelAndView(view,request);
        return view;
    }


    @RequestMapping("/list/theme")
    public ModelAndView themeList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-theme.html");
        addAdminModelAndView(view,request);
        return view;
    }

    @RequestMapping("/list/message")
    public ModelAndView messageList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/list-message.html");
        addAdminModelAndView(view,request);
        return view;
    }

}
