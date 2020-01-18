package com.ijson.blog.controller.admin;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.annotation.DocDocument;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.FileUploadEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.dao.model.FileType;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogCreateException;
import com.ijson.blog.exception.BlogUpdateException;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.FileUploadService;
import com.ijson.blog.service.PostService;
import com.ijson.blog.service.TopicService;
import com.ijson.blog.service.model.DTable;
import com.ijson.blog.service.model.Post;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.UploadResult;
import com.ijson.blog.util.DateUtils;
import com.ijson.blog.util.Md5Util;
import com.ijson.blog.util.PassportHelper;
import com.ijson.blog.util.sensitive.SensitiveFilter;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/21 2:18 PM
 */
@Slf4j
@RestController
@RequestMapping("/post")
public class PostAction extends BaseController {

    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${web.ctx}")
    private String webCtx;

    @Value("${cdn.server}")
    private String cdnServer;

    @Value("${cdn.upload.path}")
    private String cdnUploadPath;

    @DocDocument(name = "博客添加", desc = "控制台执行添加,需要添加topic")
    @PostMapping("/create")
    public Result createPost(HttpServletRequest request, HttpSession session, @RequestBody Post post) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        if (!Strings.isNullOrEmpty(post.getId())) {
            return updatePost(request, post);
        }

        if (Strings.isNullOrEmpty(post.getTitle())) {
            throw new BlogCreateException(BlogBusinessExceptionCode.TITLE_NOT_SET);
        }

        if (Strings.isNullOrEmpty(post.getContent())) {
            throw new BlogCreateException(BlogBusinessExceptionCode.CONTEXT_NOT_SET);
        }

        // 使用默认单例（加载默认词典）
//        SensitiveFilter filter = SensitiveFilter.DEFAULT;
//        // 进行过滤
//        boolean filted = filter.filter(post.getContent());
//        if (filted) {
//            throw new ReplyCreateException(BlogBusinessExceptionCode.SENSITIVE_TEXT_EXISTS_PLEASE_CHECK_AND_RESUBMIT);
//        }

        List<TopicEntity> topics = null;
        //存在tag 先保存tag,然后保存文章
        if (!Strings.isNullOrEmpty(post.getTopicName())) {
            topics = topicService.findTopicByTopicNameAndIncCount(post.getTopicName().trim(), context);
        }

        //topicEntitys有判空
        PostEntity entity = PostEntity.create(post.getId(), context.getId(), post.getTitle(), post.getContent(), topics, context.getEname());

