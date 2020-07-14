package com.ijson.blog.service.model.info;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.model.Constant;
import com.ijson.mongo.support.model.PageResult;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/7 6:25 PM
 */
@Data
public class PostInfo {
    private String id;
    private String title;
    private String content;
    private String intro;
    private long createTime;
    private long views;
    private String userId;
    private long lastModifiedTime;
    private String lastModifiedBy;
    private String topicName;
    private List<TopicInfo> topics;
    private long pros;
    private boolean enable;
    private String userCname;
    private long reply;
    private String ename;
    private String shamId;

    private String imageUrl;
    private String cname;
    private String draftId;
    private Constant.PostStatus status;
    private Constant.AuditTrigger trigger;
    private String reason;

    private String indexMenuEname;

    private boolean top;
    private boolean fine;

    private void setIntro(String intro) {
        String info = StringEscapeUtils.unescapeHtml(intro).replace("\n", "");
        Document document = Jsoup.parse(info);
        String text = document.text();
        if (text.length() > 500) {
            this.intro = text.trim().substring(0, 500);
        } else {
            this.intro = text;
        }
    }

    private void setIntro(String intro, String keyWord) {
        String info = StringEscapeUtils.unescapeHtml(intro).replace("\n", "");
        Document document = Jsoup.parse(info);
        String text = document.text();

        if (Strings.isNullOrEmpty(keyWord)) {
            if (text.length() > 500) {
                this.intro = text.trim().substring(0, 500);
            } else {
                this.intro = text;
            }
        } else {
            if (text.length() > 500) {
                this.intro = text.trim().substring(0, 500).replaceAll("(?i)" + keyWord, "<em>" + keyWord + "</em>");
            } else {
                this.intro = text.replaceAll("(?i)" + keyWord, "<em>" + keyWord + "</em>");
            }
        }
    }

    public void setImageUrl(String content) {
        String info = StringEscapeUtils.unescapeHtml(content).replace("\n", "");
        this.imageUrl = getImgStr(info);
    }

    public static String getImgStr(String htmlStr) {
        List<String> list = Lists.newArrayList();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        // String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                list.add(m.group(1));
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        return list.get(0);
    }

    public static List<PostInfo> indexPost(PageResult<PostEntity> result) {
        return indexPost(result, null);
    }

    public static List<PostInfo> indexPost(PageResult<PostEntity> result, String keyWord) {

        if (result == null) {
            return Lists.newArrayList();
        }

        return result.getDataList().stream().map(key -> {
            PostInfo post = new PostInfo();
            post.setId(key.getId());
            //post.setContent(key.getContent());
            post.setIntro(key.getContent(), keyWord);
            if (!Strings.isNullOrEmpty(keyWord)) {
                post.setTitle(key.getTitle().replaceAll("(?i)" + keyWord, "<em>" + keyWord + "</em>"));
            } else {
                post.setTitle(key.getTitle());
            }
            post.setCname(key.getCname());
            post.setCreateTime(key.getCreateTime());
            post.setViews(key.getViews());
            post.setEnable(key.isEnable());
            post.setReply(key.getReply());
            post.setEname(key.getEname());
            post.setShamId(key.getShamId());
            post.setLastModifiedBy(key.getLastModifiedBy());
            post.setDraftId(key.getDraftId());
            post.setStatus(key.getStatus());
            post.setReason(key.getReason());
            post.setTrigger(key.getTrigger());
            post.setIndexMenuEname(key.getIndexMenuEname());
            post.setTop(key.isTop());
            post.setFine(key.isFine());
            return post;
        }).collect(Collectors.toList());
    }


    /**
     * web页面专用 直接是前端使用
     * @param result
     * @return
     */
    public static List<PostInfo> postList(PageResult<PostEntity> result) {

        if (result == null) {
            return Lists.newArrayList();
        }

        return result.getDataList().stream().map(key -> {
            PostInfo post = PostInfo.create(key);
            post.setContent(null);
            post.setDraftId(key.getDraftId());
            post.setEname(key.getEname());
            post.setShamId(key.getShamId());
            post.setUserCname(key.getCname());
            post.setLastModifiedBy(key.getLastModifiedBy());
            post.setStatus(key.getStatus());
            post.setReason(key.getReason());
            post.setTrigger(key.getTrigger());
            post.setIndexMenuEname(key.getIndexMenuEname());
            post.setTop(key.isTop());
            post.setFine(key.isFine());
            return post;
        }).collect(Collectors.toList());
    }

