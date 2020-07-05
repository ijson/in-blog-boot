package com.ijson.blog.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.*;
import com.ijson.blog.dao.query.HeaderQuery;
import com.ijson.blog.exception.BlogBusinessException;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogCreateException;
import com.ijson.blog.interceptor.LoginInterceptor;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.AuthInfo;
import com.ijson.blog.model.Constant;
import com.ijson.blog.model.arg.AuthArg;
import com.ijson.blog.service.*;
import com.ijson.blog.service.model.info.*;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import com.ijson.blog.util.VerifyCodeUtils;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 11:31 PM
 */
@Slf4j
@Controller
public class BaseController {


    @Autowired
    protected TopicService topicService;

    @Autowired
    protected PostService postService;

    @Autowired
    protected PostDraftService postDraftService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected WebSiteService webSiteService;

    @Autowired
    protected FileUploadService fileUploadService;

    @Autowired
    protected BlogrollService blogrollService;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected RoleService roleService;

    @Autowired
    protected HeaderService headerService;

    @Autowired
    protected IndexMenuService indexMenuService;

    @Autowired
    protected ThemeService themeService;


    @Value("${web.ctx}")
    protected String webCtx;

    @Value("${web.ename}")
    protected String webEname;


    @Value("${cdn.server}")
    protected String cdnServer;

    @Value("${cdn.upload.path}")
    protected String cdnUploadPath;

    /**
     * 后台使用
     *
     * @param view
     */
    protected void addAdminModelAndView(ModelAndView view, HttpServletRequest request) {
        view.addObject("site", getConfig());
        //TODO 返回当前人 接收到的 评论  回复的总数
        view.addObject("cmtcount", getCmtCount(request));
    }

    private Object getCmtCount(HttpServletRequest request) {
        return null;
    }


    /**
     * 前台使用
     *
     * @param view
     */
    protected void addViewModelAndView(HttpServletRequest request, ModelAndView view) {

        //热门文章 缓存 10分钟
        view.addObject("hots", getHotPosts());
        //热门标签 缓存 10分钟
        view.addObject("hotTags", getHotTags());
        //友情链接 缓存 10分钟
        view.addObject("blogrolls", getBlogrolls());
        //博文总数 缓存 10分钟
        view.addObject("total", postService.count());
        //网站设置 缓存 5分钟
        view.addObject("site", getConfig());
        //博主信息 缓存 60分钟
        view.addObject("user", getBlogAdminUser(request));
        //header信息 缓存 1分钟
        view.addObject("header", getHeader());
        //首页菜单 缓存 5分钟
        view.addObject("indexMenu", getIndexMenu());

        //最近发表 无缓存
        view.addObject("lastPublish", getRecePublishPosts());
        //博客访问总数 无缓存
        view.addObject("webSiteCount", postService.getWebSiteCount(null));


    }

    protected String getViewTheme() {
        return "view/" + getConfig().getViewThemeEname();
    }

