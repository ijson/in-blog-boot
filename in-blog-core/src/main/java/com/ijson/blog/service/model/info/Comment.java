package com.ijson.blog.service.model.info;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.model.ReplyType;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.PostService;
import com.ijson.blog.service.UserService;
import com.ijson.blog.util.DateUtils;
import com.ijson.blog.util.Pageable;
import com.ijson.mongo.support.model.PageResult;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/3/5 8:45 PM
 */
public interface Comment {


    /**
     * 后端列表model
     */
    @Data
    class Info {

        private String id;//id
        private String title;//内容
        private long time;//创建时间


        public static Info create(CommentEntity entity) {
            Info info = new Info();
            info.setId(entity.getId());
            info.setTitle(entity.getContent());
            info.setTime(entity.getCreateTime());
            return info;
        }

        public static List<Info> createList(List<CommentEntity> dataList) {
            return dataList.stream().map(k -> {
                return create(k);
            }).collect(Collectors.toList());
        }
    }

    /**
     * 保存文章评论成功后返回结果
     */
    @Data
    class CommentResult {
        private String id;
        private String img;//评论人头像
        private String replyName;//评论人中文名称
        private String content;//回复内容
        private String time;//评论时间
        private String address;//评论人所在地,暂不用
        private String osname;//评论操作系统
        private String browse;//浏览器版本
        private List<ReplyResult> replyBody = Lists.newArrayList();//子评论

        private String ename;
        private String shamId;
        private String postId;

        private String fromAvatar;
        private String fromCname;
        private String fromUserId;


        public static CommentResult create(String id,
                                           String img,
                                           String replyName,
                                           String content,
                                           long time,
                                           String address,
                                           String osname,
                                           String browse,
                                           String ename,
                                           String shamId,
                                           String postId,
                                           String fromAvatar,
                                           String fromCname,
                                           String fromUserId) {
            CommentResult result = new CommentResult();
            result.setId(id);
            result.setImg(img);
            result.setReplyName(replyName);
            result.setContent(content);
            result.setTime(DateUtils.getInstance().format(time, "yyyy-MM-dd HH:mm:ss"));
            result.setAddress(address);
            result.setOsname(osname);
            result.setBrowse(browse);
            result.setEname(ename);
            result.setShamId(shamId);
            result.setPostId(postId);
            result.setFromAvatar(fromAvatar);
            result.setFromCname(fromCname);
            result.setFromUserId(fromUserId);
            return result;
        }


        public static CommentResult create(String id,
                                           String img,
                                           String replyName,
                                           String content,
                                           long time,
                                           String address,
                                           String osname,
                                           String browse,
                                           String ename,
                                           String shamId,
                                           String postId,
                                           String fromAvatar,
                                           String fromCname,
                                           String fromUserId,
                                           List<ReplyResult> child) {
            CommentResult result = CommentResult.create(
                    id, img, replyName, content, time, address, osname, browse, ename, shamId, postId, fromAvatar, fromCname, fromUserId);
            result.setReplyBody(child);
            return result;
        }

        /**
         * 列表查询时转换
         *
         * @param result
         * @return
         */
        public static List<CommentResult> transform(PageResult<CommentEntity> result) {
            List<CommentEntity> dataList = result.getDataList();
            if (CollectionUtils.isEmpty(dataList)) {
                return Lists.newArrayList();
            }
            return levelFlatComment(dataList);
        }


        /**
         * 只获取一级评论及1级评论下的评论,且一级一下的评论都拍平,后期支持更多层级
         *
         * @param dataList
         * @return
         */
        public static List<CommentResult> levelFlatComment(List<CommentEntity> dataList) {
            return dataList.stream().filter(v -> v.getFatherId().equals("0")).map(k -> {

                        List<ReplyResult> child = getChild(dataList, k.getId(), k.getFatherId());

                        child.sort(Comparator.comparing(ReplyResult::getCreateTime));

                        return CommentResult.create(
                                k.getId(),
                                k.getFromAvatar(),
                                k.getFromCname(),
                                k.getContent(),
                                k.getCreateTime(),
                                k.getHost(),
                                k.getOs(),
                                k.getBrowse(),
                                k.getEname(),
                                k.getShamId(),
                                k.getPostId(),
                                k.getFromAvatar(),
                                k.getFromCname(),
                                k.getFromUserId(), child);
                    }
            ).collect(Collectors.toList());
        }

