package com.ijson.blog.service.model;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.mongo.support.model.PageResult;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/7 6:25 PM
 */
@Data
public class Post {
    private String id;
    private String title;
    private String content;
    private String intro;
    private long createTime;
    private long views;
    private String userId;
    private long lastModifiedTime;
    private String topicName;
    private List<Topic> topics;
    private long pros;
    private boolean enable;
    private String userCname;
    private long reply;
    private String ename;
    private String shamId;

    private String imageUrl;
    private String cname;


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

    public static List<Post> indexPost(PageResult<PostEntity> result) {
        return indexPost(result, null);
    }

    public static List<Post> indexPost(PageResult<PostEntity> result, String keyWord) {

        if (result == null) {
            return Lists.newArrayList();
        }

        return result.getDataList().stream().map(key -> {
            Post post = new Post();
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
            return post;
        }).collect(Collectors.toList());
    }


    public static List<Post> postList(PageResult<PostEntity> result, Map<String, String> userOrCname) {

        if (result == null) {
            return Lists.newArrayList();
        }

        return result.getDataList().stream().map(key -> {
            Post post = Post.create(key);
            post.setContent(null);
            post.setEname(key.getEname());
            post.setShamId(key.getShamId());
            post.setUserCname(userOrCname.get(key.getUserId()));
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

    public static Post create(PostEntity entity) {
        Post post = new Post();
        post.setId(entity.getId());
        post.setTitle(entity.getTitle());
        post.setUserCname(entity.getCname());
        post.setContent(entity.getContent());
        post.setCreateTime(entity.getCreateTime());
        post.setUserId(entity.getUserId());
        post.setLastModifiedTime(entity.getLastModifiedTime());
        post.setViews(entity.getViews());
        post.setPros(entity.getPros());
        post.setReply(entity.getReply());
        post.setEname(entity.getEname());
        post.setIntro(entity.getContent());
        post.setImageUrl(entity.getContent());
        post.setEnable(entity.isEnable());
        post.setShamId(entity.getShamId());
        if (CollectionUtils.isNotEmpty(entity.getTopicName())) {
            post.setTopicName(getTpoicNames(entity.getTopicName()));
            post.setTopics(entity.getTopicName().stream().map(key -> {
                Topic topic = new Topic();
                topic.setId(key.getId());
                topic.setEname(key.getEname());
                topic.setShamId(key.getShamId());
                topic.setName(key.getTopicName());
                return topic;
            }).collect(Collectors.toList()));
        }
        return post;
    }

    public static Post createSimple(PostEntity entity) {
        Post post = new Post();
        post.setId(entity.getId());
        post.setEname(entity.getEname());
        post.setSimpleTitle(entity.getTitle());
        post.setViews(entity.getViews());
        post.setShamId(entity.getShamId());
        return post;
    }
}
