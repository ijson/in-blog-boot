package com.ijson.blog.dao.query;

import lombok.Data;
import java.util.List;

@Data
public class UserQuery {

	private String id;

	private String ename;

	private String cname;

	private String paasword;

	private String email;

	private String wechat;

	private String weibo;

	private String qq;

	private String editorType;

	private Boolean deleted;

}