        public static List<ReplyResult> getChild(List<CommentEntity> dataList, String currentId, String fatherId) {
            return getCommentChild(dataList.stream().filter(k -> !k.getFatherId().equals("0")).collect(Collectors.toList()), currentId, fatherId, Lists.newArrayList()).stream().map(ints -> {
                return ReplyResult.create(
                        ints.getId(),
                        ints.getFromAvatar(),
                        ints.getFromCname(),
                        ints.getContent(),
                        ints.getCreateTime(),
                        ints.getHost(),
                        ints.getOs(),
                        ints.getBrowse(),
                        ints.getEname(),
                        ints.getShamId(),
                        ints.getPostId(),
                        ints.getToAvatar(),
                        ints.getToCname(),
                        ints.getToUserId());
            }).collect(Collectors.toList());
        }

        public static List<CommentEntity> getCommentChild(List<CommentEntity> dataList, String currentId, String fatherId, List<CommentEntity> result) {
            dataList.forEach(k -> {
                if (k.getFatherId().equals(currentId)) {
                    result.add(k);
                    getCommentChild(dataList, k.getId(), k.getFatherId(), result);
                }
            });
            return result;
        }


    }


    /**
     * 评论查询
     */
    @Data
    class ListArg {
        private Integer index;
        private String ename;
        private String shamId;
    }

    /**
     * 查询列表时,需要封装从model
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class ListResult {
        private List<CommentResult> reply;
        private Pageable pageable;
    }


    /**
     * 保存回复的回复后返回
     */
    @Data
    class ReplyResult {
        private String id;
        private String img;//评论人头像
        private String replyName;//评论人中文名称
        private String content;//回复内容
        private String time;//评论时间
        private String address;//评论人所在地,暂不用
        private String osname;//评论操作系统
        private String browse;//浏览器版本
        private List<Object> replyBody = Lists.newArrayList();//子评论

        private String ename;
        private String shamId;
        private String postId;

        private String fromAvatar;
        private String fromCname;
        private String fromUserId;
        private String beReplyName;
        private long createTime;


        public static ReplyResult create(String id,
                                         String img,
                                         String replyName,
                                         String content,
                                         long time,
                                         String address,
                                         String osname,
                                         String browse,
                                         String ename,
                                         String shamId,
                                         String postId,
                                         String fromAvatar,
                                         String fromCname,
                                         String fromUserId) {
            ReplyResult result = new ReplyResult();
            result.setId(id);
            result.setImg(img);
            result.setReplyName(replyName);
            result.setContent(content);
            result.setTime(DateUtils.getInstance().format(time, "yyyy-MM-dd HH:mm:ss"));
            result.setAddress(address);
            result.setOsname(osname);
            result.setBrowse(browse);
            result.setEname(ename);
            result.setShamId(shamId);
            result.setPostId(postId);
            result.setFromAvatar(fromAvatar);
            result.setFromCname(fromCname);
            result.setFromUserId(fromUserId);
            result.setBeReplyName(fromCname);
            result.setCreateTime(time);
            return result;
        }
    }

    /**
     * 保存 回复参数
     */
    @Data
    class ReplyArg {
        private String ename;
        private String shamId;
        private String fatherId;
        private ReplyType replyType;
        private String postId;
        private String replyName;
        private String content;

        private String toAvatar;
        private String toCname;
        private String toUserId;

