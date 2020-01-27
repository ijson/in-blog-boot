package com.ijson.blog.dao.query;

import com.ijson.blog.model.Constant;
import lombok.Data;
import java.util.List;

@Data
public class PostQuery {

	private String id;

	private String topicId;

	private String userId;

	private String content;

	private long pros;

	private long cons;

	private String title;

	private Integer pageSize;

	private Integer pageNumber;

	private Boolean enable;

	/**
	 * title like 的查询
	 */
	private volatile boolean likeTitle;

	private boolean currentUser;

	private Constant.PostStatus status;

}

