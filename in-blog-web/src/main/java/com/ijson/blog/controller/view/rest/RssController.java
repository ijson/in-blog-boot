package com.ijson.blog.controller.view.rest;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.model.info.PostInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
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

    @RequestMapping(value = "/feed", produces = "application/xml")
    public String feed(HttpServletRequest request) {

        PostQuery postQuery = new PostQuery();
        postQuery.setStatus(Constant.PostStatus.pass);
        PageResult<PostEntity> pageResult = postService.find(null, postQuery, new Page());

        if (Objects.nonNull(pageResult) && CollectionUtils.isNotEmpty(pageResult.getDataList())) {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(RSS_TYPE);
            feed.setTitle(Strings.isNullOrEmpty(getConfig().getSiteName()) ? "IBO" : getConfig().getSiteName());
            feed.setLink(webCtx);
            feed.setDescription(Strings.isNullOrEmpty(getConfig().getSiteDesc()) ? "IBO" : getConfig().getSiteDesc());
            feed.setEncoding("UTF-8");
            feed.setCopyright(getConfig().getSiteCopyRight());
            feed.setWebMaster(getBlogAdminUser(request).getEmail());
            List<PostEntity> dataList = pageResult.getDataList();
            List<SyndEntry> entries = Lists.newArrayList();
            dataList.forEach(entity -> {
                PostInfo postInfo = PostInfo.create(entity);

                SyndContent description = new SyndContentImpl();
                description.setType("text/html");
                description.setValue(postInfo.getIntro());

                SyndEntry syndEntry = new SyndEntryImpl();
                syndEntry.setTitle(postInfo.getTitle());
                syndEntry.setLink(webCtx + "article/" + postInfo.getEname() + "/details/" + postInfo.getShamId() + ".html");
                syndEntry.setPublishedDate(new Date(postInfo.getCreateTime()));
                syndEntry.setDescription(description);
                entries.add(syndEntry);
            });
            feed.setEntries(entries);
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