    /**
     * 后台列表页使用
     * @param result
     * @return
     */
    public static List<PostInfo> postDraftList(PageResult<PostDraftEntity> result) {

        if (result == null) {
            return Lists.newArrayList();
        }

        return result.getDataList().stream().map(key -> {
            PostInfo post = PostInfo.create(key);
            post.setContent(null);
            post.setEname(key.getEname());
            post.setShamId(key.getShamId());
            post.setUserCname(key.getCname());
            post.setCname(key.getCname());
            post.setLastModifiedBy(key.getLastModifiedBy());
            return post;
        }).collect(Collectors.toList());
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setSimpleTitle(String title) {
        this.title = title;
    }


    public static String getTpoicNames(List<TopicEntity> topicName) {
        return Joiner.on(",").join(topicName.stream().map(TopicEntity::getTopicName).collect(Collectors.toSet()));
    }

    public static PostInfo create(PostDraftEntity entity) {
        PostInfo post = new PostInfo();
        post.setId(entity.getId());
        post.setTitle(entity.getTitle());
        post.setContent(entity.getContent());
        post.setCreateTime(entity.getCreateTime());
        post.setUserId(entity.getUserId());
        post.setLastModifiedTime(entity.getLastModifiedTime());
        post.setEname(entity.getEname());
        post.setIntro(entity.getContent());
        post.setImageUrl(entity.getContent());
        post.setShamId(entity.getShamId());
        post.setTopicName(entity.getTopicNames());
        post.setLastModifiedBy(entity.getLastModifiedBy());
        post.setIndexMenuEname(entity.getIndexMenuEname());
        return post;
    }

    public static PostInfo create(PostEntity key) {
        PostInfo post = new PostInfo();
        post.setId(key.getId());
        post.setTitle(key.getTitle());
        post.setUserCname(key.getCname());
        post.setContent(key.getContent());
        post.setCreateTime(key.getCreateTime());
        post.setUserId(key.getUserId());
        post.setLastModifiedTime(key.getLastModifiedTime());
        post.setViews(key.getViews());
        post.setPros(key.getPros());
        post.setReply(key.getReply());
        post.setEname(key.getEname());
        post.setIntro(key.getContent());
        post.setImageUrl(key.getContent());
        post.setEnable(key.isEnable());
        post.setShamId(key.getShamId());
        post.setDraftId(key.getDraftId());
        post.setStatus(key.getStatus());
        post.setReason(key.getReason());
        post.setTrigger(key.getTrigger());
        post.setLastModifiedBy(key.getLastModifiedBy());
        post.setIndexMenuEname(key.getIndexMenuEname());
        if (CollectionUtils.isNotEmpty(key.getTopicName())) {
            post.setTopicName(getTpoicNames(key.getTopicName()));
            post.setTopics(key.getTopicName().stream().map(topKey -> {
                TopicInfo topic = new TopicInfo();
                topic.setId(topKey.getId());
                topic.setEname(topKey.getEname());
                topic.setShamId(topKey.getShamId());
                topic.setName(topKey.getTopicName());
                return topic;
            }).collect(Collectors.toList()));
        }
        post.setTop(key.isTop());
        post.setFine(key.isFine());
        return post;
    }

    /**
     * 热门文章,最近发表使用,不需要太多参数
     * @param entity
     * @return
     */
    public static PostInfo createSimple(PostEntity entity) {
        PostInfo post = new PostInfo();
        post.setId(entity.getId());
        post.setEname(entity.getEname());
        post.setSimpleTitle(entity.getTitle());
        post.setViews(entity.getViews());
        post.setShamId(entity.getShamId());
        post.setLastModifiedBy(entity.getLastModifiedBy());
        post.setCreateTime(entity.getCreateTime());
        return post;
    }
}
