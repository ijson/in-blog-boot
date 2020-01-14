package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.PostService;
import com.ijson.blog.service.UserService;
import com.ijson.blog.service.model.ConsoleData;
import com.ijson.blog.service.model.Post;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.Pageable;
import com.ijson.blog.util.PassportHelper;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

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
        view.addObject("post_active", "active");
        view.addObject("post_create_active", "active");
        return view;
    }


    /**
     * 跳转到博客列表
     *
     * @param index
     * @param keyWord
     * @return
     */
    @RequestMapping("/post/list/page")
    public ModelAndView postList(Integer index, String keyWord) {
        Page page = new Page();
        if (Objects.isNull(index)) {
            index = 1;
        }
        page.setPageNumber(index);

        PostQuery query = new PostQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setLikeTitle(true);
            query.setTitle(keyWord);
        }

        PageResult<PostEntity> result = postService.find(query, page);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/post-list.html");
        addAdminModelAndView(view);
        view.addObject("total", result.getTotal());
        view.addObject("page", new Pageable(((Long) result.getTotal()).intValue(), index));


        List<PostEntity> dataList = result.getDataList();
        List<Post> posts = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            Set<String> userIds = dataList.stream().map(PostEntity::getUserId).collect(Collectors.toSet());
            posts = Post.postList(result, userService.findCnameByIds(userIds));
        }


        view.addObject("dataList", posts);
        view.addObject("keyWord", keyWord);
        view.addObject("post_active", "active");
        view.addObject("post_list_active", "active");
        return view;
    }


    /**
     * 跳转到编辑博客页面
     *
     * @return
     */
    @RequestMapping("/edit/{ename}/{shamId}/page")
    public ModelAndView skipPostEdit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId) {
        PostEntity entity = postService.findByShamIdInternal(ename, shamId,true);

        ModelAndView view = new ModelAndView();
        view.setViewName("admin/post-add.html");
        addAdminModelAndView(view);
        view.addObject("editData", Post.create(entity));
        view.addObject("topic", Post.create(entity).getTopicName());

        view.addObject("post_active", "active");
        view.addObject("post_create_active", "active");
        return view;
    }


    @RequestMapping("/i/config/page")
    public ModelAndView iconfig(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/i-config.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        UserEntity userEntity = userService.findUserByEname(context.getEname(), null, null);

        addAdminModelAndView(view);

        view.addObject("user", userEntity);

        view.addObject("system_active", "active");
        view.addObject("i_config_active", "active");
        return view;
    }
}