        entity = postService.createPost(context, entity);
        log.info("文章创建成功,id:{},title:{}", entity.getId(), entity.getTitle());
        return Result.ok("创建文章成功!");
    }

    private Result updatePost(HttpServletRequest request, @RequestBody Post post) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        if (Strings.isNullOrEmpty(post.getId())) {
            throw new BlogUpdateException(BlogBusinessExceptionCode.POST_UPDATE_ID_NOT_FOUND);
        }
        if (Strings.isNullOrEmpty(post.getTitle())) {
            throw new BlogUpdateException(BlogBusinessExceptionCode.TITLE_NOT_SET);
        }

        if (Strings.isNullOrEmpty(post.getContent())) {
            throw new BlogUpdateException(BlogBusinessExceptionCode.CONTEXT_NOT_SET);
        }

        PostEntity entity = postService.findById(post.getId());

        List<TopicEntity> oldTopicNames = entity.getTopicName();
        List<String> newTopic = Lists.newArrayList(post.getTopicName().split(","));

        //1. 对比本次提交的tag 和 库中的tag 的增量name
        Map<String, String> oldTopics = oldTopicNames.stream().collect(Collectors.toMap(TopicEntity::getTopicName, TopicEntity::getId));

        // 获取增量的name
        List<String> increment = newTopic.stream().filter(k -> {
            return !oldTopics.containsKey(k);
        }).collect(Collectors.toList());

        //2. 对比本次提交的tag 和 库中的tag 的减少的name
        List<String> reduce = oldTopics.keySet().stream().filter(k -> {
            return !newTopic.contains(k);
        }).collect(Collectors.toList());


        //3. 保存增量的tag到库中，并设置post
        List<TopicEntity> incrementTopicEntity = null;
        if (CollectionUtils.isNotEmpty(increment)) {
            incrementTopicEntity = topicService.findTopicByTopicNameAndIncCount(Joiner.on(",").join(increment), context);
        }

        //4. 减少的name  tag-1
        List<String> reduceId = Lists.newArrayList();
        for (String name : reduce) {
            String id = oldTopics.get(name);
            if (!Strings.isNullOrEmpty(id)) {
                reduceId.add(id);
            }
        }
        //5.  并从post上删除 减少的name

        // 获取没删除的tag
        List<TopicEntity> notDeleteTopic = oldTopicNames.stream().filter(k -> {
            return !reduce.contains(k.getTopicName());
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(incrementTopicEntity)) {
            notDeleteTopic.addAll(incrementTopicEntity);
        }

        //6. 更新post
        PostEntity newEntity = PostEntity.update(context, post.getId(), post.getTitle(), post.getContent(), notDeleteTopic, null);

        postService.createPost(context, newEntity);


        return Result.ok("更新文章成功!");
    }

    @PostMapping("/enable/{ename}/{shamId}")
    public Result enable(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, @RequestBody Post post, HttpServletRequest request) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        PostEntity postEntity = postService.enable(ename, shamId, !post.isEnable(), context);
        String reason = !post.isEnable() ? "启用" : "禁用";
        return Result.ok(reason + "成功");
    }

    @PostMapping("/delete/{ename}/{shamId}")
    public Result delete(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, HttpServletRequest request) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        PostEntity postEntity = postService.findByShamIdInternal(ename, shamId,false);
        if (postEntity != null && postEntity.isEnable()) {
            return Result.error(-1, "禁用后才可删除该文章");
        }
        PostEntity entity = postService.delete(postEntity.getId(), context);
        if (entity == null || entity.getDeleted()) {
            return Result.ok("删除成功");
        }

        return Result.error(-1, "删除失败");
    }


    @RequestMapping("/upload")
    @ResponseBody
    public UploadResult upload(@RequestParam("upload") MultipartFile file,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        String type = request.getParameter("type");

        FileType uploadType = FileType.files;
        if ("images".equals(type)) {
            uploadType = FileType.images;
        }
        String newFileName = "";
        String url = "";
        // 获取文件名
        String fileName = file.getOriginalFilename();

        try {

            AuthContext context = getContext(request);
            if (Objects.isNull(context)) {
                return UploadResult.create(fileName, url, BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED.getMessage());
            }
            // 获取文件的后缀名
            int lastIndex = fileName.lastIndexOf(".");
            if (lastIndex == -1) {
                return UploadResult.create(fileName, url, "不支持的文件类型");
            }

            String suffixName = fileName.substring(lastIndex);

            newFileName = DateUtils.getInstance().getLongTime() + suffixName;


            String filePath = cdnUploadPath + uploadType + "/" + newFileName;
            log.info("写入文件:{}", filePath);
            File path = new File(filePath);
            String md5 = Md5Util.getMD5ByInputStram(file.getInputStream());
            if (!Strings.isNullOrEmpty(md5)) {
                FileUploadEntity dataByMd5 = fileUploadService.findDataByMd5(md5);
                if (Objects.nonNull(dataByMd5)) {
                    newFileName = dataByMd5.getNewName();
                    log.info("图片已上传 文件名称:{},别名:{},md5:{}", dataByMd5.getFileName(), dataByMd5.getNewName(), dataByMd5.getMd5());
                } else {
                    FileUtils.copyInputStreamToFile(file.getInputStream(), path);
                    FileUploadEntity fileUploadEntity = FileUploadEntity.create(uploadType, fileName, newFileName, md5, suffixName, cdnServer + uploadType + "/" + newFileName, context);
                    fileUploadService.create(fileUploadEntity);
                }

                url = cdnServer + uploadType + "/" + newFileName;
            }


            return UploadResult.create(fileName, url);
        } catch (IOException e) {
            log.error("", e);
            return UploadResult.create(fileName, url, "图片上传失败");
        }
    }


    @RequestMapping("/list")
    @ResponseBody
    public DTable list(Integer start, Integer length, HttpServletRequest request) {

        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return DTable.create(Lists.newArrayList(), null, start);
        }


        String keyWord = request.getParameter("search[value]");

        Page page = new Page();
        if (Objects.nonNull(start)) {
            page.setPageNumber((start / length) + 1);
        }
        if (Objects.nonNull(length)) {
            page.setPageSize(length);
        }


        PostQuery query = new PostQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setLikeTitle(true);
            query.setTitle(keyWord);
        }

        PageResult<PostEntity> result = postService.find(query, page);

        List<PostEntity> dataList = result.getDataList();
        List<Post> posts = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            posts = Post.postList(result);
        }

        return DTable.create(posts, result, start);
    }

}