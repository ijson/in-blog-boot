package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.annotation.DocDocument;
import com.ijson.blog.bus.IEventBus;
import com.ijson.blog.bus.event.CreateTagEvent;
import com.ijson.blog.bus.event.DeleteArticleUpdateTagEvent;
import com.ijson.blog.bus.event.UpdateTagEvent;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.FileUploadEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.dao.model.FileType;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.V2Result;
import com.ijson.blog.service.model.info.PostInfo;
import com.ijson.blog.service.model.result.UploadResult;
import com.ijson.blog.util.DateUtils;
import com.ijson.blog.util.INFileUtil;
import com.ijson.blog.util.Md5Util;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @DocDocument(name = "博客添加", desc = "控制台执行添加,需要添加topic")
    @PostMapping("/create")
    public Result createPost(HttpServletRequest request, @RequestBody PostInfo post) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        if (Strings.isNullOrEmpty(context.getEname())) {
            throw new BlogCreateException(BlogBusinessExceptionCode.PLEASE_ADD_USER_ENAME);
        }

        if (Strings.isNullOrEmpty(post.getTitle())) {
            throw new BlogCreateException(BlogBusinessExceptionCode.TITLE_NOT_SET);
        }

        if (Strings.isNullOrEmpty(post.getContent())) {
            throw new BlogCreateException(BlogBusinessExceptionCode.CONTEXT_NOT_SET);
        }

        if (Strings.isNullOrEmpty(post.getTopicName())) {
            throw new BlogCreateException(BlogBusinessExceptionCode.LABEL_CANNOT_BE_EMPTY);
        }

        if (!Strings.isNullOrEmpty(post.getId())) {
            PostEntity postEntity = postService.findInternalById(post.getId());
            if (Objects.nonNull(postEntity)) {
                return updatePost(context, post, postEntity);
            }
        }
        //topicEntitys有判空
        PostEntity entity = PostEntity.create(post.getId(), context.getId(), post.getTitle(), post.getContent(), context.getEname());
        entity.setCreate(true);
        entity.setIndexMenuEname(post.getIndexMenuEname());
        entity.setTrigger(Constant.AuditTrigger.create);
        entity = postService.create(context, entity);
        log.info("文章创建成功,id:{},title:{}", entity.getId(), entity.getTitle());

        IEventBus.post(CreateTagEvent.create(context, entity.getId(), post.getTopicName()));
        return Result.ok("创建文章成功!");
    }

    private Result updatePost(AuthContext context, PostInfo post, PostEntity entity) {
        PostEntity newEntity = PostEntity.update(context, post.getId(), post.getTitle(), post.getContent());
        newEntity.setTrigger(Constant.AuditTrigger.update);
        newEntity.setIndexMenuEname(post.getIndexMenuEname());
        postService.create(context, newEntity);
        IEventBus.post(UpdateTagEvent.create(context, newEntity.getId(), post.getTopicName(), entity.getTopicId()));
        return Result.ok("更新文章成功!");
    }

    @PostMapping("/enable/{ename}/{shamId}")
    public Result enable(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, @RequestBody PostInfo post, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        PostEntity postEntity = postService.enable(ename, shamId, post.isEnable(), context);
        String reason = !post.isEnable() ? "启用" : "禁用";
        return Result.ok(reason + "成功");
    }


    /**
     * 置顶
     *
     * @param ename
     * @param shamId
     * @param post
     * @param request
     * @return
     */
    @PostMapping("/top/{ename}/{shamId}")
    public Result top(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, @RequestBody PostInfo post, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        postService.top(context, ename, shamId, post.isTop());
        String reason = post.isTop() ? "置顶" : "取消置顶";
        return Result.ok(reason + "成功");
    }

    /**
     * 加精
     *
     * @param ename
     * @param shamId
     * @param post
     * @param request
     * @return
     */
    @PostMapping("/fine/{ename}/{shamId}")
    public Result fine(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, @RequestBody PostInfo post, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        postService.fine(context, ename, shamId, post.isFine());
        String reason = post.isFine() ? "精华设置" : "精华取消";
        return Result.ok(reason + "成功");
    }

    @PostMapping("/audit/{ename}/{shamId}")
    public Result audit(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, @RequestBody PostInfo post, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        PostEntity postEntity = postService.audit(ename, shamId, post.getStatus(), post.getReason(), context);
        return Result.ok();
    }

    @PostMapping("/delete/{ename}/{shamId}")
    public Result delete(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        PostEntity postEntity = postService.findByShamIdInternal(ename, shamId, false);
        if (Objects.isNull(postEntity)) {
            return Result.error(-1, "文章不存在,请检查");
        }

        if (postEntity.isEnable()) {
            return Result.error(-1, "禁用后才可删除该文章");
        }
        PostEntity entity = postService.delete(postEntity, context);
        if (entity == null || entity.getDeleted()) {
            IEventBus.post(DeleteArticleUpdateTagEvent.create(context, postEntity.getId(), postEntity.getTopicId()));
            return Result.ok("删除成功");
        }

        return Result.error(-1, "删除失败");
    }


    @RequestMapping("/upload")
    @ResponseBody
    public UploadResult upload(@RequestParam("upload") MultipartFile file,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        // 获取文件名
        String fileName = file.getOriginalFilename();
        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        if (Objects.isNull(context)) {
            return UploadResult.create(fileName, "", BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED.getMessage());
        }

        String newFileName;
        String url = "";

        try {


            // 获取文件的后缀名
            int lastIndex = fileName.lastIndexOf(".");
            if (lastIndex == -1) {
                return UploadResult.create(fileName, url, "不支持的文件类型");
            }

            String suffixName = fileName.substring(lastIndex);

            newFileName = DateUtils.getInstance().getLongTime() + suffixName;


            FileType uploadType = FileType.files;
            if (INFileUtil.isImage(suffixName)) {
                uploadType = FileType.images;
            }

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
    public V2Result<PostInfo> list(Integer page, Integer limit, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        String keyWord = request.getParameter("title");
        return getList(context, page, limit, keyWord, new PostQuery());
    }


    public V2Result<PostInfo> getList(AuthContext context, Integer page, Integer limit, String keyWord, PostQuery query) {
        if (Objects.isNull(context)) {
            return new V2Result<>();
        }
        Page pageEntity = new Page();
        if (Objects.nonNull(page)) {
            pageEntity.setPageNumber(page);
        }
        if (Objects.nonNull(limit)) {
            pageEntity.setPageSize(limit);
        }


        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setLikeTitle(true);
            query.setTitle(keyWord);
        }

        PageResult<PostEntity> result = postService.find(context, query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<PostEntity> dataList = result.getDataList();
        List<PostInfo> posts = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            posts = PostInfo.postList(result);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(posts);
        v2Result.setMsg("");
        return v2Result;
    }

    @RequestMapping("/user/list")
    @ResponseBody
    public V2Result<PostInfo> userList(Integer page, Integer limit, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        String keyWord = request.getParameter("title");
        PostQuery query = new PostQuery();
        query.setCurrentUser(true);
        return getList(context, page, limit, keyWord, query);

    }

    @RequestMapping("/audit/list")
    @ResponseBody
    public V2Result<PostInfo> auditList(Integer page, Integer limit, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        String keyWord = request.getParameter("title");
        PostQuery query = new PostQuery();
        query.setStatus(Constant.PostStatus.in_progress);
        return getList(context, page, limit, keyWord, query);
    }


    @RequestMapping("/tag/list")
    @ResponseBody
    public V2Result<PostInfo> tagList(Integer page, Integer limit, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        String keyWord = request.getParameter("title");
        String tid = request.getParameter("tid");
        PostQuery query = new PostQuery();
        if (!Strings.isNullOrEmpty(tid)) {
            query.setTopicId(tid);
        }
        return getList(context, page, limit, keyWord, query);
    }

}