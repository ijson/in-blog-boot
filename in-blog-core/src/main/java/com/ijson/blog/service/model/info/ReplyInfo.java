package com.ijson.blog.service.model.info;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.ReplyEntity;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.PageResult;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/25 2:10 AM
 */
@Data
public class ReplyInfo {

    private String topicId;
    private String postId;
    private String content;
    private String portraits;
    private String replyName;
    private String beReplyName;
    private String address;
    private String browse;
    private String fatherId;
    private String lastModifiedBy;
    private String createdBy;
    private long createTime;
    private String img;
    private List<ReplyInfo> replyBody = Lists.newArrayList();

    private String time;
    private String osname;

    private String replyCode;
    private String ename;
    private String shamId;
    private String replyUserId;

    private String id;
    private String userId;
    private String beReplyId;


    /*

     {
            id: 1,
            img: im,
            replyName: "帅大叔",
            beReplyName: "匿名",
            content: "同学聚会，看到当年追我的屌丝开着宝马车带着他老婆来了，他老婆是我隔壁宿舍的同班同学，心里后悔极了。",
            time: "2017-10-17 11:42:53",
            address: "深圳",
            osname: "",
            browse: "谷歌",
            replyBody: []
        },
        {
            id: 2,
            img: im,
            replyName: "匿名",
            beReplyName: "",
            content: "到菜市场买菜，看到一个孩子在看摊，我问：“一只鸡多少钱？” 那孩子回答：“23。” 我又问：“两只鸡多少钱？” 孩子愣了一下，一时间没算过来，急中生智大吼一声：“一次只能买一只！”",
            time: "2017-10-17 11:42:53",
            address: "深圳",
            osname: "",
            browse: "谷歌",
            replyBody: []
        },
        {
            id: 3,
            img: im,
            replyName: "帅大叔",
            beReplyName: "匿名",
            content: "同学聚会，看到当年追我的屌丝开着宝马车带着他老婆来了，他老婆是我隔壁宿舍的同班同学，心里后悔极了。",
            time: "2017-10-17 11:42:53",
            address: "深圳",
            osname: "win10",
            browse: "谷歌",
            replyBody: []
        }

     */
    public static ReplyEntity formReplyEntity(ReplyInfo reply, HttpServletRequest request, AuthContext context) {
        ReplyEntity entity = new ReplyEntity();
        entity.setTopicId(reply.getTopicId());
        entity.setUserId(context.getId());
        entity.setPostId(reply.getPostId());
        entity.setContent(reply.getContent());
        entity.setPortraits(reply.getPortraits());
        entity.setReplyName(context.getCname());
        entity.setBeReplyName(context.getId());
        entity.setAddress("未知");
        entity.setEname(reply.getEname());
        entity.setShamId(reply.getShamId());
        entity.setIpAddress(request.getRemoteHost());
        String ua = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        OperatingSystem os = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        entity.setUserAgent(ua);
        entity.setOs(os.getName());
        String browserName = browser.getName();
        entity.setBrowse(browserName);
        if (Strings.isNullOrEmpty(reply.getFatherId())) {
            entity.setFatherId("0");
        } else {
            entity.setFatherId(reply.getFatherId());
        }
        entity.setLastModifiedBy(context.getId());
        entity.setDeleted(false);
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setAvatar(context.getAvatar());
        entity.setBeReplyId(reply.getBeReplyId());
        return entity;
    }


    public static ReplyInfo formReply(ReplyEntity entity) {
        ReplyInfo reply = new ReplyInfo();
        reply.setContent(entity.getContent());
        reply.setBrowse(entity.getBrowse());
        reply.setReplyName(entity.getReplyName());
        reply.setEname(entity.getEname());
        reply.setShamId(entity.getShamId());
        reply.setImg(entity.getAvatar());
        reply.setTime(DateFormatUtils.format(new Date(entity.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
        reply.setAddress(entity.getAddress());
        reply.setOsname(entity.getOs());
        reply.setReplyBody(Lists.newArrayList());
        reply.setReplyUserId(entity.getCreatedBy());
        reply.setId(entity.getId());
        reply.setFatherId(entity.getFatherId());
        reply.setBeReplyName(entity.getBeReplyName());
        reply.setUserId(entity.getUserId());
        reply.setBeReplyId(entity.getBeReplyId());
        return reply;
    }

    public static List<ReplyInfo> transform(PageResult<ReplyEntity> result, Function<Set<String>, Map<String, String>> userName) {
        List<ReplyEntity> dataList = result.getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }


        Set<String> uSetIds = dataList.stream().map(k -> {
            if (!Strings.isNullOrEmpty(k.getBeReplyId())) {
                return k.getBeReplyId();
            } else {
                return "";
            }
        }).collect(Collectors.toSet());

        Map<String, String> idOrNames = userName.apply(uSetIds);

        List<ReplyInfo> replies = result.getDataList().stream().map(k -> {
            ReplyInfo replyInfo = ReplyInfo.formReply(k);
            if (!Strings.isNullOrEmpty(replyInfo.getBeReplyId())) {
                replyInfo.setBeReplyName(idOrNames.get(replyInfo.getBeReplyId()));
            }
            return replyInfo;
        }).collect(Collectors.toList());


        // 获取所有 主comment
        List<ReplyInfo> baseComment = replies.stream().filter(k -> {
            return "0".equals(k.getFatherId());
        }).map(v -> {
            List<ReplyInfo> collect = replies.stream().filter(k -> {
                return v.getId().equals(k.getFatherId());
            }).collect(Collectors.toList());
            v.setReplyBody(collect);
            return v;
        }).collect(Collectors.toList());


        return baseComment;
    }
}