        public CommentEntity getCreateEntity(AuthContext context,
                                             PostService postService,
                                             UserService userService,
                                             HttpServletRequest request) {
            CommentEntity entity = new CommentEntity();

            //文章列表查询有多少评论时使用
            entity.setPostId(getPostId());
            //评论文章
            entity.setEname(getEname());
            entity.setShamId(getShamId());
            entity.setContent(getContent());

            //设置被评论人,评论回复时的时候设置,评论文章不需要
            entity.setToAvatar(getToAvatar());
            entity.setToCname(getToCname());
            entity.setToUserId(getToUserId());

            //设置评论人
            entity.setFromAvatar(context.getAvatar());
            entity.setFromCname(context.getCname());
            entity.setFromUserId(context.getId());


            entity.setReplyType(ReplyType.reply);
            entity.setFatherId(getFatherId());


            String ua = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            OperatingSystem os = userAgent.getOperatingSystem();
            Browser browser = userAgent.getBrowser();
            entity.setUserAgent(ua);
            entity.setOs(os.getName());
            String browserName = browser.getName();
            entity.setBrowse(browserName);
            entity.setHost(request.getRemoteHost());

            entity.setLastModifiedBy(context.getId());
            entity.setLastModifiedTime(System.currentTimeMillis());
            entity.setDeleted(false);
            entity.setEnable(true);
            entity.setCreatedBy(context.getId());
            entity.setCreateTime(System.currentTimeMillis());

            return entity;
        }
    }


    /**
     * 保存评论参数
     */
    @Data
    class Arg {
        private String replyCode;

        private String ename;
        private String shamId;

        private String content;

        //评论还是回复
        private ReplyType replyType;
        //被评论人头像
        private String toAvatar;
        //被评论人名称
        private String toCname;
        //被评论人userId
        private String toUserId;


        //评论人头像
        private String fromAvatar;
        //评论人名称
        private String fromCname;
        //评论人userId
        private String fromUserId;

        private String fatherId;

        private String postId;

        //文章作者userId
        private String author;

        /**
         * TODO 不推荐model中使用service
         *
         * @param context
         * @param postService
         * @param userService
         * @return
         */
        public CommentEntity getCreateEntity(AuthContext context,
                                             PostService postService,
                                             UserService userService,
                                             HttpServletRequest request) {

            CommentEntity entity = new CommentEntity();

            //文章列表查询有多少评论时使用
            entity.setPostId(getPostId());
            //评论文章
            entity.setEname(getEname());
            entity.setShamId(getShamId());
            entity.setContent(getContent());

            //设置被评论人,评论回复时的时候设置,评论文章不需要
            entity.setToAvatar(getToAvatar());
            entity.setToCname(getToCname());
            entity.setToUserId(getToUserId());


            //设置评论人
            entity.setFromAvatar(context.getAvatar());
            entity.setFromCname(context.getCname());
            entity.setFromUserId(context.getId());


            entity.setReplyType(ReplyType.comment);
            entity.setFatherId("0");

            entity.setAuthor(getAuthor());

            String ua = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            OperatingSystem os = userAgent.getOperatingSystem();
            Browser browser = userAgent.getBrowser();
            entity.setUserAgent(ua);
            entity.setOs(os.getName());
            String browserName = browser.getName();
            entity.setBrowse(browserName);
            entity.setHost(request.getRemoteHost());

            entity.setLastModifiedBy(context.getId());
            entity.setLastModifiedTime(System.currentTimeMillis());
            entity.setDeleted(false);
            entity.setEnable(true);
            entity.setCreatedBy(context.getId());
            entity.setCreateTime(System.currentTimeMillis());

            return entity;
        }
    }

    /**
     * 页面全部  博文回复  评论回复 总数
     */
    @Data
    class CommentCount {
        private long count;
        private long post;
        private long reply;

        public static CommentCount create(long postCount, long replyCount) {
            CommentCount commentCount = new CommentCount();
            commentCount.setPost(postCount);
            commentCount.setReply(replyCount);
            commentCount.setCount(commentCount.getPost() + commentCount.getReply());
            return commentCount;
        }
    }
}
