package com.ijson.blog.service.model.info;

import com.google.common.base.Strings;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/3/5 8:45 PM
 */
public interface Comment {


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
        private List<Object> replyBody = Lists.newArrayList();//子评论


        public static CommentResult create(String id,
                                           String img,
                                           String replyName,
                                           String content,
                                           long time,
                                           String address,
                                           String osname,
                                           String browse) {
            CommentResult result = new CommentResult();
            result.setId(id);
            result.setImg(img);
            result.setReplyName(replyName);
            result.setContent(content);
            result.setTime(DateUtils.getInstance().format(time, "yyyy-MM-dd HH:mm:ss"));
            result.setAddress(address);
            result.setOsname(osname);
            result.setBrowse(browse);
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
            return dataList.stream().map(k ->
                    CommentResult.create(
                            k.getId(),
                            k.getFromAvatar(),
                            k.getFromCname(),
                            k.getContent(),
                            k.getCreateTime(),
                            k.getHost(),
                            k.getOs(),
                            k.getBrowse())
            ).collect(Collectors.toList());
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
     * 保存评=评论参数
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


            entity.setReplyType(getReplyType());
            entity.setFatherId(getFatherId());


            String ua = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            OperatingSystem os = userAgent.getOperatingSystem();
            Browser browser = userAgent.getBrowser();
            entity.setUserAgent(ua);
            entity.setOs(os.getName());
            String browserName = browser.getName();
            entity.setBrowse(browserName);
            if (Strings.isNullOrEmpty(getFatherId())) {
                entity.setFatherId("0");
            } else {
                entity.setFatherId(getFatherId());
            }
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
}
