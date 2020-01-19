package com.ijson.blog.controller.admin;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.PostDraftService;
import com.ijson.blog.service.PostService;
import com.ijson.blog.service.UserService;
import com.ijson.blog.service.model.ConsoleData;
import com.ijson.blog.service.model.Post;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import com.ijson.mongo.support.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
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

    /**
     * 后台首页
     *
     * @return
     */
    @RequestMapping("/console/page")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/console.html");
        addAdminModelAndView(view);
        ConsoleData consoleData = postService.getConsoleData();
        view.addObject("consoleData", consoleData);
        return view;
    }


    /**
     * 跳转到添加博客页面
     *
     * @return
     */
    @RequestMapping("/post/add/page")
    public ModelAndView postAdd() {
        ModelAndView view = new ModelAndView();
        addAdminModelAndView(view);
        view.setViewName("admin/post-add.html");
        view.addObject("admin_post_root_active", "active");
        view.addObject("admin_post_add_page_active", "active");
        return view;
    }


    /**
     * 跳转到博客列表
     *
     * @param index
     * @return
     */
    @RequestMapping("/post/list/page")
    public ModelAndView postList() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/post-list.html");
        addAdminModelAndView(view);
        view.addObject("admin_post_root_active", "active");
        view.addObject("admin_post_list_page_active", "active");
        return view;
    }


    @RequestMapping("/draft/list/page")
    public ModelAndView postDriftList() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/post-draft-list.html");
        addAdminModelAndView(view);
        view.addObject("admin_post_root_active", "active");
        view.addObject("admin_draft_list_page_active", "active");
        return view;
    }


    @RequestMapping("role/page")
    public ModelAndView rolePage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/role-list.html");
        addAdminModelAndView(view);
        view.addObject("admin_org_root_active", "active");
        view.addObject("admin_role_page_active", "active");
        return view;
    }


    @RequestMapping("user/page")
    public ModelAndView userPage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/user-list.html");
        addAdminModelAndView(view);
        view.addObject("admin_org_root_active", "active");
        view.addObject("admin_user_page_active", "active");
        return view;
    }


    /**
     * 跳转到编辑博客页面
     *
     * @return
     */
    @RequestMapping("/edit/{ename}/{shamId}/page")
    public ModelAndView skipPostEdit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId) {
        PostEntity entity = postService.findByShamIdInternal(ename, shamId, true);

        ModelAndView view = new ModelAndView();
        view.setViewName("admin/post-add.html");
        addAdminModelAndView(view);
        view.addObject("editData", Post.create(entity));
        view.addObject("topic", Post.create(entity).getTopicName());

        view.addObject("admin_post_root_active", "active");
        view.addObject("admin_post_add_page_active", "active");
        return view;
    }

    @RequestMapping("/draft/edit/{ename}/{shamId}/page")
    public ModelAndView skipPostDriftEdit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId) {
        PostDraftEntity entity = postDraftService.find(ename, shamId);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/post-add.html");
        addAdminModelAndView(view);
        if (Objects.nonNull(entity)) {
            view.addObject("editData", Post.create(entity));
            view.addObject("topic", Post.create(entity).getTopicName());
        }
        view.addObject("admin_post_root_active", "active");
        view.addObject("admin_post_add_page_active", "active");
        return view;
    }


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @RequestMapping("/i/config/page")
    public ModelAndView iconfig(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/i-config.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        UserEntity userEntity = userService.findUserByEname(context.getEname(), null, null);

        if (Objects.nonNull(userEntity.getWorkStartTime()) && userEntity.getWorkStartTime() != 0) {
            userEntity.setStartTime(simpleDateFormat.format(userEntity.getWorkStartTime()));
        }

        if (Objects.nonNull(userEntity.getWorkEndTime()) && userEntity.getWorkEndTime() != 0) {
            userEntity.setEndTime(simpleDateFormat.format(userEntity.getWorkEndTime()));
        }

        addAdminModelAndView(view);


        view.addObject("user", userEntity);

        view.addObject("admin_system_root_active", "active");
        view.addObject("admin_i_config_page_active", "active");
        return view;
    }
}
