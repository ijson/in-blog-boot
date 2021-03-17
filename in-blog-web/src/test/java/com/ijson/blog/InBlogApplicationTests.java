package com.ijson.blog;

import com.ijson.blog.dao.CommentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogWebBootstrap.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InBlogApplicationTests {

	@Test
	public void contextLoads() {
	}

}
