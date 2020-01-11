package com.ijson.blog.dao.query;

import lombok.Data;
import java.util.List;

@Data
public class TopicQuery {

	private String id;

	private String userId;

	private String moduleId;

	private long postCount;

}

