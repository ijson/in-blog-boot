package com.ijson.blog.controller.view.rest;

import com.google.common.base.Strings;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.service.model.info.PostInfo;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/7/1 12:30 上午
 */
@Slf4j
@RestController
@RequestMapping("/rss")
public class RssController extends BaseController {
    private static final String RSS_TYPE = "rss_2.0";

    @RequestMapping("/hello")
    public String hello() {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType(RSS_TYPE);
        feed.setTitle("IBO 开源博客");
        feed.setLink("https://www.ijson.net");
        feed.setDescription("blog,ijson,json,springboot,IBO,博客,开源博客,in-blog-boot");
        feed.setEncoding("UTF-8");
        try {
            SyndFeedOutput output = new SyndFeedOutput();
            return output.outputString(feed);
        } catch (Exception ex) {
            log.error("", ex);
        }
        return "";
    }


    @RequestMapping("/article")
    public String article(@RequestParam("en") String en, @RequestParam("sid") String sid) {

        if (Strings.isNullOrEmpty(en) || Strings.isNullOrEmpty(sid)) {
            return "";
        }

        PostEntity entity = postService.findByShamIdInternal(en, sid, false);

        if (Objects.nonNull(entity)) {
            PostInfo postInfo = PostInfo.create(entity);
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(RSS_TYPE);
            feed.setTitle(postInfo.getTitle());
            feed.setLink("https://www.ijson.net/article/" + en + "/details/" + sid + ".html");
            feed.setDescription(postInfo.getIntro());
            feed.setEncoding("UTF-8");
            feed.setAuthor(en);
            feed.setCopyright("IJSON");
            feed.setWebMaster("414648691@qq.com");
            try {
                SyndFeedOutput output = new SyndFeedOutput();
                return output.outputString(feed);
            } catch (Exception ex) {
                log.error("", ex);
            }
        }


        return "";
    }


}