    /**
     * 获取首页顶部菜单项
     *
     * @return
     */
    @Cacheable(value = "indexMenu")
    public List<IndexMenuInfo> getIndexMenu() {
        List<IndexMenuEntity> all = indexMenuService.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return IndexMenuInfo.getDefaultIndexMenu();
        }
        List<IndexMenuInfo> list = IndexMenuInfo.createList(all);
        return list.stream().sorted((o1, o2) -> o1.getOrder() < o2.getOrder() ? -1 : 1).collect(Collectors.toList());
    }

    /**
     * 获取头信息,例如百度分析等
     *
     * @return
     */
    @Cacheable(value = "header")
    public List<HeaderInfo> getHeader() {
        HeaderQuery headerQuery = new HeaderQuery();
        headerQuery.setEnable(true);
        Page page = new Page();
        page.setPageSize(500);
        PageResult<HeaderEntity> headerResult = headerService.find(headerQuery, page);
        List<HeaderEntity> dataList = headerResult.getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return HeaderInfo.createList(dataList);
    }

    /**
     * 获取友情链接
     *
     * @return
     */
    private List<BlogrollInfo> getBlogrolls() {
        List<BlogrollEntity> all = blogrollService.findAll();
        return BlogrollInfo.createBlogrollList(all);
    }

    /**
     * 获取热门文章
     *
     * @return
     */
    private List<PostInfo> getHotPosts() {
        List<PostEntity> serviceHotPost = postService.findHotPostBeforeTen();
        return serviceHotPost.stream().map(PostInfo::createSimple).sorted((o1, o2) -> o1.getViews() > o2.getViews() ? -1 : 1).collect(Collectors.toList());
    }

    /**
     * 获取最近发表的文章
     *
     * @return
     */
    private List<PostInfo> getRecePublishPosts() {
        List<PostEntity> recentlyPublished = postService.findRecentlyPublishedBeforeTen();
        return recentlyPublished.stream().map(PostInfo::createSimple).collect(Collectors.toList());
    }


    /**
     * 获取最热的标签
     *
     * @return
     */
    private List<HotTopicInfo> getHotTags() {
        List<TopicEntity> topicEntities = topicService.findHotTag();
        return HotTopicInfo.getHotTopic(topicEntities);
    }


    /**
     * 获取系统配置项
     *
     * @return
     */
    protected ConfigEntity getConfig() {
        return webSiteService.findAllConfig("all");
    }

    /**
     * 获取博客管理信息
     *
     * @return
     */
    protected UserInfo getBlogAdminUser(HttpServletRequest request) {
        AuthContext context = getContext(request);
        if (Objects.nonNull(context)) {
            return UserInfo.create(context);
        }
        UserEntity userEntity = userService.findUserByEname(webEname, null, null);
        if (Objects.isNull(userEntity)) {
            return new UserInfo();
        }
        return UserInfo.create(userEntity);
    }

    /**
     * 获取context信息
     *
     * @param request
     * @return
     */
    public AuthContext getContext(HttpServletRequest request) {
        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        if (!Strings.isNullOrEmpty(cookieValue)) {
            AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
            if (context != null) {
                return context;
            }
        }
        return null;
    }

    /**
     * 生成验证码
     *
     * @param response
     * @param session
     * @param varCodeKey
     * @param varCodeTime
     * @throws IOException
     */
    protected void generateVerification(HttpServletResponse response, HttpSession session, String varCodeKey, String varCodeTime) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        session.removeAttribute(varCodeKey);
        session.removeAttribute(varCodeTime);

        session.setAttribute(varCodeKey, verifyCode.toLowerCase());
        session.setAttribute(varCodeTime, LocalDateTime.now());

        // 生成图片
        int w = 100, h = 30;
        OutputStream out = response.getOutputStream();
        VerifyCodeUtils.outputImage(w, h, out, verifyCode);
    }

    /**
     * 获取动态菜单
     *
     * @param request
     * @return
     */
    protected Map<AuthArg, List<AuthInfo>> getMenu(HttpServletRequest request) {
        AuthContext context = getContext(request);
        List<AuthEntity> menuAuth = context.getAuths().stream().filter(k -> {
            return k.getMenuType() == Constant.MenuType.menu && k.getShowMenu();
        }).collect(Collectors.toList());
        return AuthInfo.getAuthMap(menuAuth, Lists.newArrayList(), false);
    }

    /**
     * @param request
     * @param handleContextException 自行处理context 异常
     * @param handleAuthException    自行处理权限异常
     * @return
     */
    protected AuthContext regularCheck(HttpServletRequest request, boolean handleContextException, boolean handleAuthException) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            if (handleContextException) {
                return null;
            } else {
                throw new BlogBusinessException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
            }
        }
        if (!LoginInterceptor.isParadigm(context.getPermissionPath(), request.getRequestURI())) {
            if (handleAuthException) {
                return null;
            } else {
                throw new BlogCreateException(BlogBusinessExceptionCode.NO_RIGHT_TO_DO_THIS);
            }
        }
        return context;
    }
}
