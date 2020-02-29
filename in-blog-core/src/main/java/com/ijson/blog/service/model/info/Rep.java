package com.ijson.blog.service.model.info;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.ReplyEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * desc: 后端评论model
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/29 5:33 PM
 */
public interface Rep {

    @Data
    class RepInfo {
        private String id;
        private String replyBy;//回复人
        private String replyByUserId; //回复人Id
        private String title;
        private long time;

        public static List<RepInfo> createList(List<ReplyEntity> dataList) {
            return dataList.stream().map(k -> {
                RepInfo repInfo = new RepInfo();
                repInfo.setId(k.getId());
                repInfo.setReplyBy("");
                repInfo.setReplyByUserId(k.getCreatedBy());
                repInfo.setTitle(k.getContent());
                repInfo.setTime(k.getCreateTime());
                return repInfo;
            }).collect(Collectors.toList());

        }
    }


    @Data
    class Result {
        private int code;
        private String msg;
        private long count = 0L;
        private List<RepInfo> data = Lists.newArrayList();

        public Result(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Result() {
        }
    }
}
