package com.ijson.blog.dao.query;

import lombok.Data;

@Data
public class TopicQuery {

    private String id;

    private String userId;

    private String moduleId;

    private long postCount;

    private String topicName;

}

